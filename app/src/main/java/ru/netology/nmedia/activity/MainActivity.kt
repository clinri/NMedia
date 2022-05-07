package ru.netology.nmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityPostBinding
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

        viewModel.currentPost.observe(this) { currentPost ->
            binding.contentEditText.setText(currentPost?.content)
            binding.editTextView.setText(currentPost?.content)
            if (currentPost != null) {
                binding.groupEdit.visibility = View.VISIBLE
                binding.closeEdit.setOnClickListener {
                    viewModel.onCancelEditClicked()
                    with(binding.contentEditText) {
                        clearFocus()
                        hideKeyboard()
                    }
                }
            } else {
                binding.groupEdit.visibility = View.GONE
            }
        }
        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }
    }
}