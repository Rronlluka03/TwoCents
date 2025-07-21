package com.example.twocentschallenge.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.enums.FilterEnum
import com.example.twocentschallenge.repository.PostRepository
import com.example.twocentschallenge.utils.PostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorVM @Inject constructor(
    private val repository: PostRepository,
) : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>?>(null)
    val posts: StateFlow<List<Post>?> get() = _posts

    private val _uiState = MutableStateFlow<PostUiState>(PostUiState.Idle)
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    fun getPostById(id: String) = viewModelScope.launch {
        _uiState.value = PostUiState.Loading
        Log.d("rronirroni", "$id")
        _uiState.value = PostUiState.Loading

        repository.getPostsPerAuthor(id)
            .fold(
                onSuccess = { list ->
                    list?.let {
                        _uiState.value = PostUiState.Success(it)
                    } ?: run {
                        _uiState.value = PostUiState.Error("Author Not Found")
                    }
                },
                onFailure = { error ->
                    _uiState.value =
                        PostUiState.Error(error.localizedMessage ?: "Unknown error")
                }
            )
    }

}