package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostContentActivityBinding

class PostContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = intent.getStringExtra(INPUT_TEXT)
        if (!text.isNullOrBlank()) {
            binding.edit.setText(text)
        }

        binding.edit.requestFocus()
        binding.ok.setOnClickListener{
            val intent = Intent()
            val text = binding.edit.text
            if (text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = text.toString()
                intent.putExtra(RESULT_KEY, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    object ResultContract: ActivityResultContract<String?, String?>() {
        override fun createIntent(context: Context, input: String?): Intent =
            Intent(context, PostContentActivity::class.java).apply {
                putExtra(INPUT_TEXT, input)
            }

        override fun parseResult(resultCode: Int, intent: Intent?): String? =
            if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(RESULT_KEY)
            } else null
    }

    private companion object{
        private const val RESULT_KEY = "postNewContent"
        private const val INPUT_TEXT = "postEditContent"
    }
}