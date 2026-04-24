package com.kidsphere.game.repository

import android.content.Context
import android.media.MediaPlayer
import com.kidsphere.game.api.ApiKeyManager
import com.kidsphere.game.api.RetrofitClient
import java.io.File
import java.io.FileOutputStream

class TtsRepository(
    private val context: Context,
    private val keyManager: ApiKeyManager
) {
    private var mediaPlayer: MediaPlayer? = null

    /**
     * Synthesize text to speech using Alibaba Cloud NLS TTS and play it.
     */
    suspend fun speakText(text: String, voiceName: String = "Aixia"): Result<Unit> {
        return try {
            val service = RetrofitClient.buildTtsService()
            val body = mapOf<String, Any>(
                "appkey" to keyManager.aliyunAppKey,
                "text" to text,
                "token" to keyManager.aliyunToken,
                "format" to "mp3",
                "voice" to voiceName,
                "sample_rate" to 16000
            )
            val response = service.synthesize(
                token = keyManager.aliyunToken,
                body = body
            )
            if (response.isSuccessful) {
                val bytes = response.body()?.bytes() ?: return Result.failure(Exception("Empty TTS response"))
                val tmpFile = File(context.cacheDir, "tts_${System.currentTimeMillis()}.mp3")
                FileOutputStream(tmpFile).use { it.write(bytes) }
                playAudio(tmpFile)
                Result.success(Unit)
            } else {
                Result.failure(Exception("TTS API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun playAudio(file: File) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            prepare()
            start()
            setOnCompletionListener {
                it.release()
                file.delete()
            }
        }
    }

    fun stopSpeaking() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
