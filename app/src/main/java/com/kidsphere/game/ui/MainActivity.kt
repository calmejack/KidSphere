package com.kidsphere.game.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kidsphere.game.databinding.ActivityMainBinding
import com.kidsphere.game.ui.settings.SettingsActivity
import com.kidsphere.game.ui.world.WorldMapActivity
import com.kidsphere.game.viewmodel.GameViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.player.observe(this) { player ->
            player?.let {
                binding.tvPlayerName.text = it.name
                binding.tvStars.text = "⭐ ${it.totalStars}"
                binding.tvLevel.text = "Lv.${it.level}"
            }
        }

        viewModel.toastMessage.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearToast()
            }
        }

        binding.btnPlay.setOnClickListener {
            startActivity(Intent(this, WorldMapActivity::class.java))
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnRewards.setOnClickListener {
            startActivity(Intent(this, RewardsActivity::class.java))
        }
    }
}
