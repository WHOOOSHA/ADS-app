package com.example.ads.repositories

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.ads.data.Ad
import com.example.ads.data.User
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class UserRepository(private val context: Context) {

    fun getUser(login: String?): User {
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
            /* Тут должен быть сетевой запрос*/
            getPlaceholderUser()
        }
    }

    private fun getPlaceholderUser(): User {
        return User(
            name = "Иван Иванов",
            dateOfBirth = LocalDate.parse("1990-01-01", DateTimeFormatter.ISO_DATE),
            city = "Москва",
            image = null,
            about = "Информация о пользователе будет здесь."
        )
    }

    fun getAdsForUser(user: User): List<Ad> {
        return if (user.name == "Иван Иванов") {
            List(5) { index ->
                Ad(
                    authorName = user.name,
                    authorGroup = "Группа $index",
                    authorAvatar = null, // Заглушка
                    image = null, // Заглушка
                    title = "Объявление $index",
                    description = "Описание объявления $index"
                )
            }
        } else {
            /* Тут должен быть сетевой запрос*/
            emptyList()
        }
    }
}