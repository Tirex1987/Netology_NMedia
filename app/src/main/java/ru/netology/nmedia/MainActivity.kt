package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.PostListItemBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 0L,
            author = "Andrey",
            content = "Homework",
            published = "16.05.2022",
            likes = 999,
            share = 8_997,
            views = 1_399_500
        )

        binding.render(post)

        binding.likesIcon.setOnClickListener {
            post.likedByMe = !post.likedByMe
            if (post.likedByMe) post.likes++ else post.likes--
            binding.likes.text = getAmountFormat(post.likes)
            binding.likesIcon.setImageResource(getLikeIconResId(post.likedByMe))
        }

        binding.shareIcon.setOnClickListener {
            binding.share.text = getAmountFormat(++post.share)
        }
    }

    private fun PostListItemBinding.render(post: Post) {
        authorName.text = post.author
        content.text = post.content
        published.text = post.published
        likesIcon.setImageResource(getLikeIconResId(post.likedByMe))
        likes.text = getAmountFormat(post.likes)
        share.text = getAmountFormat(post.share)
        views.text = getAmountFormat(post.views)
    }

    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_liked_24dp else R.drawable.ic_likes_24dp

    private fun getAmountFormat(amount: Int): String = when {
        amount >= 1_000_000 -> "%.1f".format((amount / 100_000) / 10.0) + "M"
        amount >= 10_000 -> "${amount / 1_000}K"
        amount >= 1_000 -> "%.1f".format((amount / 100) / 10.0) + "K"
        else -> "$amount"
    }
}