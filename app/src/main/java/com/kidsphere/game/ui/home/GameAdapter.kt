package com.kidsphere.game.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kidsphere.game.data.local.entity.GameEntity
import com.kidsphere.game.databinding.ItemGameBinding

class GameAdapter(
    private val onGameClick: (GameEntity) -> Unit
) : ListAdapter<GameEntity, GameAdapter.GameViewHolder>(GameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GameViewHolder(
        private val binding: ItemGameBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(game: GameEntity) {
            binding.tvGameTitle.text = game.title
            binding.tvGameDescription.text = game.description
            binding.tvGameCategory.text = game.category
            binding.tvAgeRange.text = "Ages ${game.minAge}-${game.maxAge}"
            binding.root.setOnClickListener { onGameClick(game) }
        }
    }

    class GameDiffCallback : DiffUtil.ItemCallback<GameEntity>() {
        override fun areItemsTheSame(oldItem: GameEntity, newItem: GameEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GameEntity, newItem: GameEntity) =
            oldItem == newItem
    }
}
