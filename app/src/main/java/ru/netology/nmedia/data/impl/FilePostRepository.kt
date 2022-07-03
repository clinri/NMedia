package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import java.lang.Exception
import kotlin.properties.Delegates

class FilePostRepository(
    private val application: Application
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private val prefs = application.getSharedPreferences(
        "repo",
        Context.MODE_PRIVATE
    )

    override val data: MutableLiveData<List<Post>>
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

    private var nextId by Delegates.observable(
        prefs.getInt(NEXT_ID_PREFS_KEY, 0)
    ) { _, _, newValue ->
        prefs.edit { putInt(NEXT_ID_PREFS_KEY, newValue) }
    }

    init {
        val postsFile = application.filesDir.resolve(FILE_NAME)
        var postsInit: List<Post> = emptyList()
        try {
            postsInit = if (postsFile.exists()) {
                val inputStream = application.openFileInput(FILE_NAME)
                val reader = inputStream.bufferedReader()
                reader.use {
                    gson.fromJson(it, type)
                }
            } else emptyList()
        } catch (e: Exception) {
            Log.d("Read error", "Error read JSON from file $FILE_NAME")
        }
        data = MutableLiveData(postsInit)
    }

    override fun like(postId: Int) {
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (!it.likedByMe) it.likes + 1 else it.likes - 1
            )
        }
    }

    override fun share(postId: Int) {
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(shareCount = it.shareCount + 1)
        }
    }

    override fun delete(postId: Int) {
        posts = posts.filterNot { it.id == postId }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        posts = listOf(
            post.copy(id = ++nextId)
        ) + posts
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private companion object {
        const val NEXT_ID_PREFS_KEY = "next_id"
        const val FILE_NAME = "posts.json"
    }
}