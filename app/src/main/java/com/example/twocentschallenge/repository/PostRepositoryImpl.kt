package com.example.twocentschallenge.repository

import android.util.Log
import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.api.ApiService
import com.example.twocentschallenge.api.JsonRpcRequest
import com.example.twocentschallenge.enums.FilterEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PostRepository {

    override suspend fun getPosts(filter: FilterEnum, secretKey: String?): Result<List<Post>?> = withContext(Dispatchers.IO) {
        val params = buildMap<String, Any> {
            put("filter", filter.value)
            secretKey?.let { put("secret_key", it) }
        }

        val req = JsonRpcRequest(
            method = "/v1/posts/arena",
            params = params
        )

        runCatching {
            val response = apiService.getItems(req)
            response.result?.posts
        }
    }

    override suspend fun getPostByID(id: String): Post {
        return apiService.getPostById(id)
    }

    override suspend fun getPostsPerAuthor(authorId: String): Result<List<Post>?> = withContext(Dispatchers.IO) {
        val params = mapOf("user_uuid" to authorId)
        val request = JsonRpcRequest(
            id = "anon",
            method = "/v1/users/get",
            params = params
        )

        runCatching {
            val response = apiService.getItemsPerAuthor(request)
            response.result?.recentPosts
        }

    }

    override suspend fun getPoll(id: String): Result<Post?> = withContext(Dispatchers.IO) {
        val params = mapOf("user_uuid" to id)

        val request = JsonRpcRequest(
            id = "anon",
            method = "/v1/polls/get",
            params = params
        )

        runCatching {
            val response = apiService.getPoll(request)
            Log.d("rronirroni", "$response")
            response.result
        }
    }


}
