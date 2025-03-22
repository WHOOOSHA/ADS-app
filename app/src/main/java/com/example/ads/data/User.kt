package com.example.ads.data
import java.time.LocalDate

data class User(
    val name: String = "",
    val login: String = "",
    val dateOfBirth: LocalDate? = null,
    val city: String = "",
    val image: String? = null,
    val about: String = ""
)