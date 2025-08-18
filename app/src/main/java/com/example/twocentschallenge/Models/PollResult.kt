package com.example.twocentschallenge.Models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class Poll(
    val results: Map<String, PollValues>
)

@JsonClass(generateAdapter = true)
data class PollValues(
    @Json(name = "average_balance")
    val average_balance: BigDecimal,
    @Json(name = "votes")
    val votes: Int
)

data class PollOptions(
    val question: String,
    val votes: Int,
    val averageBalance: BigDecimal
)