package com.example.twocentschallenge.repository

import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.enums.FilterEnum
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPosts(filter: FilterEnum, secretKey: String? = null): Result<List<Post>?>
    suspend fun getPostByID(id: String): Post
    suspend fun getPostsPerAuthor( authorId: String): Result<List<Post>?>
    suspend fun getPoll(id: String): Result<Post?>
}