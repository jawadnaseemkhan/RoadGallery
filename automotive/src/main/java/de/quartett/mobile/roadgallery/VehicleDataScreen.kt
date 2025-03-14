package de.quartett.mobile.roadgallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VehicleDataScreen(viewModel: MainViewModel) {

    val speed by viewModel.speedFlow.collectAsState()
    val gear by viewModel.gearFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            Text(
                text = "Vehicle Data",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Speed: ${"%.1f".format(speed)} km/h",
                style = TextStyle(
                    fontSize = 36.sp, color = Color.Black
                )
            )
            Text(
                text = "Gear: ${gear}",
                style = TextStyle(
                    fontSize = 36.sp,
                    color = if (gear > 3) Color.Red else Color.Green
                )
            )

    }
}