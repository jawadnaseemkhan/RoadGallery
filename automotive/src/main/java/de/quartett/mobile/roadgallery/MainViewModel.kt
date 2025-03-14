package de.quartett.mobile.roadgallery

import android.car.Car
import android.car.hardware.CarSensorManager
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import global.covesa.sdk.client.push.ActionEvent
import global.covesa.sdk.client.push.PushServiceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    context: Context,
    pushUiState: PushUiState = PushUiState(),
) : ViewModel() {
    var pushUiState by mutableStateOf(pushUiState)
        private set

    val speedFlow = MutableStateFlow(0f)
    val gearFlow = MutableStateFlow(0)
    val fuelLevelFlow = MutableStateFlow(0f)
    val rangeRemainingFlow = MutableStateFlow(0f)

    var car:Car? = null

    init {

        initCar(context)

        viewModelScope.launch {
            PushServiceImpl.events.collect {
                this@MainViewModel.pushUiState =
                    this@MainViewModel.pushUiState.copy(registered = it.registered)
            }
        }
    }

    public fun connect() {
        car?.run {
            if(!isConnected && !isConnecting) {
                connect()
            }
        }
    }

    public fun disconnect() {
        car?.run {
            if(isConnected) {
                disconnect()
            }
        }
    }

    private fun initCar(context: Context) {
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
            Log.wtf("XXX", "FEATURE_AUTOMOTIVE is not available!")
            return
        }

        if(car != null) {
            Log.wtf("XXX", "CAR is initialized!")
            return
        }

        car = Car.createCar(context, object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                onCarServiceReady()
            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                // on failure callback
            }
        })
    }

    private fun onCarServiceReady() {
        watchSpeedSensor()
        watchFuelLevelSensor()
        watchRPMSensor()
    }

    //    Push
    fun sendPushNotification() {
        viewModelScope.launch {
            ActionEvent.emit(ActionEvent(ActionEvent.Type.SendNotification))
        }
    }

    fun registerPushService() {
        viewModelScope.launch {
            ActionEvent.emit(ActionEvent(ActionEvent.Type.RegisterPush))
        }
    }

    fun unregisterPushService() {
        viewModelScope.launch {
            ActionEvent.emit(ActionEvent(ActionEvent.Type.UnregisterPush))
            pushUiState = pushUiState.copy(registered = false)
        }
    }

    //    Car sensor
    private fun watchSpeedSensor() {
        val car = car ?: return
        val sensorManager = car.getCarManager(Car.SENSOR_SERVICE) as CarSensorManager
        sensorManager.registerListener(
            { carSensorEvent ->
                Log.d("MainActivity", "Speed: ${carSensorEvent.floatValues[0]}")
                speedFlow.value = carSensorEvent.floatValues[0]
            },
            CarSensorManager.SENSOR_TYPE_CAR_SPEED,
            CarSensorManager.SENSOR_RATE_NORMAL
        )
        sensorManager.registerListener(
            { carSensorEvent ->
                Log.d("MainActivity", "Gear: ${carSensorEvent.intValues[0]}")
                gearFlow.value = carSensorEvent.intValues[0]
            },
            CarSensorManager.SENSOR_TYPE_GEAR,
            CarSensorManager.SENSOR_RATE_NORMAL
        )
    }
    private fun watchFuelLevelSensor() {
        val car = car ?: return
        val sensorManager = car.getCarManager(Car.SENSOR_SERVICE) as CarSensorManager
        sensorManager.registerListener(
            { carSensorEvent ->
                Log.d("MainActivity", "Fuel Level: ${carSensorEvent.floatValues[0]}")
                fuelLevelFlow.value = carSensorEvent.floatValues[0]
            },
            CarSensorManager.SENSOR_TYPE_FUEL_LEVEL,
            CarSensorManager.SENSOR_RATE_NORMAL)
    }

    private fun watchRPMSensor() {
        val car = car ?: return
        val sensorManager = car.getCarManager(Car.SENSOR_SERVICE) as CarSensorManager
        sensorManager.registerListener(
            { carSensorEvent ->
                Log.d("MainActivity", "Range Remaining: ${carSensorEvent.floatValues[0]}")
                rangeRemainingFlow.value = carSensorEvent.floatValues[0]
            },
            CarSensorManager.SENSOR_TYPE_RPM,
            CarSensorManager.SENSOR_RATE_NORMAL)
    }
}