package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.PostFragmentBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.ui.FeedFragment.Companion.startShareActivity
import ru.netology.nmedia.ui.FeedFragment.Companion.startVideoActivity
import ru.netology.nmedia.ui.NewPostFragment.Companion.textArg
import ru.netology.nmedia.util.IdArg
import ru.netology.nmedia.viewModel.PostViewModel

class PostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = PostFragmentBinding.inflate(inflater, container, false)
        val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)
        val viewHolder = PostViewHolder(binding.postLayout, object : PostInteractionListener {
            override fun onLikeClicked(post: Post) {
                viewModel.onLikeClicked(post)
            }

            override fun onShareClicked(post: Post) {
                viewModel.onShareClicked(post)
            }

            override fun onRemoveClicked(post: Post) {
                viewModel.deletePostAfterNavigateFromPostFragment(post)
                findNavController().navigateUp()
            }

            override fun onAddClicked() {
            }

            override fun onEditClicked(post: Post) {
                viewModel.onEditClicked(post)
            }

            override fun onCancelEditClicked() {
            }

            override fun onPlayClicked(post: Post) {
                viewModel.onPlayClicked(post)
            }

            override fun onPostClicked(postId: Int) {
            }

        })
        viewModel.data.observe(viewLifecycleOwner) {
            viewHolder.bind(arguments!!.idArg.let(viewModel::getPostById))
        }
        viewModel.navigateToNewPostFragment.observe(viewLifecycleOwner) {
            findNavController()
                .navigate(
                    R.id.action_postFragment_to_postContentFragment,
                    Bundle().apply {
                        textArg = it
                    }
                )
        }
        viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
            startShareActivity(postContent)
        }
        viewModel.viewVideoContent.observe(viewLifecycleOwner) { url ->
            startVideoActivity(url)
        }
        return binding.root
    }

    companion object {
        var Bundle.idArg: Int by IdArg
    }
}