package com.example.twocentschallenge.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.repository.PostRepository
import com.example.twocentschallenge.utils.PostUiState
import com.example.twocentschallenge.enums.FilterEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>?>(null)
    val posts: StateFlow<List<Post>?> get() = _posts

    private val _uiState = MutableStateFlow<PostUiState>(PostUiState.Idle)
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()


    init {
        getItems()
    }

    fun getItems() = viewModelScope.launch {

        _uiState.value = PostUiState.Loading
        repository.getPosts(FilterEnum.NewToday)
            .fold(
                onSuccess = { list ->
                    _uiState.value = PostUiState.Success(list)
                },
                onFailure = { error ->
                    _uiState.value =
                        PostUiState.Error(error.localizedMessage ?: "Unknown error")
                }
            )
    }

    fun getPoll(postId: String) = viewModelScope.launch {
        var result = repository.getPoll(postId)
    }
}