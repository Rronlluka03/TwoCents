package com.example.twocentschallenge.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twocentschallenge.Models.Comment
import com.example.twocentschallenge.Models.CommentNode
import com.example.twocentschallenge.Models.PollOptions
import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.repository.PostRepository
import com.example.twocentschallenge.utils.PostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostVM @Inject constructor(
    private val repository: PostRepository,
) : ViewModel() {

    private val _poll = MutableStateFlow<PostUiState<List<PollOptions>?>>(PostUiState.Idle)
    val poll: StateFlow<PostUiState<List<PollOptions>?>?> get() = _poll

    private val _uiState = MutableStateFlow<PostUiState<Post?>>(PostUiState.Loading)
    val uiState: StateFlow<PostUiState<Post?>> = _uiState.asStateFlow()

    private val _commentState = MutableStateFlow<PostUiState<List<CommentNode>?>>(PostUiState.Loading)
    val commentsState: StateFlow<PostUiState<List<CommentNode>?>> = _commentState

    fun getPostById(id: String) {
        viewModelScope.launch {
            _uiState.value = PostUiState.Loading

            repository.getPostByID(id)
                .fold(
                    onSuccess = { post ->
                        _uiState.value = PostUiState.Success(post)
                        getPoll(postId = id, post?.postMeta?.poll)

                        if(post!!.commentCount >= 0) getComments(id)
                    },
                    onFailure = { error ->
                        _uiState.value =
                            PostUiState.Error(error.localizedMessage ?: "Unknown error")
                    }
                )

        }
    }

    fun getComments(id: String) {
        viewModelScope.launch {
            _commentState.value = PostUiState.Loading

            repository.getComments(id)
                .fold(
                    onSuccess = { comments ->
                        var commentTree = comments?.let { it -> buildCommentTree(it, id) }
                        _commentState.value = PostUiState.Success(commentTree)
                    },
                    onFailure = { error ->
                        _commentState.value = PostUiState.Error( error.localizedMessage ?: "Unknown Error")
                    }
                )
        }
    }

    fun getPoll(postId: String, questions: List<String>?) = viewModelScope.launch {
        viewModelScope.launch {
            _poll.value = PostUiState.Loading

            repository.getPoll(postId)
                .fold(
                    onSuccess = { poll ->
                        val options = questions?.mapIndexedNotNull { index, question ->
                            poll?.results?.get(index.toString())?.let {
                                PollOptions(question, it.votes, it.average_balance)
                            }
                        }
                        _poll.value = PostUiState.Success(options)
                    },
                    onFailure = { error ->
                        _poll.value = PostUiState.Error(error.localizedMessage ?: "Unknown error")
                    }
                )
        }
    }

    fun buildCommentTree(
        comments: List<Comment>,
        parentUuid: String?,
        depth: Int = 0
    ): List<CommentNode> {
        return comments
            .filter { it.replyParentUuid == parentUuid }
            .map { comment ->
                CommentNode(
                    comment = comment,
                    depth = depth,
                    children = buildCommentTree(comments, comment.uuid, depth + 1)
                )
            }
    }

}