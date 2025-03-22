package com.example.ads.viewModels

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CreateUserViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)

    var name = mutableStateOf("")
    var login = mutableStateOf("")
    var dateOfBirth = mutableStateOf("")
    var city = mutableStateOf("")
    var about = mutableStateOf("")
    var imageUri = mutableStateOf<Uri?>(null)

    fun saveUserData() {
        viewModelScope.launch {
            with(sharedPreferences.edit()) {
                putString("name", name.value)
                putString("login", login.value)
                putString("dateOfBirth", dateOfBirth.value)
                putString("city", city.value)
                putString("about", about.value)
                apply()
            }
            imageUri.value?.let { saveImageToInternalStorage(it, login.value) }
        }
    }

    private fun saveImageToInternalStorage(imageUri: Uri, login: String) {
        val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }

        val directory = File(context.filesDir, "user_images")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "$login.jpg")
        try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
