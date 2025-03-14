package de.quartett.mobile.roadgallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcomposeexample.ui.theme.RoadGalleryTheme

@Composable
fun MainView(speed: Float, gear: Int) {
	// Loading an image resource from drawable
	val image: Painter = painterResource(id = R.drawable.welcome)
	val LightBlue = Color(0xFF0073CF)

	val gradientColors = listOf(LightBlue, Cyan , LightBlue /*...*/)


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

		Text(text = "Speed: ${"%.1f".format(speed)} km/h",
				style = TextStyle(
						fontSize = 36.sp, color = Color.White))
		Text(text = "Gear: ${gear}",
				style = TextStyle(fontSize = 36.sp, color = if (gear > 3) Color.Red else Color.Green))

		// Displaying the Image
		Image(painter = image,
				contentDescription = "Sample Image",
				modifier = Modifier.padding(bottom = 16.dp)
		)
	}
}

@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
	RoadGalleryTheme {
		MainView(35f, 4)
	}
}