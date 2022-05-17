package ru.netology.nmedia.data

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post

class PostRepositoryInMemory : PostRepository {
    override val data = MutableLiveData(
        Post(
            id = 0L,
            author = "Andrey",
            content = "Homework\nBla-bla-bla, Bla-bla-bla",
            published = "16.05.2022",
            likes = 999,
            share = 8_997,
            views = 1_399_500
        )
    )

    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "Data value should not be null"
        }
        val likesCount = if (!currentPost.likedByMe) ++currentPost.likes else --currentPost.likes
        val likedPost = currentPost.copy(likedByMe = !currentPost.likedByMe, likes = likesCount)
        data.value = likedPost
    }

    override fun share() {
        val currentPost = checkNotNull(data.value) {
            "Data value should not be null"
        }
        data.value = currentPost.copy(share = ++currentPost.share)
    }
}