package com.example.twocentschallenge.Models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostMeta (
    @Json(name = "poll")
    val poll: List<String>?,
    @Json(name = "post_type")
    val postType: Int?
)