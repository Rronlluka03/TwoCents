package com.example.twocentschallenge.utils

import androidx.compose.runtime.State
import com.example.twocentschallenge.Models.PollOptions
import com.example.twocentschallenge.Models.Post

sealed interface PostUiState<out T> {
    data object Idle : PostUiState<Nothing>
    data object Loading : PostUiState<Nothing>
    data class Success<T>(val post: T) : PostUiState<T>
    data class Error(val message: String) : PostUiState<Nothing>

}