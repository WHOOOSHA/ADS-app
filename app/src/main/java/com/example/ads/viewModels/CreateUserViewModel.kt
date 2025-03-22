//package com.example.ads.viewModels
//
//import android.app.Application
//import android.content.ContentValues
//import android.content.Context
//import android.content.SharedPreferences
//import android.graphics.Bitmap
//import android.net.Uri
//import android.provider.MediaStore
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.ads.data.User
//import kotlinx.coroutines.launch
//import java.io.OutputStream
//
//class CreateUserViewModel(application: Application) : AndroidViewModel(application) {
//    private val context = application.applicationContext
//    private val sharedPreferences: SharedPreferences =
//        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//
//    var user = User()
//        private set
//
//    fun updateFullName(fullName: String) {
//        user = user.copy(name = fullName)
//    }
//
//    fun updateLogin(login: String) {
//        user = user.copy(login = login)
//    }
//
//    fun updateBirthDate(birthDate: String) {
//        user = user.copy(dateOfBirth = birthDate)
//    }
//
//    fun updateCity(city: String) {
//        user = user.copy(city = city)
//    }
//
//    fun updateAboutMe(aboutMe: String) {
//        user = user.copy(about = aboutMe)
//    }
//
//    fun updateImageUri(uri: Uri?) {
//        user = user.copy(imageUri = uri)
//    }
//
//    fun saveUserData() {
//        viewModelScope.launch {
//            with(sharedPreferences.edit()) {
//                putString("full_name", user.name)
//                putString("login", user.login)
//                putString("birth_date", user.dateOfBirth)
//                putString("city", user.city)
//                putString("about_me", user.about)
//
//                user.imageUri?.let { uri ->
//                    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
//                    val savedUri = saveImageToInternalStorage(bitmap)
//                    putString("image_uri", savedUri.toString())
//                }
//
//                apply()
//            }
//        }
//    }
//
//    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
//        val filename = "user_profile_${System.currentTimeMillis()}.jpg"
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/UserImages")
//        }
//
//        val uri: Uri? =
//            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        uri?.let {
//            val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
//            outputStream?.use { stream -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream) }
//        }
//        return uri
//    }
//}
