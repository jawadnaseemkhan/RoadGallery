package de.quartett.mobile.roadgallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp

@Composable
fun CovesaPushScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (viewModel.pushUiState.registered) {
            Text(
                text = "Registered to push service ${viewModel.pushUiState.pushDistributor}.",
                modifier = Modifier.padding(top = 16.dp)
            )
            Button(
                modifier = Modifier.padding(top = 32.dp),
                onClick = {
                    viewModel.unregisterPushService()
                }
            ) {
                Text(text = "Unregister")
            }
            Button(
                modifier = Modifier.padding(top = 32.dp),
                onClick = {
                    viewModel.sendPushNotification()
                }
            ) {
                Text(text = "Notify")
            }
        } else {
            Text(
                text = "Not registered to push service.",
                modifier = Modifier.padding(top = 16.dp)
            )
            Button(
                modifier = Modifier.padding(top = 32.dp),
                onClick = {
                    viewModel.registerPushService()
                }
            ) {
                Text(text = "Register")


            }
        }
    }
}