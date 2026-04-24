package com.kidsphere.game.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.kidsphere.game.databinding.ActivityRewardsBinding
import com.kidsphere.game.ui.adapter.RewardAdapter
import com.kidsphere.game.viewmodel.GameViewModel

class RewardsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRewardsBinding
    private val viewModel: GameViewModel by viewModels()
    private lateinit var adapter: RewardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RewardAdapter()
        binding.rvRewards.layoutManager = GridLayoutManager(this, 3)
        binding.rvRewards.adapter = adapter

        viewModel.collectedRewards.observe(this) { rewards ->
            adapter.submitList(rewards)
            binding.tvRewardCount.text = "Collected: ${rewards.size}"
        }

        binding.btnBack.setOnClickListener { finish() }
    }
}
