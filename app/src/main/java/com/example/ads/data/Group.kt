package com.example.ads.data

data class Group(
    val name: String,
    val description: String,
    val city: String,
    val categories: List<String>,
    val image: String?,
    val moderators: List<String>
)