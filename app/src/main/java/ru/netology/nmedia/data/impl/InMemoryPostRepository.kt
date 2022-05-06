package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {
    override val data = MutableLiveData(
        List(10) { index ->
            Post(
                index + 1,
                "Netology",
                "Some random text $index",
                "02.05.2021"
            )
        }
    )

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override fun like(postId: Int) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (!it.likedByMe) it.likes + 1 else it.likes - 1
            )
        }
    }

    override fun share(postId: Int) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(shareCount = it.shareCount + 1)
        }
    }
}