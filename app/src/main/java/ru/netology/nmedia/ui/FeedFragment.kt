package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>()
    /*val viewModel: PostViewModel by viewModels(
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

        setFragmentResultListener(
            requestKey = OpenPostFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            val post = bundle.getSerializable(OpenPostFragment.RESULT_POST) as Post
            if (requestKey != OpenPostFragment.REQUEST_KEY) return@setFragmentResultListener
            if (bundle.getBoolean(OpenPostFragment.REMOVE_POST_KEY)) {
                viewModel.onRemoveClicked(post)
            } else {
                viewModel.onChangePost(post)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
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

        viewModel.navigateToPostContentScreenEvent.observe(viewLifecycleOwner) { initialContent ->
            val direction = FeedFragmentDirections.toPostContentFragment(initialContent)
            findNavController().navigate(direction)
        }

        viewModel.navigateToOpenPostScreenEvent.observe(viewLifecycleOwner) { post ->
            val direction = FeedFragmentDirections.toOpenPostFragment(post)
            findNavController().navigate(direction)
        }

        viewModel.playVideoContent.observe(viewLifecycleOwner) { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            Intent.createChooser(
                intent, getString(R.string.chooser_play_video)
            )
            startActivity(intent)
        }
    }.root
}