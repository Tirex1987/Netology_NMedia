package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.utils.setTextAmountFormat
import ru.netology.nmedia.utils.setVideoPreview

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
            binding.constraintLayoutPost.setOnClickListener { interactionListener.onPostClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                content.text = post.content
                published.text = post.published
                likes.setTextAmountFormat(post.likes)
                shares.setTextAmountFormat(post.share)
                views.setTextAmountFormat(post.views)
                options.setOnClickListener { popupMenu.show() }
                avatar.setImageResource(R.drawable.ic_launcher_foreground)
                likes.isChecked = post.likedByMe
                if (post.videoUrl.isNullOrBlank()){
                    videoGroup.visibility = View.GONE
                } else {
                    videoGroup.visibility = View.VISIBLE
                    video.setVideoPreview(post.videoUrl)
                    videoTitle.setText(post.videoTitle)
                    play.setOnClickListener{ interactionListener.onPlayClicked(post) }
                    video.setOnClickListener{ play.callOnClick() }
                }
            }
        }
    }

    private object DiffCallback: DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem
    }
}