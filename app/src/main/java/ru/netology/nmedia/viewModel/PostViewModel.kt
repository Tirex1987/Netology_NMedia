package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.FilePostRepository
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.SQLiteRepository
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.utils.SingleLiveEvent

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository =
        SQLiteRepository(
            dao = AppDb.getInstance(
                context = application
            ).postDao
        )

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<Pair<String, Boolean>>()
    val navigateToOpenPostScreenEvent = SingleLiveEvent<Post>()
    val playVideoContent = SingleLiveEvent<String>()

    private val currentPost = MutableLiveData<Post?>(null)

    private var enteredTextForPost: String? = null

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return

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
        enteredTextForPost = null
    }

    fun onAddClicked() {
        navigateToPostContentScreenEvent.value = (Pair(enteredTextForPost ?: "", true))
    }

    fun onChangePost(post: Post) {
        repository.save(post)
    }

    fun saveEnteredText(text: String) {
        enteredTextForPost = text
    }

    // region PostInteractionListener

    override fun onLikeClicked(post: Post) =
        repository.like(post.id)

    override fun onShareClicked(post: Post) {
        sharePostContent.value = post.content
        repository.share(post.id)
    }

    override fun onRemoveClicked(post: Post) =
        repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.value = Pair(post.content, false)
    }

    override fun onPlayClicked(post: Post) {
        playVideoContent.value = post.videoUrl ?: return
    }

    override fun onPostClicked(post: Post) {
        navigateToOpenPostScreenEvent.value = post
    }

    // endregion PostInteractionListener
}