package com.kidsphere.game.ui.npc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kidsphere.game.data.local.entity.NpcConversationEntity
import com.kidsphere.game.databinding.ItemMessageNpcBinding
import com.kidsphere.game.databinding.ItemMessageUserBinding

class MessageAdapter : ListAdapter<NpcConversationEntity, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_USER = 0
        private const val VIEW_TYPE_NPC = 1
    }

    override fun getItemViewType(position: Int): Int {
        // Alternate user/npc per message pair - odd indices are NPC responses
        return if (position % 2 == 0) VIEW_TYPE_USER else VIEW_TYPE_NPC
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val binding = ItemMessageUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            UserMessageViewHolder(binding)
        } else {
            val binding = ItemMessageNpcBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            NpcMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is UserMessageViewHolder -> holder.bind(item.userMessage)
            is NpcMessageViewHolder -> holder.bind(item.npcResponse)
        }
    }

    class UserMessageViewHolder(private val binding: ItemMessageUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: String) {
            binding.tvMessage.text = message
        }
    }

    class NpcMessageViewHolder(private val binding: ItemMessageNpcBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: String) {
            binding.tvMessage.text = message
        }
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<NpcConversationEntity>() {
        override fun areItemsTheSame(old: NpcConversationEntity, new: NpcConversationEntity) =
            old.id == new.id

        override fun areContentsTheSame(old: NpcConversationEntity, new: NpcConversationEntity) =
            old == new
    }
}
