package com.example.twocentschallenge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.twocentschallenge.screens.AuthorPostsScreen
import com.example.twocentschallenge.screens.HomeScreen
import com.example.twocentschallenge.screens.PostScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(onPostClick = { id ->
            navController.navigate("postDetail/$id")
            },

            onPosterNetWorthClick = { id ->
                navController.navigate("authorPosts/$id")
            }) }

        composable("postDetail/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            PostScreen(navController, postId,onPostClick = { id ->
                navController.navigate("postDetail/$id")
            },

                onPosterNetWorthClick = { id ->
                    navController.navigate("authorPosts/$id")
                })
        }

        composable("authorPosts/{authorId}") { backStackEntry ->
            val authorId = backStackEntry.arguments?.getString("authorId") ?: ""
            AuthorPostsScreen(navController, authorId, onPostClick = { id ->
                navController.navigate("postDetail/$id")
            })
        }
    }
}