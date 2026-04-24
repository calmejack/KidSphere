package com.kidsphere.game.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kidsphere.game.data.model.World
import com.kidsphere.game.databinding.ItemWorldBinding

class WorldAdapter(private val onWorldClick: (World) -> Unit) :
    ListAdapter<World, WorldAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemWorldBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemWorldBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val world = getItem(position)
        holder.binding.tvWorldName.text = world.name
        holder.binding.tvWorldDesc.text = world.description
        holder.binding.tvWorldStatus.text = if (world.isUnlocked) "✅ Unlocked" else "🔒 ${world.requiredStars}⭐ needed"
        holder.binding.root.alpha = if (world.isUnlocked) 1f else 0.5f
        holder.binding.root.setOnClickListener { onWorldClick(world) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<World>() {
            override fun areItemsTheSame(a: World, b: World) = a.id == b.id
            override fun areContentsTheSame(a: World, b: World) = a == b
        }
    }
}
