package com.example.twocentschallenge.Models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthorPosts(
    @Json(name = "user")
    val userInfo: Author,
    @Json(name = "recentPosts")
    val recentPosts: List<Post>
)