package com.example.twocentschallenge.viewModels

import android.util.Log
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

    private val _uiState = MutableStateFlow<PostUiState<List<Post>?>>(PostUiState.Loading)
    val uiState: StateFlow<PostUiState<List<Post>?>> = _uiState.asStateFlow()

    init {
        getItems()
    }

    fun getItems() = viewModelScope.launch {

        _uiState.value = PostUiState.Loading
        repository.getPosts(FilterEnum.TopAllTime)
            .fold(
                onSuccess = {
                    _uiState.value = PostUiState.Success(it)
                },
                onFailure = { error ->
                    _uiState.value =
                        PostUiState.Error(error.localizedMessage ?: "Unknown error")
                }
            )
    }
}