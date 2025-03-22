package com.example.ads.screens

import android.net.Uri
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ads.viewModels.CreateUserViewModel

@Composable
fun CreateUserScreen(navController: NavController, viewModel: CreateUserViewModel = viewModel()) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageUploader(viewModel.imageUri.value) { viewModel.imageUri.value = it }

        TextField(
            value = viewModel.name.value,
            onValueChange = { viewModel.name.value = it },
            label = { Text("ФИО") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = viewModel.login.value,
            onValueChange = { viewModel.login.value = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = viewModel.dateOfBirth.value,
            onValueChange = { viewModel.dateOfBirth.value = it },
            label = { Text("Дата рождения") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = viewModel.city.value,
            onValueChange = { viewModel.city.value = it },
            label = { Text("Город") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = viewModel.about.value,
            onValueChange = { viewModel.about.value = it },
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
                if (viewModel.login.value.isNotBlank()) {
                    viewModel.saveUserData()
                    navController.navigate("profile/${viewModel.login.value}")
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
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, selectedImageUri)
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Выбранное изображение",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text("Загрузить изображение")
        }
    }
}
