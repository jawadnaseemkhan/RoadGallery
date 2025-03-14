package de.quartett.mobile.roadgallery

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetpackcomposeexample.ui.theme.RoadGalleryTheme

@Composable
fun MainView(speed: Float, gear: Int, fuelLevel: Float, rangeRemaining: Float) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Vehicle Data", "Cookies", "Covesa Push", "WebView")

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> VehicleDataScreen(speed, gear, fuelLevel, rangeRemaining)
            1 -> CookieScreen()
            2 -> CovesaPushScreen()
            3 -> WebViewScreen("https://www.google.com")
        }
    }
}