package com.example.twocentschallenge.utils

import com.example.twocentschallenge.Models.Post

sealed class PostUiState {
    object Idle : PostUiState()
    object Loading : PostUiState()
    data class Success<T>(val post: T) : PostUiState()
    data class SuccessPosts(val posts: List<Post>)
    data class Error(val message: String) : PostUiState()
}