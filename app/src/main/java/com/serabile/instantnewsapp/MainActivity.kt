package com.serabile.instantnewsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.serabile.designsystem.theme.InstantNewsTheme
import com.serabile.instantnewsapp.navigation.NewsNavHost
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point of the application.
 * Annotated with @AndroidEntryPoint to enable Hilt dependency injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstantNewsTheme {
                val navController = rememberNavController()
                NewsNavHost(navController = navController)
            }
        }
    }
}
