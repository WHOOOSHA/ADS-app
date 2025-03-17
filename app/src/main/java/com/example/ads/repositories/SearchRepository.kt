package com.example.ads.repositories


import com.example.ads.data.Group
import com.example.ads.data.Ad
import com.example.ads.R

class SearchRepository {
    private val categories = listOf("Авто", "Недвижимость", "Электроника", "Услуги", "Работа", "Одежда", "Животные", "Хобби")

    private val allAds = List(100) { i ->
        val category = categories[i % categories.size]
        Ad(
            authorName = "Имя $i",
            authorGroup = "$category",
            authorAvatar = R.drawable.avatar_placeholder,
            image = R.drawable.ad_image_placeholder,
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
            image = R.drawable.avatar_placeholder,
            moderators = listOf("Модератор")
        )
    }

    fun getInitialResults(): Pair<List<Ad>, List<Group>> {
        return allAds.take(5) to allGroups.take(5)
    }

    fun search(query: String): Pair<List<Ad>, List<Group>> {
        val filteredAds = allAds.filter { it.title.contains(query, ignoreCase = true) }
        val filteredGroups = allGroups.filter { it.name.contains(query, ignoreCase = true) }
        return filteredAds to filteredGroups
    }

    fun loadMoreAds(currentSize: Int): List<Ad> {
        return allAds.drop(currentSize).take(10)
    }

    fun loadMoreGroups(currentSize: Int): List<Group> {
        return allGroups.drop(currentSize).take(10)
    }
}