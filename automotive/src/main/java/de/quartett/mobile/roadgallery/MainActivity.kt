package de.quartett.mobile.roadgallery

import android.car.Car
import android.car.hardware.CarSensorManager
import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.jetpackcomposeexample.ui.theme.RoadGalleryTheme
import kotlinx.coroutines.flow.MutableStateFlow


class MainActivity : ComponentActivity() {
    private lateinit var car : Car
    private val permissions = arrayOf(Car.PERMISSION_SPEED,Car.PERMISSION_POWERTRAIN)

    private val speedFlow = MutableStateFlow(0f)
    private val gearFlow = MutableStateFlow(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCar()
        setContent {
            RoadGalleryTheme {
                // A surface container using the 'background' color from the theme
                val speed by speedFlow.collectAsState()
                val gear by gearFlow.collectAsState()
                MainView(speed, gear)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if(checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            if (!car.isConnected && !car.isConnecting) car.connect()
        } else {
            requestPermissions(permissions, 0)
        }

        if (!car.isConnected && !car.isConnecting) car.connect()
    }

    override fun onPause() {
        if(car.isConnected) car.disconnect()
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

