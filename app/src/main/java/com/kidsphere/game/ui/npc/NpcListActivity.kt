package com.kidsphere.game.ui.npc

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kidsphere.game.databinding.ActivityNpcListBinding
import com.kidsphere.game.ui.adapter.NpcAdapter
import com.kidsphere.game.viewmodel.GameViewModel

class NpcListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNpcListBinding
    private val viewModel: GameViewModel by viewModels()
    private lateinit var adapter: NpcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNpcListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val worldId = intent.getStringExtra(EXTRA_WORLD_ID) ?: return
        val worldName = intent.getStringExtra(EXTRA_WORLD_NAME) ?: "World"
        binding.tvWorldName.text = worldName

        adapter = NpcAdapter { npc ->
            val intent = Intent(this, NpcChatActivity::class.java)
            intent.putExtra(NpcChatActivity.EXTRA_NPC_ID, npc.id)
            startActivity(intent)
        }

        binding.rvNpcs.layoutManager = LinearLayoutManager(this)
        binding.rvNpcs.adapter = adapter

        viewModel.getNpcsForWorld(worldId).observe(this) { npcs ->
            adapter.submitList(npcs)
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_WORLD_ID   = "extra_world_id"
        const val EXTRA_WORLD_NAME = "extra_world_name"
    }
}
