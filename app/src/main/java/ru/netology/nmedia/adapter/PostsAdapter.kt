package ru.netology.nmedia.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.Group
import androidx.core.view.get
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.CardPostBinding

//typealias onLikeClicked = (post: Post) -> Unit

internal class PostsAdapter (
    private val interactionListener: PostInteractionListener
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

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.remove -> {
                            interactionListener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            interactionListener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.likes.setOnClickListener{ interactionListener.onLikeClicked(post) }
            binding.shares.setOnClickListener{ interactionListener.onShareClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                content.text = post.content
                published.text = post.published
                likes.text = getAmountFormat(post.likes)
                shares.text = getAmountFormat(post.share)
                views.text = getAmountFormat(post.views)
                options.setOnClickListener { popupMenu.show() }
                avatar.setImageResource(R.drawable.ic_launcher_foreground)
                likes.isChecked = post.likedByMe
                if (post.videoUrl.isNullOrBlank()){
                    videoGroup.visibility = View.GONE
                } else {
                    videoGroup.visibility = View.VISIBLE
                    videoTitle.setText(post.videoTitle)
                    play.setOnClickListener{ interactionListener.onPlayClicked(post) }
                    video.setOnClickListener{ play.callOnClick() }
                }
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