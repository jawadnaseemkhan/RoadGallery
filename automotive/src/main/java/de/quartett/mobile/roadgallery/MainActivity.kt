package de.quartett.mobile.roadgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import com.example.jetpackcomposeexample.ui.theme.RoadGalleryTheme
import android.car.Car
import android.car.hardware.CarSensorManager
import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.collectAsState

import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {
    private lateinit var car : Car
    private val permissions = arrayOf(Car.PERMISSION_SPEED, Car.PERMISSION_POWERTRAIN)

    private val speedFlow = MutableStateFlow(0f)
    private val gearFlow = MutableStateFlow(0)
    private val fuelLevelFlow = MutableStateFlow(0f)
    private val rangeRemainingFlow = MutableStateFlow(0f)
    private val engineOilLevelFlow = MutableStateFlow(0f)
    private val outsideTemperatureFlow = MutableStateFlow(0f)
    private val fuelDoorOpenFlow = MutableStateFlow(false)
    private val ignitionStateFlow = MutableStateFlow(0)
    private val parkingBrakeFlow = MutableStateFlow(false)
    private val tractionControlActiveFlow = MutableStateFlow(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCar()
        setContent {
            RoadGalleryTheme {
                val speed by speedFlow.collectAsState()
                val gear by gearFlow.collectAsState()
                val fuelLevel by fuelLevelFlow.collectAsState()
                val rangeRemaining by rangeRemainingFlow.collectAsState()
                val engineOilLevel by engineOilLevelFlow.collectAsState()
                val outsideTemperature by outsideTemperatureFlow.collectAsState()
                val fuelDoorOpen by fuelDoorOpenFlow.collectAsState()
                val ignitionState by ignitionStateFlow.collectAsState()
                val parkingBrake by parkingBrakeFlow.collectAsState()
                val tractionControlActive by tractionControlActiveFlow.collectAsState()
                MainView(
                    speed, gear, fuelLevel, rangeRemaining, engineOilLevel, outsideTemperature,
                    fuelDoorOpen, ignitionState, parkingBrake, tractionControlActive
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            if (!car.isConnected && !car.isConnecting) car.connect()
        } else {
            requestPermissions(permissions, 0)
        }

        if (!car.isConnected && !car.isConnecting) car.connect()
    }

    override fun onPause() {
        if (car.isConnected) car.disconnect()
        super.onPause()
    }

    private fun initCar() {
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
            Log.wtf("XXX", "FEATURE_AUTOMOTIVE is not available!")
            return
        }

        if(::car.isInitialized) {
            Log.wtf("XXX", "CAR is not initialized!")
            return
        }

        car = Car.createCar(this, object : ServiceConnection {
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
        watchAdditionalSensors()
    }

    private fun watchSpeedSensor() {
        val sensorManager = car.getCarManager(Car.SENSOR_SERVICE) as CarSensorManager
        sensorManager.registerListener(
            { carSensorEvent ->
                Log.d("MainActivity", "Speed: ${carSensorEvent.floatValues[0]}")
                speedFlow.value = carSensorEvent.floatValues[0]
            },
            CarSensorManager.SENSOR_TYPE_CAR_SPEED,
            CarSensorManager.SENSOR_RATE_NORMAL)
        sensorManager.registerListener(
            { carSensorEvent ->
                Log.d("MainActivity", "Gear: ${carSensorEvent.intValues[0]}")
                gearFlow.value = carSensorEvent.intValues[0]
            },
            CarSensorManager.SENSOR_TYPE_GEAR,
            CarSensorManager.SENSOR_RATE_NORMAL)
    }

    private fun watchFuelLevelSensor() {
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
        val sensorManager = car.getCarManager(Car.SENSOR_SERVICE) as CarSensorManager
        sensorManager.registerListener(
            { carSensorEvent ->
                Log.d("MainActivity", "Range Remaining: ${carSensorEvent.floatValues[0]}")
                rangeRemainingFlow.value = carSensorEvent.floatValues[0]
            },
            CarSensorManager.SENSOR_TYPE_RPM,
            CarSensorManager.SENSOR_RATE_NORMAL)
    }

    private fun watchAdditionalSensors() {
        val sensorManager = car.getCarManager(Car.SENSOR_SERVICE) as CarSensorManager

        sensorManager.registerListener(
            { carSensorEvent ->
                engineOilLevelFlow.value = carSensorEvent.floatValues[0]
            },
            CarSensorManager.SENSOR_TYPE_ENGINE_OIL_LEVEL,
            CarSensorManager.SENSOR_RATE_NORMAL
        )

        sensorManager.registerListener(
            { carSensorEvent ->
                outsideTemperatureFlow.value = carSensorEvent.floatValues[0]
            },
            CarSensorManager.SENSOR_TYPE_ENV_OUTSIDE_TEMPERATURE,
            CarSensorManager.SENSOR_RATE_NORMAL
        )

        sensorManager.registerListener(
            { carSensorEvent ->
                fuelDoorOpenFlow.value = carSensorEvent.intValues[0] == 1
            },
            CarSensorManager.SENSOR_TYPE_FUEL_DOOR_OPEN,
            CarSensorManager.SENSOR_RATE_NORMAL
        )

        sensorManager.registerListener(
            { carSensorEvent ->
                ignitionStateFlow.value = carSensorEvent.intValues[0]
            },
            CarSensorManager.SENSOR_TYPE_IGNITION_STATE,
            CarSensorManager.SENSOR_RATE_NORMAL
        )

        sensorManager.registerListener(
            { carSensorEvent ->
                parkingBrakeFlow.value = carSensorEvent.intValues[0] == 1
            },
            CarSensorManager.SENSOR_TYPE_PARKING_BRAKE,
            CarSensorManager.SENSOR_RATE_NORMAL
        )

        sensorManager.registerListener(
            { carSensorEvent ->
                tractionControlActiveFlow.value = carSensorEvent.intValues[0] == 1
            },
            CarSensorManager.SENSOR_TYPE_TRACTION_CONTROL_ACTIVE,
            CarSensorManager.SENSOR_RATE_NORMAL
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray,
                                            deviceId: Int) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)

        if (permissions.indexOf(Car.PERMISSION_SPEED) != -1
            && grantResults[permissions.indexOf(Car.PERMISSION_SPEED)] == PackageManager.PERMISSION_GRANTED
            && permissions.indexOf(Car.PERMISSION_POWERTRAIN) != -1
            && grantResults[permissions.indexOf(Car.PERMISSION_POWERTRAIN)] == PackageManager.PERMISSION_GRANTED) {
            if (!car.isConnected && !car.isConnecting) car.connect()
        }
    }
}