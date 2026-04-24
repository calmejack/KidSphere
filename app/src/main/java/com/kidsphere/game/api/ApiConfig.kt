package com.kidsphere.game.api

object ApiConfig {
    // AI provider base URL (OpenAI-compatible API)
    // Replace with actual endpoint; defaults to official OpenAI
    const val AI_BASE_URL = "https://api.openai.com/"

    // Alibaba Cloud TTS endpoint
    const val ALIYUN_TTS_BASE_URL = "https://nls-gateway.cn-shanghai.aliyuncs.com/"

    // API keys are loaded from BuildConfig / secure storage at runtime
    // Do NOT hard-code secrets here; use ApiKeyManager
}
