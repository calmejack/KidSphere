package com.kidsphere.game.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kidsphere.game.databinding.FragmentGameBinding
import com.kidsphere.game.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by viewModels()
    private val args: GameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadGame()
    }

    private fun loadGame() {
        lifecycleScope.launch {
            val game = viewModel.allGames.value.firstOrNull { it.id == args.gameId }
            game?.let {
                binding.tvGameTitle.text = it.title
                binding.tvGameDescription.text = it.description
                binding.tvCategory.text = it.category
                binding.tvAgeRange.text = "Ages ${it.minAge}-${it.maxAge}"

                val npcName = "${it.category} Guide"
                val npcPersonality = "enthusiastic educator who loves ${it.category.lowercase()} and children"

                binding.btnStartChat.setOnClickListener {
                    val action = GameFragmentDirections.actionGameToNpc(
                        npcName = npcName,
                        npcPersonality = npcPersonality
                    )
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
