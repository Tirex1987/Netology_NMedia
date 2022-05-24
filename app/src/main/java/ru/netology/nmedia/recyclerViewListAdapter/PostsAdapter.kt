package ru.netology.nmedia.recyclerViewListAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding

//typealias onLikeClicked = (post: Post) -> Unit

internal class PostsAdapter (
    private val onLikeClicked: (Post) -> Unit,
    private val onShareClicked: (Post) -> Unit
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardPostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(private val binding: CardPostBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var post: Post
        init {
            binding.likesIcon.setOnClickListener{ onLikeClicked(post) }
            binding.shareIcon.setOnClickListener{ onShareClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                content.text = post.content
                published.text = post.published
                likesIcon.setImageResource(getLikeIconResId(post.likedByMe))
                likes.text = getAmountFormat(post.likes)
                share.text = getAmountFormat(post.share)
                views.text = getAmountFormat(post.views)
            }
        }

        @DrawableRes
        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_liked_24dp else R.drawable.ic_likes_24dp

        private fun getAmountFormat(amount: Int): String = when {
            amount >= 1_000_000 -> "%.1f".format((amount / 100_000) / 10.0) + "M"
            amount >= 10_000 -> "${amount / 1_000}K"
            amount >= 1_000 -> "%.1f".format((amount / 100) / 10.0) + "K"
            else -> "$amount"
        }
    }

    private object DiffCallback: DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem
    }
}