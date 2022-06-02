package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.sharePostContent.observe(this) { postContent ->
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

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(
                PostContentFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newPostContent)
        }

        viewModel.navigateToPostContentScreenEvent.observe(this) { initialContent ->
            val direction = FeedFragmentDirections.toPostContentFragment(initialContent)
            findNavController().navigate(direction)
        }

        viewModel.playVideoContent.observe(this) { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            Intent.createChooser(
                intent, getString(R.string.chooser_play_video)
            )
            startActivity(intent)
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
    }.root
}