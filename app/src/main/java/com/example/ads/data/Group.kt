package com.example.ads.data

data class Group(
    val name: String,
    val description: String,
    val city: String,
    val categories: List<String>,
    val image: Int,
    val moderators: List<String>
)