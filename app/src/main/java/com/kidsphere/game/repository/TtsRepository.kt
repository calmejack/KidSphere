package com.kidsphere.game.repository

import android.content.Context
import android.media.MediaPlayer
import com.kidsphere.game.data.remote.api.OpenAiService
import com.kidsphere.game.data.remote.model.TtsRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TtsRepository @Inject constructor(
    private val openAiService: OpenAiService,
    @ApplicationContext private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null

    suspend fun speak(text: String, voice: String = "nova"): Result<Unit> {
        return try {
            val response = openAiService.textToSpeech(TtsRequest(input = text, voice = voice))
            if (response.isSuccessful) {
                val bytes = response.body()?.bytes() ?: return Result.failure(Exception("Empty audio"))
                val file = File(context.cacheDir, "tts_${System.currentTimeMillis()}.mp3")
                file.writeBytes(bytes)
                playAudio(file.absolutePath)
                Result.success(Unit)
            } else {
                Result.failure(Exception("TTS API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun playAudio(path: String) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(path)
            prepare()
            start()
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
