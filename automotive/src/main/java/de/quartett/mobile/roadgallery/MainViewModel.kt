package de.quartett.mobile.roadgallery

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import global.covesa.sdk.client.push.ActionEvent
import global.covesa.sdk.client.push.PushServiceImpl
import kotlinx.coroutines.launch

class MainViewModel(
    pushUiState: PushUiState = PushUiState(),
) : ViewModel() {
    var pushUiState by mutableStateOf(pushUiState)
        private set

    init {
        viewModelScope.launch {
            PushServiceImpl.events.collect {
                this@MainViewModel.pushUiState =
                    this@MainViewModel.pushUiState.copy(registered = it.registered)
            }
        }
    }

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
}