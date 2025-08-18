package com.example.twocentschallenge.repository

import com.example.twocentschallenge.Models.Comment
import com.example.twocentschallenge.Models.Post
import com.example.twocentschallenge.Models.Poll
import com.example.twocentschallenge.enums.FilterEnum

interface PostRepository {
    suspend fun getPosts(filter: FilterEnum, secretKey: String? = null): Result<List<Post>?>
    suspend fun getPostByID(id: String): Result<Post?>
    suspend fun getPostsPerAuthor( authorId: String): Result<List<Post>?>
    suspend fun getPoll(id: String): Result<Poll?>
    suspend fun getComments(id: String): Result<List<Comment>?>
}