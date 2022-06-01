package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import ru.netology.nmedia.databinding.PostContentFragmentBinding

class PostContentFragment : Fragment() {

    private val initialContent
        get() = requireArguments().getString(INITIAL_CONTENT_ARGUMENTS_KEY)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        binding.edit.setText(initialContent)

        binding.edit.requestFocus()
        binding.ok.setOnClickListener{
            onOkButtonClicked(binding)
        }
    }.root

    private fun onOkButtonClicked(binding: PostContentFragmentBinding) {
        val text = binding.edit.text
        if (!text.isNullOrBlank()) {
            val resultBundle = Bundle(1)
            resultBundle.putString(RESULT_KEY, text.toString())
            setFragmentResult(REQUEST_KEY, resultBundle)
        }
        parentFragmentManager.popBackStack()
    }


    companion object{
        const val RESULT_KEY = "postNewContent"
        const val REQUEST_KEY = "postContentRequestKey"

        const val INITIAL_CONTENT_ARGUMENTS_KEY = "initialContent"

        fun createInstance(initialContent: String?) = PostContentFragment().apply {
            arguments = Bundle(1).also {
                it.putString(INITIAL_CONTENT_ARGUMENTS_KEY, initialContent)
            }
        }
    }
}