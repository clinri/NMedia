package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post

internal class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
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
                        R.id.edit ->{
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

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
}