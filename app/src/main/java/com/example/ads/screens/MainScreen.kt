package com.example.ads.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ads.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavController
import com.example.ads.data.Ad
import java.io.File

@Composable
fun MainScreen(navController: NavController, isLoggedIn: Boolean) {
    val adsList = listOf(
        Ad(
            authorName = "Автор 1",
            authorGroup = "Группа A",
            authorAvatar = null, // Файл не указан, будет использована заглушка
            image = null, // Файл не указан, будет использована заглушка
            title = "Объявление 1",
            description = "Описание объявления 1"
        ),
        Ad(
            authorName = "Автор 2",
            authorGroup = "Группа B",
            authorAvatar = null,
            image = null,
            title = "Объявление 2",
            description = "Описание объявления 2"
        ),
        Ad(
            authorName = "Автор 3",
            authorGroup = "Группа C",
            authorAvatar = null,
            image = null,
            title = "Объявление 3",
            description = "Описание объявления 3"
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController, isLoggedIn) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                AdList(ads = adsList)
            }

            NewADS(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                navController, isLoggedIn
            )
        }
    }
}

@Composable
fun AdList(ads: List<Ad>) {
    LazyColumn {
        items(ads) { ad ->
            AdItem(ad)
        }
    }
}

@Composable
fun AdItem(ad: Ad) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val avatarBitmap = ad.authorAvatar?.takeIf { File(it).exists() }?.let { path ->
                    BitmapFactory.decodeFile(path)?.asImageBitmap()
                }

                if (avatarBitmap != null) {
                    Image(
                        bitmap = avatarBitmap,
                        contentDescription = "Аватар автора",
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Gray)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.avatar_placeholder),
                        contentDescription = "Аватар автора",
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Gray)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = ad.authorName, style = MaterialTheme.typography.titleMedium)
                    Text(text = ad.authorGroup, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            val imageBitmap = ad.image?.takeIf { File(it).exists() }?.let { path ->
                BitmapFactory.decodeFile(path)?.asImageBitmap()
            }

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Изображение объявления",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.LightGray)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ad_image_placeholder),
                    contentDescription = "Изображение объявления",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.LightGray)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = ad.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = ad.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, isLoggedIn: Boolean) {
    BottomAppBar {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { /* Действие для кнопки "Главная" */ }) {
                Icon(imageVector = Icons.Filled.Home, contentDescription = "Главная")
            }
            Text(text = "Главная")
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { navController.navigate("search") }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Найти")
            }
            Text(text = "Найти")
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = {
                if (!isLoggedIn) {
                    navController.navigate("create_user")
                } else {
                    // Действие при нажатии, если пользователь вошел
                }
            }) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "Мой профиль")
            }
            Text(text = "Мой профиль")
        }
    }
}

@Composable
fun NewADS(modifier: Modifier = Modifier, navController: NavController, isLoggedIn: Boolean) {
    Button(onClick = {
        if (!isLoggedIn) {
            navController.navigate("create_user")
        } else {
            // Действие при нажатии, если пользователь вошел
        }
    }, modifier = modifier) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Добавить")
    }
}

