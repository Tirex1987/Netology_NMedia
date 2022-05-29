package ru.netology.nmedia.data

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import kotlin.properties.Delegates

class FilePostRepository (
    private val application: Application
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type


    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    private var lastId: Long by Delegates.observable(
        prefs.getLong(NEXT_TO_PREFS_KEY, 0L)
    ) { _, _, newValue ->
        prefs.edit { putLong(NEXT_TO_PREFS_KEY, newValue) }

    }

    private var posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }
        set(value) {
            application.openFileOutput(
                FILE_NAME, Context.MODE_PRIVATE
            ).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
            data.value = value
        }

    override val data : MutableLiveData<List<Post>>

    init {
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use {
                try {
                    gson.fromJson(it, type)
                } catch (e: JsonSyntaxException) {
                    Toast.makeText(
                        application.applicationContext,
                        R.string.json_read_exception,
                        Toast.LENGTH_LONG
                    ).show()
                    emptyList()
                } catch (e: JsonIOException) {
                    Toast.makeText(
                        application.applicationContext,
                        R.string.json_io_exception,
                        Toast.LENGTH_LONG
                    ).show()
                    emptyList()
                }
            }
        } else emptyList()
        data = MutableLiveData(posts)
    }

    override fun like(postId: Long) {
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(
                likedByMe = !it.likedByMe,
                likes = it.likes + if (it.likedByMe) -1 else +1
            )
        }
    }

    override fun share(postId: Long) {
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(share = it.share + 1)
        }
    }

    override fun delete(postId: Long) {
        posts = posts.filterNot { it.id == postId }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        posts = listOf(
            post.copy(id = ++lastId)
        ) + posts
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private companion object {
        const val NEXT_TO_PREFS_KEY = "lastId"
        const val FILE_NAME = "posts.json"
    }
}