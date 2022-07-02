package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.activity.PostContentActivity
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel : ViewModel(), PostInteractionListener {
    private val repository: PostRepository = InMemoryPostRepository()
    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val viewVideoContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<String?>()

    private val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) {
            return
        }
        val post = currentPost.value?.copy( // edit
            content = content
        ) ?: Post( // new
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "Today",
            video = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        )
        repository.save(post)
        currentPost.value = null
    }

    //region PostInteractionListener

    override fun onLikeClicked(post: Post) =
        repository.like(post.id)

    override fun onShareClicked(post: Post) {
        sharePostContent.value = post.content
        repository.share(post.id)
    }

    override fun onPlayClicked(post: Post) {
        viewVideoContent.value = post.video
    }

    override fun onRemoveClicked(post: Post) =
        repository.delete(post.id)

    override fun onAddClicked() {
        navigateToPostContentScreenEvent.value = null
    }

    override fun onEditClicked(post: Post) {
        navigateToPostContentScreenEvent.value = post.content
        currentPost.value = post
    }

    override fun onCancelEditClicked() {
        currentPost.value = null
    }
    //endregion InteractionListener
}