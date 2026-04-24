package com.kidsphere.game.api.model

data class AliyunTtsRequest(
    val text: String,
    val voice: String = "Aixia",    // child-friendly voice
    val format: String = "mp3",
    val sample_rate: Int = 16000,
    val volume: Int = 50,
    val speech_rate: Int = 0,
    val pitch_rate: Int = 0
)

data class AliyunTtsTokenResponse(
    val Token: AliyunTtsTokenInfo?,
    val RequestId: String?
)

data class AliyunTtsTokenInfo(
    val Id: String,
    val ExpireTime: Long
)
