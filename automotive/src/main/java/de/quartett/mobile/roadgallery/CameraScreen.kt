package de.quartett.mobile.roadgallery

import android.content.Context
import android.hardware.camera2.CameraManager
import android.util.Log
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraScreen(viewModel: CameraViewModel) {

    Button(onClick = {viewModel.changeLens()}) {
        Text("Change Cameras")
    }

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current
    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }

    val cameraIDs = getCameraIds(context)
    for (cameraID in cameraIDs) {
        Log.i("cameraIds", cameraID)
    }

    if (cameraIDs.isEmpty()){
        Log.e("Camera", "No Camera")
    }

    LaunchedEffect(viewModel.cameraSelector) {
        val cameraProvider = getCameraProvider(context)
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, viewModel.cameraSelector, preview)
        preview.surfaceProvider = previewView.surfaceProvider
    }
    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
}


suspend fun getCameraProvider(context: Context): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(context).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(context))
        }
    }

fun getCameraIds(context: Context): List<String> {
    val cameraIds = mutableListOf<String>()

    // Get the CameraManager system service
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    try {
        // Get a list of all camera IDs
        cameraIds.addAll(cameraManager.cameraIdList)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return cameraIds
}