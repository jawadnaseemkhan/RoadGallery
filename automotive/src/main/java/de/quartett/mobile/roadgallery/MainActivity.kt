package de.quartett.mobile.roadgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcomposeexample.ui.theme.RoadGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoadGalleryTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var counter1 by remember { mutableStateOf(0) }
    var counter2 by remember { mutableStateOf(0) }
    var rotation1 by remember { mutableStateOf(0f) }
    var rotation2 by remember { mutableStateOf(0f) }
    val image: Painter = painterResource(id = R.drawable.cookie) // Use a higher resolution image
    val BMWBlue = Color(0xFF0066B1)

    val animatedRotation1 by animateFloatAsState(
        targetValue = rotation1,
        animationSpec = keyframes {
            durationMillis = 300
            0f at 0 with androidx.compose.animation.core.LinearEasing
            10f at 150 with androidx.compose.animation.core.LinearEasing
            0f at 300 with androidx.compose.animation.core.LinearEasing
        },
        finishedListener = {
            rotation1 = 0f // Reset rotation after animation
        }
    )

    val animatedRotation2 by animateFloatAsState(
        targetValue = rotation2,
        animationSpec = keyframes {
            durationMillis = 300
            0f at 0 with androidx.compose.animation.core.LinearEasing
            10f at 150 with androidx.compose.animation.core.LinearEasing
            0f at 300 with androidx.compose.animation.core.LinearEasing
        },
        finishedListener = {
            rotation2 = 0f // Reset rotation after animation
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentHeight()
        ) {
            Text(
                text = "Welcome from BMW! (with cookies)",
                style = TextStyle(
                    fontSize = 48.sp,
                    color = BMWBlue,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Spacer(modifier = Modifier.height(32.dp)) // Add space between text and image

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = image,
                        contentDescription = "Cookie Image",
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .rotate(animatedRotation1)
                            .size(250.dp) // Adjust size as needed
                    )
                    Text(
                        text = counter1.toString(),
                        style = TextStyle(
                            fontSize = 48.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Button(
                        onClick = {
                            counter1++
                            rotation1 = 10f
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BMWBlue),
                        modifier = Modifier.size(150.dp, 60.dp)
                    ) {
                        Text(text = "Player 1")
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = image,
                        contentDescription = "Cookie Image",
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .rotate(animatedRotation2)
                            .size(250.dp) // Adjust size as needed
                    )
                    Text(
                        text = counter2.toString(),
                        style = TextStyle(
                            fontSize = 48.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Button(
                        onClick = {
                            counter2++
                            rotation2 = 10f
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BMWBlue),
                        modifier = Modifier.size(150.dp, 60.dp)
                    ) {
                        Text(text = "Player 2")
                    }
                }
            }
        }
    }
}

@Composable
fun DefaultPreview() {
    RoadGalleryTheme {
        MainScreen()
    }
}