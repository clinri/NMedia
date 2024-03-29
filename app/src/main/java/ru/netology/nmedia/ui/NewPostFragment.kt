package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.NewPostFragmentBinding
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewModel.PostViewModel

class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = NewPostFragmentBinding.inflate(inflater, container, false)
        val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)

        arguments?.textArg.let(binding.edit::setText)
        if (!viewModel.draft.isLocked()) {
            viewModel.draft.getContent().let(binding.edit::setText)
        }
        binding.edit.requestFocus()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!viewModel.draft.isLocked()){
                val text = binding.edit.text.toString()
                viewModel.draft.setContent(text)
                viewModel.draft.lock()
            }
            findNavController().navigateUp()
        }

        binding.ok.setOnClickListener {
            val postContent = binding.edit.text.toString()
            if (!postContent.isNullOrBlank()) {
                viewModel.onSaveButtonClicked(postContent)
            }
            if (!viewModel.draft.isLocked()) {
                viewModel.draft.clearContent()
            }
            findNavController().navigateUp()
        }
        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}