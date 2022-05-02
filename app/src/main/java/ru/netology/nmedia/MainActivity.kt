package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.data.impl.PostsAdapter
import ru.netology.nmedia.databinding.ActivityPostBinding
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel::onLikeClicked, viewModel::onShareClicked)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
                adapter.submitList(posts)
        }
    }
}