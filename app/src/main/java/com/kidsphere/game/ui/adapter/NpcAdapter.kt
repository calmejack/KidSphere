package com.kidsphere.game.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kidsphere.game.data.model.Npc
import com.kidsphere.game.databinding.ItemNpcBinding

class NpcAdapter(private val onNpcClick: (Npc) -> Unit) :
    ListAdapter<Npc, NpcAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemNpcBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemNpcBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val npc = getItem(position)
        holder.binding.tvNpcName.text = npc.name
        holder.binding.tvNpcSubject.text = "Subject: ${npc.subject}"
        holder.binding.tvNpcPersonality.text = npc.personality
        holder.binding.root.setOnClickListener { onNpcClick(npc) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Npc>() {
            override fun areItemsTheSame(a: Npc, b: Npc) = a.id == b.id
            override fun areContentsTheSame(a: Npc, b: Npc) = a == b
        }
    }
}
