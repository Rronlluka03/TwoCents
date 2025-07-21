package com.example.twocentschallenge.Models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostWrapper(
    @Json(name = "posts")
    val posts: List<Post>
)