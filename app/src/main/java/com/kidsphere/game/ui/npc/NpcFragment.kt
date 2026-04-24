package com.kidsphere.game.ui.npc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kidsphere.game.databinding.FragmentNpcBinding
import com.kidsphere.game.viewmodel.NpcViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NpcFragment : Fragment() {

    private var _binding: FragmentNpcBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NpcViewModel by viewModels()
    private val args: NpcFragmentArgs by navArgs()
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNpcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSendButton()
        observeUiState()
        viewModel.loadConversation(args.npcName)
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter()
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            adapter = messageAdapter
        }
    }

    private fun setupSendButton() {
        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(
                    npcName = args.npcName,
                    npcPersonality = args.npcPersonality,
                    userMessage = message,
                    useTts = binding.switchTts.isChecked
                )
                binding.etMessage.text?.clear()
            }
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                binding.btnSend.isEnabled = !state.isLoading
                messageAdapter.submitList(state.messages)
                if (state.messages.isNotEmpty()) {
                    binding.rvMessages.scrollToPosition(state.messages.size - 1)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
