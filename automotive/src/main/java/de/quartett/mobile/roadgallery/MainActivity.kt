package de.quartett.mobile.roadgallery

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.jetpackcomposeexample.ui.theme.RoadGalleryTheme
import android.car.Car
import android.content.pm.PackageManager
import global.covesa.sdk.client.push.ActionEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    private var viewModel: MainViewModel? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
        setContent {
            RoadGalleryTheme {
                val viewModel: MainViewModel = viewModel {
                    MainViewModel(
                        context = this@MainActivity,
                        pushUiState = PushUiState(this@MainActivity),
                    ).also { viewModel = it }
                }
                MainView(viewModel)
            }
        }
        subscribeActions()
    }

    private var job : Job? = null
    private fun subscribeActions() {
        job = CoroutineScope(Dispatchers.IO).launch {
            ActionEvent.events.collect {
                it.handleAction(this@MainActivity)
            }
        }
    }

    private fun requestPermissions() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Car.PERMISSION_SPEED, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }

        if (checkSelfPermission(Car.PERMISSION_POWERTRAIN) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Car.PERMISSION_POWERTRAIN), 1
            )
        }

        if (checkSelfPermission(Car.PERMISSION_ENERGY) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Car.PERMISSION_ENERGY), 1
            )
        }
    }
}

