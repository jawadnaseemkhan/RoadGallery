package com.example.cameratestapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.cameratestapp.ui.theme.CameraTestAppTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                launchUI()
            } else {
                Log.e("Camera", "Permission denied")
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            launchUI()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    private fun launchUI() {
        setContent {
            CameraTestAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DrowsinessDetectionView()
                }
            }
        }
    }
}

@Composable
fun DrowsinessDetectionView() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var statusText by remember { mutableStateOf("Status: Awake") }

    Column {
        Text(
            text = statusText,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge
        )

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                startCamera(previewView, lifecycleOwner, ctx) { drowsy ->
                    statusText = if (drowsy) "Drowsiness Detected!" else "Status: Awake"
                }
                previewView
            }
        )
    }
}

fun startCamera(
    previewView: PreviewView,
    lifecycleOwner: LifecycleOwner,
    context: Context,
    onDrowsinessDetected: (Boolean) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalyzer = ImageAnalysis.Builder().build().also {
            it.setAnalyzer(
                ContextCompat.getMainExecutor(context),
                FaceAnalyzer(context, onDrowsinessDetected)
            )
        }

        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalyzer)
    }, ContextCompat.getMainExecutor(context))
}

class FaceAnalyzer(
    private val context: Context,
    private val onDrowsinessDetected: (Boolean) -> Unit
) : ImageAnalysis.Analyzer {

    private val detector: FaceDetector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
    )

    private var frameCounter = 0
    private val EAR_THRESHOLD = 0.3
    private val CONSEC_FRAMES = 15
    private var hasAlerted = false

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        detector.process(inputImage)
            .addOnSuccessListener { faces ->
                var drowsy = false
                for (face in faces) {
                    val left = face.leftEyeOpenProbability ?: 1.0f
                    val right = face.rightEyeOpenProbability ?: 1.0f

                    if (left < EAR_THRESHOLD && right < EAR_THRESHOLD) {
                        frameCounter++
                        if (frameCounter >= CONSEC_FRAMES) {
                            drowsy = true
                            if (!hasAlerted) {
                                hasAlerted = true
                                MediaPlayer.create(context, R.raw.alert_beep).start()
                            }
                        }
                    } else {
                        frameCounter = 0
                        hasAlerted = false
                    }
                }

                onDrowsinessDetected(drowsy)
                imageProxy.close()
            }
            .addOnFailureListener {
                imageProxy.close()
            }
    }
}
