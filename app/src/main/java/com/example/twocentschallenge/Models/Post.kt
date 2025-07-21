package com.example.twocentschallenge.Models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
data class Post (
    @Json(name = "uuid")
    val uuid: String,
    @Json(name = "created_at")
    val createdAt: Instant,
    @Json(name = "updated_at")
    val updatedAt: Instant,
    @Json(name = "author_uuid")
    val authorUuid: String,
    @Json(name = "upvote_count")
    val upvoteCount: Int,
    @Json(name = "comment_count")
    val commentCount: Int,
    @Json(name = "view_count")
    val viewCount: Int,
    @Json(name = "report_count")
    val reportCount: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "text")
    val text: String,
    @Json(name = "topic")
    val topic: String,
    @Json(name = "author_meta")
    val authorMetaDto: Author,
    @Json(name = "post_meta")
    val postMeta: PostMeta?,
    @Json(name = "post_type")
    val postType: Int
)