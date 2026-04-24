package com.kidsphere.game.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kidsphere.game.R
import com.kidsphere.game.databinding.FragmentHomeBinding
import com.kidsphere.game.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by viewModels()
    private lateinit var gameAdapter: GameAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeGames()
    }

    private fun setupRecyclerView() {
        gameAdapter = GameAdapter { game ->
            val action = HomeFragmentDirections.actionHomeToGame(game.id)
            findNavController().navigate(action)
        }
        binding.rvGames.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = gameAdapter
        }
    }

    private fun observeGames() {
        lifecycleScope.launch {
            viewModel.allGames.collect { games ->
                if (games.isEmpty()) {
                    viewModel.seedGames()
                } else {
                    gameAdapter.submitList(games)
                    binding.tvEmptyState.visibility = View.GONE
                    binding.rvGames.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
