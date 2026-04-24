package com.kidsphere.game.ui.world

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kidsphere.game.databinding.ActivityWorldMapBinding
import com.kidsphere.game.ui.adapter.WorldAdapter
import com.kidsphere.game.ui.npc.NpcListActivity
import com.kidsphere.game.viewmodel.GameViewModel

class WorldMapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorldMapBinding
    private val viewModel: GameViewModel by viewModels()
    private lateinit var adapter: WorldAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorldMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = WorldAdapter { world ->
            if (world.isUnlocked) {
                val intent = Intent(this, NpcListActivity::class.java)
                intent.putExtra(NpcListActivity.EXTRA_WORLD_ID, world.id)
                intent.putExtra(NpcListActivity.EXTRA_WORLD_NAME, world.name)
                startActivity(intent)
            }
        }

        binding.rvWorlds.layoutManager = LinearLayoutManager(this)
        binding.rvWorlds.adapter = adapter

        viewModel.allWorlds.observe(this) { worlds ->
            adapter.submitList(worlds)
        }

        viewModel.player.observe(this) { player ->
            binding.tvStarsNeeded.text = player?.let { "Your stars: ⭐ ${it.totalStars}" } ?: ""
        }

        binding.btnBack.setOnClickListener { finish() }
    }
}
