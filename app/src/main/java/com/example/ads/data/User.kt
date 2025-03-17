package com.example.ads.data
import java.time.LocalDate

data class User (
    val name: String,
    val dateOfBirth: LocalDate,
    val city: String,
    val image: Int,
    val about: String,
)