package com.kidsphere.game.ui.quest

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kidsphere.game.databinding.ActivityQuestListBinding
import com.kidsphere.game.ui.adapter.QuestAdapter
import com.kidsphere.game.viewmodel.GameViewModel

class QuestListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestListBinding
    private val viewModel: GameViewModel by viewModels()
    private lateinit var adapter: QuestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val npcId = intent.getStringExtra(EXTRA_NPC_ID) ?: return

        adapter = QuestAdapter { quest ->
            val intent = Intent(this, QuestActivity::class.java)
            intent.putExtra(QuestActivity.EXTRA_QUEST_ID, quest.id)
            startActivity(intent)
        }

        binding.rvQuests.layoutManager = LinearLayoutManager(this)
        binding.rvQuests.adapter = adapter

        viewModel.getQuestsForNpc(npcId).observe(this) { quests ->
            adapter.submitList(quests)
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_NPC_ID = "extra_npc_id"
    }
}
