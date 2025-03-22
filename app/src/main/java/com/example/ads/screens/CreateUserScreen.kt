package com.example.ads.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun CreateUserScreen(navController: NavController) {
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var login by remember { mutableStateOf(TextFieldValue("")) }
    var dateOfBirth by remember { mutableStateOf(TextFieldValue("")) }
    var city by remember { mutableStateOf(TextFieldValue("")) }
    var about by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageUploader(imageUri) { imageUri = it }

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("ФИО") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text("Дата рождения") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Город") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = about,
            onValueChange = { about = it },
            label = { Text("О себе") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Отменить")
            }
            Button(onClick = {
                if (login.text.isNotBlank()) {
                    saveUserData(context, name.text, login.text, dateOfBirth.text, city.text, about.text)
                    imageUri?.let { saveImageToInternalStorage(context, it, login.text) }
                    navController.navigate("profile/${login.text}")
                }
            }) {
                Text("Завершить")
            }
        }
    }
}

@Composable
fun ImageUploader(selectedImageUri: Uri?, onImageSelected: (Uri) -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.LightGray)
            .clickable { launcher.launch("image/*") },
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri != null) {
            val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(context.contentResolver, selectedImageUri)
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Выбранное изображение",
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            Text("Загрузить изображение")
        }
    }
}

fun saveUserData(context: Context, name: String, login: String, dateOfBirth: String, city: String, about: String) {
    val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("name", name)
        putString("login", login)
        putString("dateOfBirth", dateOfBirth)
        putString("city", city)
        putString("about", about)
        apply()
    }
}

fun saveImageToInternalStorage(context: Context, imageUri: Uri, login: String) {
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
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
