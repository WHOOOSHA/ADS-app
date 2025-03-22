package com.example.ads.data

data class Ad(
    val authorName: String,
    val authorGroup: String,
    val authorAvatar: String?,
    val image: String?,
    val title: String,
    val description: String
)