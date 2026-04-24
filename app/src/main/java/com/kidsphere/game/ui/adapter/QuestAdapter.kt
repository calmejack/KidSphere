package com.kidsphere.game.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kidsphere.game.data.model.Quest
import com.kidsphere.game.data.model.QuestStatus
import com.kidsphere.game.databinding.ItemQuestBinding

class QuestAdapter(private val onQuestClick: (Quest) -> Unit) :
    ListAdapter<Quest, QuestAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemQuestBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemQuestBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val quest = getItem(position)
        holder.binding.tvQuestTitle.text = quest.title
        holder.binding.tvQuestDesc.text = quest.description
        holder.binding.tvQuestReward.text = "⭐ ${quest.rewardStars}"
        holder.binding.tvQuestStatus.text = when(quest.status) {
            QuestStatus.COMPLETED   -> "✅"
            QuestStatus.IN_PROGRESS -> "▶"
            QuestStatus.AVAILABLE   -> "🎯"
            QuestStatus.LOCKED      -> "🔒"
        }
        holder.binding.root.setOnClickListener { onQuestClick(quest) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Quest>() {
            override fun areItemsTheSame(a: Quest, b: Quest) = a.id == b.id
            override fun areContentsTheSame(a: Quest, b: Quest) = a == b
        }
    }
}
