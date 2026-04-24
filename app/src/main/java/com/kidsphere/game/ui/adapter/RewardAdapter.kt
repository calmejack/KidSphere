package com.kidsphere.game.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kidsphere.game.data.model.Reward
import com.kidsphere.game.data.model.RewardType
import com.kidsphere.game.databinding.ItemRewardBinding

class RewardAdapter : ListAdapter<Reward, RewardAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemRewardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemRewardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val reward = getItem(position)
        holder.binding.tvRewardName.text = reward.name
        holder.binding.tvRewardDesc.text = reward.description
        holder.binding.tvRewardIcon.text = when(reward.type) {
            RewardType.STAR         -> "⭐"
            RewardType.BADGE        -> "🏅"
            RewardType.ITEM         -> "🎁"
            RewardType.WORLD_UNLOCK -> "🗺️"
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Reward>() {
            override fun areItemsTheSame(a: Reward, b: Reward) = a.id == b.id
            override fun areContentsTheSame(a: Reward, b: Reward) = a == b
        }
    }
}
