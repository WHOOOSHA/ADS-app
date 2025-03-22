package com.example.ads

import android.os.Build
import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.ads.screens.MainScreen
import com.example.ads.screens.CreateUserScreen
import com.example.ads.screens.SearchScreen
import com.example.ads.screens.ProfileScreen
import com.example.ads.ui.theme.ADSTheme

class MainActivity : ComponentActivity() {
    private var isLoggedIn = false

    @RequiresApi(Build.VERSION_CODES.O)
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
                    composable("create_user") { CreateUserScreen(navController) }
                    composable("search") { SearchScreen(navController) }
                    composable(
                        "profile/{login}",
                        arguments = listOf(navArgument("login") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val login = backStackEntry.arguments?.getString("login")
                        ProfileScreen(navController, login)
                    }
                }
            }
        }
    }
}
