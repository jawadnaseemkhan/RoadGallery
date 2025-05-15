package de.quartett.mobile.roadgallery

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
import android.Manifest

class MainActivity : ComponentActivity() {
    private val permissions = arrayOf(Manifest.permission.CAMERA)

    private var viewModel: MainViewModel? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    override fun onResume() {
        super.onResume()

        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            viewModel?.connect()
        } else {
            requestPermissions(permissions, 0)
        }

        viewModel?.connect()
    }

    override fun onPause() {
        viewModel?.disconnect()
        super.onPause()
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
            viewModel?.connect()
        }
    }
}

