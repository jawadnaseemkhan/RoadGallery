package de.quartett.mobile.roadgallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VehicleDataScreen(speed: Float, gear: Int, fuelLevel: Float, rangeRemaining: Float) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 450.dp),
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
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Speed:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
                Text("${"%.1f".format(speed)} km/h", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            }
            HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Gear:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
                Text("$gear", style = TextStyle(fontSize = 18.sp, color = if (gear > 3) Color.Red else Color.Green))
            }
            HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Fuel Level:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
                Text("${"%.1f".format(fuelLevel)}%", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            }
            HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("RPM:", style = TextStyle(fontSize = 18.sp, color = Color.Black))
                Text("${"%.1f".format(rangeRemaining)} rpm", style = TextStyle(fontSize = 18.sp, color = Color.Black))
            }
        }
    }
}