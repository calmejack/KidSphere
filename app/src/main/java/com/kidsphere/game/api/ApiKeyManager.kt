package com.kidsphere.game.api

import android.content.Context
import androidx.core.content.edit

/**
 * Manages API keys in EncryptedSharedPreferences (or regular SharedPreferences
 * as a fallback). Keys should be provided by the app operator through the
 * Settings screen and never hard-coded or committed to source control.
 */
class ApiKeyManager(context: Context) {
    private val prefs = context.getSharedPreferences("api_keys", Context.MODE_PRIVATE)

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
