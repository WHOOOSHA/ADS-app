package com.example.ads.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.ads.data.Ad
import com.example.ads.data.User
import com.example.ads.repositories.UserRepository

@RequiresApi(Build.VERSION_CODES.O)
class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUser(login: String?): User {
        return userRepository.getUser(login)
    }

    fun getAdsForUser(user: User): List<Ad> {
        return userRepository.getAdsForUser(user)
    }
}