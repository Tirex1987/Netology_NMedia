package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.OpenPostFragmentBinding
import ru.netology.nmedia.utils.setTextAmountFormat
import ru.netology.nmedia.utils.setVideoPreview
import ru.netology.nmedia.viewModel.PostViewModel

class OpenPostFragment : Fragment() {

    private val receivedPost by lazy {
        val args by navArgs<OpenPostFragmentArgs>()
        args.post
    }

    private val resultBundle = Bundle(1)

    private val viewModel by viewModels<PostViewModel>()
    /*private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(
                PostContentFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newPostContent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = OpenPostFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val popupMenu by lazy {
            PopupMenu(this.context, binding.options).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.remove -> {
                            resultBundle.putBoolean(REMOVE_POST_KEY, true)
                            resultBundle.putSerializable(RESULT_POST, getCurrentPost())
                            setFragmentResult(REQUEST_KEY, resultBundle)
                            findNavController().popBackStack()
                            true
                        }
                        R.id.edit -> {
                            viewModel.onEditClicked(getCurrentPost())
                            val direction = OpenPostFragmentDirections
                                .actionOpenPostFragmentToPostContentFragment(
                                    getCurrentPost().content, false
                                )
                            findNavController().navigate(direction)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        binding.likes.setOnClickListener{
            viewModel.onLikeClicked(getCurrentPost())
        }

        binding.shares.setOnClickListener{
            viewModel.onShareClicked(getCurrentPost())
        }

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            posts.find { it.id == receivedPost.id }?.also { post ->
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
                    post.videoUrl?.also { url ->
                        videoGroup.visibility = View.VISIBLE
                        video.setVideoPreview(url)
                        videoTitle.text = post.videoTitle
                        play.setOnClickListener { viewModel.onPlayClicked(post) }
                        video.setOnClickListener { play.callOnClick() }
                    } ?: videoGroup.also { it.visibility = View.GONE }
                }
            }
        }

        viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
            val intent = Intent().apply{
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }

        viewModel.playVideoContent.observe(viewLifecycleOwner) { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            Intent.createChooser(
                intent, getString(R.string.chooser_play_video)
            )
            startActivity(intent)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this){
            if (resultBundle.isEmpty) {
                resultBundle.putSerializable(RESULT_POST, getCurrentPost())
                setFragmentResult(REQUEST_KEY, resultBundle)
            }
            findNavController().popBackStack()
        }
    }.root

    private fun getCurrentPost() = requireNotNull(viewModel.data.value?.find { it.id == receivedPost.id }) {
        "Don't find post in viewModel OpenPostFragment"
    }

    companion object{
        const val REQUEST_KEY = "openPostResultKey"
        const val REMOVE_POST_KEY = "removePost"
        const val RESULT_POST = "openPost"
    }
}