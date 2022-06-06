package ru.netology.nmedia.db

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.UNDEFINED
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
class PostEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "published")
    val published: String,

    @ColumnInfo(name = "likedByMe", defaultValue = "0")
    val likedByMe: Boolean,
    @ColumnInfo(name = "likes", defaultValue = "0")
    val likes: Int = 0,
    @ColumnInfo(name = "shares", defaultValue = "0")
    val share: Int = 0,
    @ColumnInfo(name = "views", defaultValue = "0")
    val views: Int = 0,
    @ColumnInfo(name = "videoUrl")
    val videoUrl: String? = null,
    @ColumnInfo(name = "videoTitle")
    val videoTitle: String? = null
)