package ru.netology.nmedia.service.actions

import com.google.gson.annotations.SerializedName

class NewPost(
    @SerializedName("userId")
    val userId: Long,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("postId")
    val postId: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String
)