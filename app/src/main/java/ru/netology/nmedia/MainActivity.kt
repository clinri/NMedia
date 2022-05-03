package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityPostBinding
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        binding.save.setOnClickListener {
            with(binding.contentEditText) {
                val content = binding.contentEditText.text.toString()
                viewModel.onSaveButtonClicked(content)
                if (content.isBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.empty_content_toast),//"Content can't be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                clearFocus()
                hideKeyboard()
            }
        }

        viewModel.currentPost.observe(this)
        { currentPost ->
            binding.contentEditText.setText(currentPost?.content)
        }
    }
}