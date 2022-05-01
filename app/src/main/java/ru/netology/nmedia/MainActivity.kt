package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import ru.netology.nmedia.databinding.ActivityPostBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val imageView = findViewById<ImageView>(R.id.imageView)
        val post = Post(
            0,
            "Andrei",
            "test text",
            "01.05.2021",
            likedByMe = true,
            likes = 10000,
            shareCount = 1099999
        )
        binding.render(post)
        binding.like.setOnClickListener {
            println("like clicked")
            post.likedByMe = !post.likedByMe
            post.likes = if (post.likedByMe) post.likes + 1 else post.likes - 1
            binding.like.setImageResource(getLikeIcon(post.likedByMe))
            binding.likesCount.text = post.likes.formatIntLikeVk()
        }
        binding.sharePost.setOnClickListener {
            post.shareCount = post.shareCount + 1
            binding.sharePostCount.text = post.shareCount.formatIntLikeVk()
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
        if (like) R.drawable.ic_like_red_16dp else R.drawable.ic_like_16dp
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
            if (this<10_000) {
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