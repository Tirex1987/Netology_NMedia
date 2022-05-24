package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.PostRepositoryInMemory

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = PostRepositoryInMemory()

    val data by repository::data

    val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: String) {
        if (content.isNullOrBlank()) return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "Today"
        )
        repository.save(post)
        currentPost.value = null
    }

    fun onCancelButtonClicked() {
        currentPost.value = null
    }

    // region PostInteractionListener

    override fun onLikeClicked(post: Post) =
        repository.like(post.id)

    override fun onShareClicked(post: Post) =
        repository.share(post.id)

    override fun onRemoveClicked(post: Post) =
        repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
    }

    // endregion PostInteractionListener
}