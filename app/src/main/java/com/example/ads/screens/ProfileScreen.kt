package com.example.ads.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ads.R
import com.example.ads.data.User
import com.example.ads.data.Ad
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(navController: NavController, login: String?) {
    val context = LocalContext.current

    // Загружаем данные о пользователе
    val user = remember(login) { loadUserData(context, login) }
    val isRealUser = login != null && context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        .getString("login", null) == login

    // Список объявлений (заглушка)
    val ads = if (!isRealUser) List(10) { index ->
        Ad(
            authorName = user.name,
            authorGroup = "Группа $index",
            authorAvatar = null, // Заглушка
            image = null, // Заглушка
            title = "Объявление $index",
            description = "Описание объявления $index"
        )
    } else emptyList()

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val bitmap = user.image?.let { BitmapFactory.decodeFile(it) }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Avatar",
                            modifier = Modifier.size(80.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_placeholder),
                            contentDescription = "Avatar",
                            modifier = Modifier.size(80.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = user.name, style = MaterialTheme.typography.titleLarge)
                        Text(text = "Дата рождения: ${user.dateOfBirth}")
                        Text(text = "Город: ${user.city}")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "О себе:", style = MaterialTheme.typography.titleMedium)
                Text(text = user.about)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Объявления:", style = MaterialTheme.typography.titleMedium)
            }

            if (ads.isNotEmpty()) {
                items(ads.size) { index ->
                    AdItem(ads[index])
                }
            } else {
                item {
                    Text(
                        text = "Объявлений нет",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 32.dp, start = 16.dp)
                .size(48.dp)
                .background(Color.Black, shape = RoundedCornerShape(12.dp))
                .clickable { navController.popBackStack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color.White)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun loadUserData(context: Context, login: String?): User {
    val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)

    return if (login != null && sharedPreferences.getString("login", null) == login) {
        val name = sharedPreferences.getString("name", "Неизвестный") ?: "Неизвестный"
        val dateOfBirthStr = sharedPreferences.getString("dateOfBirth", "2000-01-01") ?: "2000-01-01"
        val city = sharedPreferences.getString("city", "Не указан") ?: "Не указан"
        val about = sharedPreferences.getString("about", "Нет информации") ?: "Нет информации"
        val imagePath = File(context.filesDir, "user_images/$login.jpg").absolutePath

        User(
            name = name,
            dateOfBirth = LocalDate.parse(dateOfBirthStr, DateTimeFormatter.ISO_DATE),
            city = city,
            image = if (File(imagePath).exists()) imagePath else null,
            about = about
        )
    } else {
        getPlaceholderUser()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getPlaceholderUser(): User {
    return User(
        name = "Иван Иванов",
        dateOfBirth = LocalDate.parse("1990-01-01", DateTimeFormatter.ISO_DATE),
        city = "Москва",
        image = null,
        about = "Информация о пользователе будет здесь."
    )
}
