package ru.netology.nmedia.data

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post

class PostRepositoryInMemory : PostRepository {

    private var lastId = 0L

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override val data = MutableLiveData(
        listOf(
            Post(
                id = ++lastId,
                author = "Andrey",
                content = "Homework\nBla-bla-bla, Bla-bla-bla $lastId",
                published = "28.05.2022",
                videoUrl = "https://www.youtube.com/watch?v=F7MJJeDTd7E",
                videoTitle = "Youtube video"
            ),
            Post(
                    id = ++lastId,
            author = "Andrey",
            content = "Homework\nBla-bla-bla, Bla-bla-bla $lastId",
            published = "28.05.2022",
            videoUrl = "https://rutube.ru/video/88b8f487354de842f2d4efef3bdd0bac/",
            videoTitle = "Rutube video"
            )
        ) +
                List(100) { index ->
                    Post(
                        id = ++lastId,
                        author = "Andrey",
                        content = "Homework\nBla-bla-bla, Bla-bla-bla $lastId",
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

    override fun delete(postId: Long) {
        data.value = posts.filterNot { it.id == postId }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        data.value = listOf(
            post.copy(id = ++lastId)
        ) + posts
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }
}