package de.quartett.mobile.roadgallery

import androidx.camera.core.CameraSelector
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {
    var camera: Int = CameraSelector.LENS_FACING_FRONT
    var cameraSelector = CameraSelector.Builder().requireLensFacing(camera).build()

    fun changeLens(){
        camera = if (camera == CameraSelector.LENS_FACING_FRONT){
            CameraSelector.LENS_FACING_BACK
        }else{
            CameraSelector.LENS_FACING_FRONT
        }
        cameraSelector = CameraSelector.Builder().requireLensFacing(camera).build()
    }
}
