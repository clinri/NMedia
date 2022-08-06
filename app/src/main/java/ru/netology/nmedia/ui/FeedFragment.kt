package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.ui.NewPostFragment.Companion.textArg
import ru.netology.nmedia.ui.PostFragment.Companion.idArg
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FeedFragmentBinding.inflate(inflater, container, false)
        val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)

        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }

        viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
            startShareActivity(postContent)
        }

        viewModel.viewVideoContent.observe(viewLifecycleOwner) { url ->
            startVideoActivity(url)
        }

        viewModel.navigateAfterOnRemoveClickedFromPostFragment.observe(viewLifecycleOwner) { post ->
            viewModel.onRemoveClicked(post)
        }

        viewModel.navigateToNewPostFragment.observe(viewLifecycleOwner) {
            findNavController()
                .navigate(
                    R.id.action_feedFragment_to_postContentFragment,
                    Bundle().apply {
                        textArg = it
                    }
                )
        }
        viewModel.navigateToSinglePostFragment.observe(viewLifecycleOwner) {
            findNavController()
                .navigate(
                    R.id.action_feedFragment_to_postFragment,
                    Bundle().apply {
                        idArg = it
                    }
                )
        }
        return binding.root
    }

    companion object{
        fun Fragment.startShareActivity(postContent: String){
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

        fun Fragment.startVideoActivity(url: String){
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

}