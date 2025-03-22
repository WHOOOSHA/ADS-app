package com.example.ads.repositories

import com.example.ads.data.Ad
import com.example.ads.data.Group
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepository {
    private val categories = listOf("Авто", "Недвижимость", "Электроника", "Услуги", "Работа", "Одежда", "Животные", "Хобби")

    private val allAds = List(100) { i ->
        val category = categories[i % categories.size]
        Ad(
            authorName = "Имя $i",
            authorGroup = category,
            authorAvatar = null, // Заглушка для аватара
            image = null, // Заглушка для картинки объявления
            title = "$category - Объявление $i",
            description = "Описание объявления $i в категории $category"
        )
    }

    private val allGroups = List(100) { i ->
        val category = categories[i % categories.size]
        Group(
            name = "Группа $i - $category",
            description = "Описание группы $i в категории $category",
            city = "Город",
            categories = listOf(category),
            image = null, // Заглушка для изображения группы
            moderators = listOf("Модератор")
        )
    }

    // Для имитации нагрузки используем delay(1000)
    fun getInitialResults(): Flow<Pair<List<Ad>, List<Group>>> = flow {
        delay(1000)
        emit(allAds.take(5) to allGroups.take(5))
    }

    fun search(query: String): Flow<Pair<List<Ad>, List<Group>>> = flow {
        delay(1000)
        val filteredAds = allAds.filter { it.title.contains(query, ignoreCase = true) }
        val filteredGroups = allGroups.filter { it.name.contains(query, ignoreCase = true) }
        emit(filteredAds.take(5) to filteredGroups.take(5))
    }

    fun loadMoreAds(query: String, offset: Int): Flow<List<Ad>> = flow {
        delay(1000)
        val filteredAds = allAds.filter { it.title.contains(query, ignoreCase = true) }
        emit(filteredAds.drop(offset).take(10))
    }

    fun loadMoreGroups(query: String, offset: Int): Flow<List<Group>> = flow {
        delay(1000)
        val filteredGroups = allGroups.filter { it.name.contains(query, ignoreCase = true) }
        emit(filteredGroups.drop(offset).take(10))
    }
}