package de.quartett.mobile.roadgallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun VehicleDataScreen(viewModel: MainViewModel) {

    val speed by viewModel.speedFlow.collectAsState()
    val gear by viewModel.gearFlow.collectAsState()
    val fuelLevel by viewModel.fuelLevelFlow.collectAsState()
    val rangeRemaining by viewModel.rangeRemainingFlow.collectAsState()
    val engineOilLevel by viewModel.engineOilLevelFlow.collectAsState()
    val outsideTemperature by viewModel.outsideTemperatureFlow.collectAsState()
    val fuelDoorOpen by viewModel.fuelDoorOpenFlow.collectAsState()
    val ignitionState by viewModel.ignitionStateFlow.collectAsState()
    val parkingBrake by viewModel.parkingBrakeFlow.collectAsState()
    val tractionControlActive by viewModel.tractionControlActiveFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 450.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Speed:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            Text("${"%.1f".format(speed*3.6)} km/h", style = TextStyle(fontSize = 18.sp, color = Color.Black))
        }
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Gear:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            Text("$gear (see VehicleGear)", style = TextStyle(fontSize = 18.sp, color = if (gear > 3) Color.Red else Color.Green))
        }
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Fuel Level:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            Text("${"%.1f".format(fuelLevel)} mm", style = TextStyle(fontSize = 18.sp, color = Color.Black))
        }
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Range remaining:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            Text("${"%.1f".format(rangeRemaining)} m", style = TextStyle(fontSize = 18.sp, color = Color.Black))
        }
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Engine Oil Level: ", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            Text("$engineOilLevel (see VehicleOilLevel)", style = TextStyle(fontSize = 18.sp, color = Color.Black))
        }
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Outside Temperature:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            Text("${"%.1f".format(outsideTemperature)}°C", style = TextStyle(fontSize = 18.sp, color = Color.Black))
        }
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Fuel Door Open:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            Text(if (fuelDoorOpen) "Yes" else "No", style = TextStyle(fontSize = 18.sp, color = Color.Black))
        }
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Ignition State:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            Text("$ignitionState (see VehicleIgnitionState)", style = TextStyle(fontSize = 18.sp, color = Color.Black))
        }
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Parking Brake:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            Text(if (parkingBrake) "Engaged" else "Released", style = TextStyle(fontSize = 18.sp, color = Color.Black))
        }
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Traction Control Active:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            Text(if (tractionControlActive) "Yes" else "No", style = TextStyle(fontSize = 18.sp, color = Color.Black))
        }
    }
}
