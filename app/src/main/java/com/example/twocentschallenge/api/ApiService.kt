package com.example.twocentschallenge.api

import com.example.twocentschallenge.Models.AuthorPosts
import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.Models.PostMeta
import com.example.twocentschallenge.Models.PostWrapper
import com.example.twocentschallenge.Models.ResultItem
import com.example.twocentschallenge.Models.ResultWrapper
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    suspend fun getPostById(id: String): Post

    @POST("prod")
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    suspend fun getItems(
        @Body request: JsonRpcRequest
    ): JsonRpcResponse<PostWrapper>

    @POST("prod")
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    suspend fun getItemsPerAuthor(
        @Body req: JsonRpcRequest
    ): JsonRpcResponse<AuthorPosts>

    @POST("jsonrpc")
    suspend fun getUserInfo(
        @Body request: JsonRpcRequest
    ): JsonRpcResponse<Post>

    @POST("prod")
    suspend fun getPoll(
        @Body request: JsonRpcRequest
    ): JsonRpcResponse<ResultWrapper>
}

@JsonClass(generateAdapter = true)
data class JsonRpcRequest(
    val jsonrpc: String = "2.0",
    val id: String = "anon",
    val method: String,
    val params: Map<String, Any>
)

data class JsonRpcResponse<T>(
    val jsonrpc: String,
    val id: String,
    val result: T? = null,
    val error: JsonRpcError? = null
)

data class JsonRpcError(
    val code: Int,
    val message: String
)