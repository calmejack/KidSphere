package com.kidsphere.game.api

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Manages API keys using [EncryptedSharedPreferences] backed by Android Keystore.
 * Keys should be provided by the app operator through the Settings screen and
 * never hard-coded or committed to source control.
 */
class ApiKeyManager(context: Context) {
    private val prefs: SharedPreferences = try {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            "api_keys_enc",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        // Fallback to regular prefs if encryption is not available (e.g. emulator quirks)
        context.getSharedPreferences("api_keys", Context.MODE_PRIVATE)
    }

    companion object {
        const val KEY_AI_API_KEY     = "ai_api_key"
        const val KEY_AI_BASE_URL    = "ai_base_url"
        const val KEY_AI_MODEL       = "ai_model"
        const val KEY_ALIYUN_APP_KEY = "aliyun_app_key"
        const val KEY_ALIYUN_TOKEN   = "aliyun_token"
    }

    var aiApiKey: String
        get() = prefs.getString(KEY_AI_API_KEY, "") ?: ""
        set(v) = prefs.edit { putString(KEY_AI_API_KEY, v) }

    var aiBaseUrl: String
        get() = prefs.getString(KEY_AI_BASE_URL, ApiConfig.AI_BASE_URL) ?: ApiConfig.AI_BASE_URL
        set(v) = prefs.edit { putString(KEY_AI_BASE_URL, v) }

    var aiModel: String
        get() = prefs.getString(KEY_AI_MODEL, "gpt-3.5-turbo") ?: "gpt-3.5-turbo"
        set(v) = prefs.edit { putString(KEY_AI_MODEL, v) }

    var aliyunAppKey: String
        get() = prefs.getString(KEY_ALIYUN_APP_KEY, "") ?: ""
        set(v) = prefs.edit { putString(KEY_ALIYUN_APP_KEY, v) }

    var aliyunToken: String
        get() = prefs.getString(KEY_ALIYUN_TOKEN, "") ?: ""
        set(v) = prefs.edit { putString(KEY_ALIYUN_TOKEN, v) }
}
