package com.kidsphere.game.ui.npc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kidsphere.game.data.db.AppDatabase
import com.kidsphere.game.databinding.ActivityNpcChatBinding
import com.kidsphere.game.ui.adapter.ChatMessageAdapter
import com.kidsphere.game.ui.quest.QuestListActivity
import com.kidsphere.game.viewmodel.NpcViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class NpcChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNpcChatBinding
    private val viewModel: NpcViewModel by viewModels()
    private lateinit var adapter: ChatMessageAdapter
    private var greetingSent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNpcChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val npcId = intent.getStringExtra(EXTRA_NPC_ID) ?: return

        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@NpcChatActivity)
            val npc = db.npcDao().getById(npcId) ?: return@launch
            viewModel.setNpc(npc)
            binding.tvNpcName.text = npc.name
            binding.tvNpcSubject.text = npc.subject.replaceFirstChar { it.uppercase() }
        }

        adapter = ChatMessageAdapter()
        binding.rvMessages.layoutManager = LinearLayoutManager(this).apply { stackFromEnd = true }
        binding.rvMessages.adapter = adapter

        viewModel.messages.observe(this) { messages ->
            adapter.submitList(messages)
            if (messages.isNotEmpty()) binding.rvMessages.smoothScrollToPosition(messages.size - 1)
        }

        viewModel.npc.observe(this) { npc ->
            npc ?: return@observe
            // Only send the greeting on the very first load (no existing messages yet)
            if (!greetingSent && viewModel.messages.value.isNullOrEmpty()) {
                greetingSent = true
                viewModel.sendMessage(npc.greetingText.ifBlank { "Hello!" })
            }
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnSend.isEnabled = !loading
        }

        viewModel.errorMessage.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }

        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.sendMessage(text)
                binding.etMessage.setText("")
            }
        }

        binding.btnQuests.setOnClickListener {
            val intent = Intent(this, QuestListActivity::class.java)
            intent.putExtra(QuestListActivity.EXTRA_NPC_ID, npcId)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_NPC_ID = "extra_npc_id"
    }
}
