package de.quartett.mobile.roadgallery

import android.car.VehicleGear
import android.car.VehicleIgnitionState
import android.car.VehiclePropertyIds
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paradoxcat.karpropertymanager.KarPropertyManager
import global.covesa.sdk.client.push.ActionEvent
import global.covesa.sdk.client.push.PushServiceImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    context: Context,
    pushUiState: PushUiState = PushUiState(),
) : ViewModel() {
    var pushUiState by mutableStateOf(pushUiState)
        private set

    private val kpm = KarPropertyManager(context, viewModelScope)

    // Dangerous permission Car#PERMISSION_SPEED to read property.
    val speedFlow = kpm.flowOfProperty<Float>(VehiclePropertyIds.PERF_VEHICLE_SPEED, 0, 0.5F)
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0F)

    // Normal permission Car#PERMISSION_POWERTRAIN to read property.
    val gearFlow = kpm.flowOfProperty<Int>(VehiclePropertyIds.CURRENT_GEAR, 0, 0.5F)
        .stateIn(viewModelScope, SharingStarted.Eagerly, VehicleGear.GEAR_UNKNOWN)

    // Dangerous permission Car#PERMISSION_ENERGY to read property.
    val fuelLevelFlow = kpm.flowOfProperty<Float>(VehiclePropertyIds.FUEL_LEVEL, 0, 0.5F)
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0F)

    // Dangerous permission Car#PERMISSION_ENERGY or
    // Signature|Privileged permission "android.car.permission.ADJUST_RANGE_REMAINING" to read property.
    val rangeRemainingFlow = kpm.flowOfProperty<Float>(VehiclePropertyIds.RANGE_REMAINING, 0, 0.5F)
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0F)

    // Signature|Privileged permission "android.car.permission.CAR_ENGINE_DETAILED" to read property.
    // NOTE: will not work without platform signature
    val engineOilLevelFlow = kpm.flowOfProperty<Int>(VehiclePropertyIds.ENGINE_OIL_LEVEL, 0, 0.5F)
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    // Normal permission Car#PERMISSION_EXTERIOR_ENVIRONMENT to read property.
    val outsideTemperatureFlow =
        kpm.flowOfProperty<Float>(VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE, 0, 0.5F)
            .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0F)

    // Normal permission Car#PERMISSION_ENERGY_PORTS or
    // Signature|Privileged permission "android.car.permission.CONTROL_CAR_ENERGY_PORTS"to read property.
    val fuelDoorOpenFlow = kpm.flowOfProperty<Boolean>(VehiclePropertyIds.FUEL_DOOR_OPEN, 0, 0.5F)
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // Normal permission Car#PERMISSION_POWERTRAIN to read property.
    val ignitionStateFlow = kpm.flowOfProperty<Int>(VehiclePropertyIds.IGNITION_STATE, 0, 0.5F)
        .stateIn(viewModelScope, SharingStarted.Eagerly, VehicleIgnitionState.UNDEFINED)

    // Normal permission Car#PERMISSION_POWERTRAIN to read property.
    val parkingBrakeFlow = kpm.flowOfProperty<Boolean>(VehiclePropertyIds.PARKING_BRAKE_ON, 0, 0.5F)
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // Signature|Privileged permission "android.car.permission.CAR_DYNAMICS_STATE" to read property.
    // NOTE: will not work without platform signature
    val tractionControlActiveFlow =
        kpm.flowOfProperty<Boolean>(VehiclePropertyIds.TRACTION_CONTROL_ACTIVE, 0, 0.5F)
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        kpm.startObservingCar()
        viewModelScope.launch {
            PushServiceImpl.events.collect {
                this@MainViewModel.pushUiState =
                    this@MainViewModel.pushUiState.copy(registered = it.registered)
            }
        }
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
}