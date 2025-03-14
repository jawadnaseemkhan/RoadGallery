package de.quartett.mobile.roadgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcomposeexample.ui.theme.RoadGalleryTheme
import global.covesa.sdk.client.push.ActionEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoadGalleryTheme {
                val viewModel: MainViewModel = viewModel {
                    MainViewModel(
                        pushUiState = PushUiState(this@MainActivity),
                    )
                }
                // A surface container using the 'background' color from the theme
                MainScreen(viewModel)
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
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    // Loading an image resource from drawable
    val image: Painter = painterResource(id = R.drawable.welcome)
    val LightBlue = Color(0xFF0073CF)

    val gradientColors = listOf(LightBlue, Cyan, LightBlue /*...*/)

    val pushUiState = viewModel.pushUiState

    // Column to arrange the Text and Image vertically
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        Text(
            text = "Welcome from Quartett mobile!",
            style = TextStyle(
                fontSize = 48.sp,
                brush = Brush.linearGradient(
                    colors = gradientColors
                )
            ),
            modifier = Modifier.padding(bottom = 16.dp),
        )

        if (pushUiState.registered) {
            Text(
                text = "Registered to push service ${pushUiState.pushDistributor}.",
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
