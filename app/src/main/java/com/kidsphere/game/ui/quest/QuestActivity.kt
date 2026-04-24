package com.kidsphere.game.ui.quest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kidsphere.game.data.db.AppDatabase
import com.kidsphere.game.data.model.Quest
import com.kidsphere.game.data.model.QuestStatus
import com.kidsphere.game.databinding.ActivityQuestBinding
import com.kidsphere.game.viewmodel.GameViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestBinding
    private val viewModel: GameViewModel by viewModels()
    private var quest: Quest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val questId = intent.getStringExtra(EXTRA_QUEST_ID) ?: return

        CoroutineScope(Dispatchers.Main).launch {
            val db = AppDatabase.getInstance(this@QuestActivity)
            val q = db.questDao().getById(questId) ?: return@launch
            quest = q
            binding.tvQuestTitle.text = q.title
            binding.tvQuestDesc.text = q.description
            binding.tvQuestType.text = "Type: ${q.type.name}"
            binding.tvQuestReward.text = "Reward: ⭐ ${q.rewardStars} stars"
            binding.tvKnowledgeTag.text = "Topic: ${q.knowledgeTag}"

            if (q.status == QuestStatus.COMPLETED) {
                binding.btnComplete.isEnabled = false
                binding.tvQuestStatus.text = "✅ Completed"
                binding.tvQuestStatus.visibility = View.VISIBLE
            }
        }

        binding.btnComplete.setOnClickListener {
            quest?.let { q ->
                viewModel.onQuestCompleted(q)
                binding.btnComplete.isEnabled = false
                binding.tvQuestStatus.text = "✅ Quest Complete! +${q.rewardStars} ⭐"
                binding.tvQuestStatus.visibility = View.VISIBLE
                Toast.makeText(this, "Great job! 🎉", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.toastMessage.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearToast()
            }
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_QUEST_ID = "extra_quest_id"
    }
}
