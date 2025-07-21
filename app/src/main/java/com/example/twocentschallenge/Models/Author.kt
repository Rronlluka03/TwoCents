package com.example.twocentschallenge.Models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class Author (
    @Json(name = "bio")
    val bio: String,
    @Json(name = "age")
    val age: Int,
    @Json(name = "gender")
    val gender: String,
    @Json(name = "balance")
    val balance: BigDecimal,
    @Json(name = "arena")
    val arena: String,
    @Json(name = "subscription_type")
    val subscriptionType: String
)