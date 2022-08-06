package ru.netology.nmedia.adapter;

import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding;
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: PostBinding,
    listener: PostInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var post: Post
    private val popupMenu by lazy {
        PopupMenu(itemView.context, binding.menu).apply {
            inflate(R.menu.options_post)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.remove -> {
                        listener.onRemoveClicked(post)
                        true
                    }
                    R.id.edit -> {
                        listener.onEditClicked(post)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    init {
        binding.like.setOnClickListener {
            listener.onLikeClicked(post)
        }
        binding.sharePost.setOnClickListener {
            listener.onShareClicked(post)
        }
        binding.playButton.setOnClickListener {
            listener.onPlayClicked(post)
        }
        binding.imageViewVideo.setOnClickListener {
            listener.onPlayClicked(post)
        }
        //Click on Post: postCardFrame,avatar,textTitle,date,view,textOfPost
        binding.postCardFrame.setOnClickListener {
            listener.onPostClicked(post.id)
        }
        binding.avatar.setOnClickListener {
            listener.onPostClicked(post.id)
        }
        binding.textTitle.setOnClickListener {
            listener.onPostClicked(post.id)
        }
        binding.date.setOnClickListener {
            listener.onPostClicked(post.id)
        }
        binding.view.setOnClickListener {
            listener.onPostClicked(post.id)
        }
        binding.textOfPost.setOnClickListener {
            listener.onPostClicked(post.id)
        }
    }

    fun bind(post: Post) {
        this.post = post
        with(binding) {
            textTitle.text = post.author
            date.text = post.published
            textOfPost.text = post.content
            view.text = "104"
            like.isChecked = post.likedByMe
            like.text = post.likes.formatIntLikeVk()
            sharePost.text = post.shareCount.formatIntLikeVk()
            menu.setOnClickListener { popupMenu.show() }
        }
    }

    private fun Int.formatIntLikeVk(): String {
        var digitOne = 0
        var digitTwo = 0
        val symbol = when (this / 1000) {
            0 -> {
                digitOne = this
                ""
            }
            in 1..999 -> {
                digitOne = this / 1_000
                if (this < 10_000) {
                    digitTwo = this % 1_000 / 100
                }
                "K"
            }
            in 1_000 until 1_000_000 -> {
                digitOne = this / 1_000_000
                digitTwo = this % 1_000_000 / 100_000
                "M"
            }
            else -> ""
        }
        return "${digitOne}${if (digitTwo != 0) ".$digitTwo" else ""}$symbol"
    }
}


