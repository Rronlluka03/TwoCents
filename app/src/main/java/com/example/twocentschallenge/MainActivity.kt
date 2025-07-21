package com.example.twocentschallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.twocentschallenge.navigation.AppNavigation
import com.example.twocentschallenge.ui.theme.TwoCentsChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TwoCentsChallengeTheme {
                AppNavigation()
            }
        }
    }
}