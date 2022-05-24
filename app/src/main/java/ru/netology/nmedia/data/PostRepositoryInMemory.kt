package ru.netology.nmedia.data

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post

class PostRepositoryInMemory : PostRepository {

    private val posts get() = checkNotNull(data.value) {
        "Data value should not be null"
    }

    override val data = MutableLiveData(
        List(100) { index ->
            Post(
                id = index + 1L,
                author = "Andrey",
                content = "Homework\nBla-bla-bla, Bla-bla-bla $index",
                published = "16.05.2022",
                likes = 999,
                share = 8_997,
                views = 1_399_500
            )
        }
    )

    override fun like(postId: Long) {
        data.value = posts.map {
            if (it.id != postId) it
            else {
                val likesCount = if (!it.likedByMe) it.likes + 1 else it.likes - 1
                it.copy(likedByMe = !it.likedByMe, likes = likesCount)
            }
        }
    }

    override fun share(postId: Long) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(share = it.share + 1)
        }
    }
}