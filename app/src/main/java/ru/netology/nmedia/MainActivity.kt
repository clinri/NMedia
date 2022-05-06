package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            binding.render(post)
        }

        binding.like.setOnClickListener {
            viewModel.onLikeClicked()
        }

        binding.sharePost.setOnClickListener {
            viewModel.onShareClicked()
        }
    }

    private fun ActivityPostBinding.render(post: Post) {
        textTitle.text = post.author
        date.text = post.published
        textOfPost.text = post.content
        like.setImageResource(getLikeIcon(post.likedByMe))
        likesCount.text = post.likes.formatIntLikeVk()
        sharePostCount.text = post.shareCount.formatIntLikeVk()
    }

    private fun getLikeIcon(like: Boolean) =
        if (like) R.drawable.ic_like_red_24dp else R.drawable.ic_like_white_24dp
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