package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityPostContentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val screenMode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (screenMode == MODE_EDIT) {
            val textContent = PostViewModel.currentPost.value?.content
            binding.edit.setText(textContent)
        }
        binding.edit.requestFocus()
        binding.ok.setOnClickListener {
            val intent = Intent()
            val text = binding.edit.text
            if (text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = text.toString()
                intent.putExtra(RESULT_KEY, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    object ResultContract : ActivityResultContract<String, String?>() {
        override fun createIntent(context: Context, input: String) =
            Intent(context, PostContentActivity::class.java)
                .putExtra(EXTRA_SCREEN_MODE, input)


        override fun parseResult(resultCode: Int, intent: Intent?) =
            if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(RESULT_KEY)
            } else null
    }

    companion object {
        private const val RESULT_KEY = "postNewContent"
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        const val MODE_EDIT = "mode_edit"
        const val MODE_ADD = "mode_add"
    }
}