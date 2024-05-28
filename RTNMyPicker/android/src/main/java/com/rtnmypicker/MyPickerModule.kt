package com.rtnmypicker

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream

class MyPickerModule(val context: ReactApplicationContext?) : NativeMyPickerSpec(context), ActivityEventListener {

    private var mat: Mat? = null
    private val REQUEST_GALLERY_IMAGE_ID = 101
    private var resultPromise: Promise? = null

    init {
        context?.addActivityEventListener(this)
    }

    override fun getName(): String {
        return NAME
    }

    override fun pickImage(promise: Promise?) {
        resultPromise = promise
        val intent = Intent(Intent.ACTION_PICK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/*"
        context?.startActivityForResult(intent, REQUEST_GALLERY_IMAGE_ID, null)
    }

    companion object {
        const val NAME = "RTNMyPicker"
    }

    override fun onActivityResult(p0: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_GALLERY_IMAGE_ID && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let { mediaUri ->
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = context?.contentResolver?.let {
                        ImageDecoder.createSource(it, mediaUri)
                    }
                    source?.let { ImageDecoder.decodeBitmap(it) }
                } else {
                    MediaStore.Images.Media.getBitmap(
                        context?.contentResolver, mediaUri
                    )
                }?.copy(Bitmap.Config.ARGB_8888, true)

                if (mat == null) {
                    mat = Mat()
                }

                Utils.bitmapToMat(bitmap, mat)
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY)
                Utils.matToBitmap(mat, bitmap)

                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                resultPromise?.resolve("data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT))
            }
        } else {
            Toast.makeText(context?.currentActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNewIntent(p0: Intent?) {}
}
