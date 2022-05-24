package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.PostRepositoryInMemory

class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemory()

    val data by repository::data

    fun onLikeClicked(post: Post) = repository.like(post.id)

    fun onShareClicked(post: Post) = repository.share(post.id)
}