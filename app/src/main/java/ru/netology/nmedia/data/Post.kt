package ru.netology.nmedia.data

import kotlinx.serialization.Serializable

@Serializable
data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val likedByMe: Boolean = false,
    val share: Int = 0,
    val views: Int = 0,
    val videoUrl: String? = null,
    val videoTitle: String? = null
)