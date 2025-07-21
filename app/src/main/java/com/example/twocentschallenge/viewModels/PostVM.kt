package com.example.twocentschallenge.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twocentschallenge.Models.ResultWrapper
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
class PostVM @Inject constructor(
    private val repository: PostRepository,
) : ViewModel() {

    private val _poll = MutableStateFlow<PostUiState>(PostUiState.Idle)
    val poll: StateFlow<PostUiState?> get() = _poll

    private val _uiState = MutableStateFlow<PostUiState>(PostUiState.Idle)
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    fun getPostById(id: String) {
        viewModelScope.launch {
            _uiState.value = PostUiState.Loading

            repository.getPosts(FilterEnum.NewToday)
                .fold(
                    onSuccess = { list ->
                        val post = list?.find { it.uuid == id }
                        post?.let {
                            _uiState.value = PostUiState.Success(it)
                        } ?: run {
                            _uiState.value = PostUiState.Error("Post not found")
                        }
                    },
                    onFailure = { error ->
                        _uiState.value =
                            PostUiState.Error(error.localizedMessage ?: "Unknown error")
                    }
                )
            getPoll(postId = id)
        }
    }

    fun getPoll(postId: String) = viewModelScope.launch {
        viewModelScope.launch {
            _poll.value = PostUiState.Loading

            repository.getPoll(postId)
                .fold(
                    onSuccess = { poll ->
                            _poll.value = PostUiState.Success(poll)
                    },
                    onFailure = { error ->
                        _poll.value =
                            PostUiState.Error(error.localizedMessage ?: "Unknown error")
                    }
                )
        }
    }

}