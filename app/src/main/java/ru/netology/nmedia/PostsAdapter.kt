package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post

typealias OnLikeClicked = (Post) -> Unit
typealias OnShareClicked = (Post) -> Unit

internal class PostsAdapter(
    private val onLikeClicked: OnLikeClicked,
    private val onShareClicked: OnShareClicked,
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: PostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) = with(binding) {
            textTitle.text = post.author
            date.text = post.published
            textOfPost.text = post.content
            viewCount.text = "104"
            like.setImageResource(getLikeIcon(post.likedByMe))
            likesCount.text = post.likes.formatIntLikeVk()
            sharePostCount.text = post.shareCount.formatIntLikeVk()
            like.setOnClickListener { onLikeClicked(post) }
            sharePost.setOnClickListener { onShareClicked(post) }
        }

        @DrawableRes
        private fun getLikeIcon(like: Boolean) =
            if (like) R.drawable.ic_like_red_24dp else R.drawable.ic_like_white_24dp

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

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
}