package com.example.twocentschallenge.Models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResultWrapper(
    val results: Map<String, ResultItem>
)

@JsonClass(generateAdapter = true)
data class ResultItem(
    @Json(name = "average_balance")
    val average_balance: Double,
    @Json(name = "votes")
    val votes: Int
)