package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.ItemNotFoundExceptions
import ru.netology.nmedia.util.SingleLiveEvent

open class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {
    private val repository: PostRepository = FilePostRepository(application)
    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val viewVideoContent = SingleLiveEvent<String>()
    val navigateToNewPostFragment = SingleLiveEvent<String?>()
    val navigateToSinglePostFragment = SingleLiveEvent<Int>()
    val navigateAfterOnRemoveClickedFromPostFragment = SingleLiveEvent<Post>()

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

    override fun onPostClicked(postId: Int) {
        navigateToSinglePostFragment.value = postId
    }

    override fun onRemoveClicked(post: Post) =
        repository.delete(post.id)

    override fun onAddClicked() {
        navigateToNewPostFragment.value = null
    }

    override fun onEditClicked(post: Post) {
        navigateToNewPostFragment.value = post.content
        currentPost.value = post
    }

    override fun onCancelEditClicked() {
        currentPost.value = null
    }
    //endregion InteractionListener

    fun getPostById(postId: Int): Post {
        return data.value?.find { it.id == postId } ?: throw ItemNotFoundExceptions()
    }

    fun deletePostAfterNavigateFromPostFragment(post: Post) {
        navigateAfterOnRemoveClickedFromPostFragment.value = post
    }
}