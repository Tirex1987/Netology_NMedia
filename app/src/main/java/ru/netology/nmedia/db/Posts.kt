package ru.netology.nmedia.db

import ru.netology.nmedia.data.Post

internal fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    content = content,
    published = published,
    likes = likes,
    likedByMe = likedByMe,
    share = share,
    views = views,
    videoUrl = videoUrl,
    videoTitle = videoTitle
)

internal fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    published = published,
    likes = likes,
    likedByMe = likedByMe,
    share = share,
    views = views,
    videoUrl = videoUrl,
    videoTitle = videoTitle
)