package com.example.twocentschallenge.Models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@JsonClass(generateAdapter = true)
data class CommentsResponse(
    @Json(name = "comments")
    val comments: List<Comment>
)


data class Comment(
    @Json(name = "uuid")
    val uuid: String,

    @Json(name = "created_at")
    val createdAt: Instant,

    @Json(name = "updated_at")
    val updatedAt: Instant,

    @Json(name = "post_uuid")
    val postUuid: String,

    @Json(name = "reply_parent_uuid")
    val replyParentUuid: String,

    @Json(name = "author_uuid")
    val authorUuid: String,

    @Json(name = "author_meta")
    val authorMeta: Author,

    @Json(name = "text")
    val text: String,

    @Json(name = "upvote_count")
    val upvoteCount: Int,

    @Json(name = "report_count")
    val reportCount: Int,

    @Json(name = "deleted_at")
    val deletedAt: String?

)

data class CommentNode(
    val comment: Comment,
    val depth: Int,
    val children: List<CommentNode> = emptyList()
)

fun formatDateTime(isoString: String): String {
    return try {
        val instant = Instant.parse(isoString)
        val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        isoString
    }
}