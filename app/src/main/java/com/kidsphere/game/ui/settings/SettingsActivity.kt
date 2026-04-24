package com.kidsphere.game.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kidsphere.game.api.ApiKeyManager
import com.kidsphere.game.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var keyManager: ApiKeyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        keyManager = ApiKeyManager(this)

        binding.etAiBaseUrl.setText(keyManager.aiBaseUrl)
        binding.etAiApiKey.setText(keyManager.aiApiKey)
        binding.etAiModel.setText(keyManager.aiModel)
        binding.etAliyunAppKey.setText(keyManager.aliyunAppKey)
        binding.etAliyunToken.setText(keyManager.aliyunToken)

        binding.btnSave.setOnClickListener {
            keyManager.aiBaseUrl    = binding.etAiBaseUrl.text.toString().trim()
            keyManager.aiApiKey     = binding.etAiApiKey.text.toString().trim()
            keyManager.aiModel      = binding.etAiModel.text.toString().trim()
            keyManager.aliyunAppKey = binding.etAliyunAppKey.text.toString().trim()
            keyManager.aliyunToken  = binding.etAliyunToken.text.toString().trim()
            Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener { finish() }
    }
}
