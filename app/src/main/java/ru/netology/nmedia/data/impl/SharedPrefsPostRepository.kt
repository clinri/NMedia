package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import kotlin.properties.Delegates

class SharedPrefsPostRepository(
    application: Application
) : PostRepository {

    private val prefs = application.getSharedPreferences(
        "repo",
        Context.MODE_PRIVATE
    )
    private var nextId by Delegates.observable(
        prefs.getInt(NEXT_ID_PREFS_KEY, 0)
    ) { _, _, newValue ->
        prefs.edit { putInt(NEXT_ID_PREFS_KEY, newValue) }
    }

    override val data: MutableLiveData<List<Post>>

    private var posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }
        set(value) {
            prefs.edit {
                val serializedPosts = Json.encodeToString(value)
                putString(POST_PREFS_KEY, serializedPosts)
            }
            data.value = value
        }

    init {
        val serializedPosts = prefs.getString(POST_PREFS_KEY, null)
        val posts: List<Post> = if (serializedPosts != null) {
            Json.decodeFromString(serializedPosts)
        } else emptyList()
        data = MutableLiveData(posts)
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
        const val GENERATED_POSTS_AMOUNT = 10
        const val POST_PREFS_KEY = "posts"
        const val NEXT_ID_PREFS_KEY = "next_id"
        const val FILE_NAME = "posts.jsone"
    }
}