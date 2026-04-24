package com.kidsphere.game.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kidsphere.game.R
import com.kidsphere.game.data.model.ChatMessage
import com.kidsphere.game.data.model.MessageRole
import com.kidsphere.game.databinding.ItemChatMessageBinding

class ChatMessageAdapter : ListAdapter<ChatMessage, ChatMessageAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val msg = getItem(position)
        holder.binding.tvMessage.text = msg.content
        if (msg.role == MessageRole.USER) {
            holder.binding.tvMessage.gravity = Gravity.END
            holder.binding.tvMessage.setBackgroundResource(R.color.colorUserBubble)
        } else {
            holder.binding.tvMessage.gravity = Gravity.START
            holder.binding.tvMessage.setBackgroundResource(R.color.colorNpcBubble)
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(a: ChatMessage, b: ChatMessage) = a.id == b.id
            override fun areContentsTheSame(a: ChatMessage, b: ChatMessage) = a == b
        }
    }
}
