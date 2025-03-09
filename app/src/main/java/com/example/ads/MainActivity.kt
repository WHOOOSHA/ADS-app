package com.example.ads

import android.os.Bundle
import com.example.ads.ui.theme.ADSTheme
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ads.screens.MainScreen
import com.example.ads.screens.CreateUserScreen
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ADSTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main_screen") {
                    composable("main_screen") {
                        MainScreen(navController, isLoggedIn)
                    }
                    composable("create_user") { CreateUserScreen() }
                }
            }
        }
    }
}