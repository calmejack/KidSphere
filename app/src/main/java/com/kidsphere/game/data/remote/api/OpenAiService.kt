package com.kidsphere.game.data.remote.api

import com.kidsphere.game.data.remote.model.ChatRequest
import com.kidsphere.game.data.remote.model.ChatResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface OpenAiService {

    @POST("v1/chat/completions")
    suspend fun chat(@Body request: ChatRequest): Response<ChatResponse>

    @POST("v1/audio/speech")
    @Streaming
    suspend fun textToSpeech(@Body request: com.kidsphere.game.data.remote.model.TtsRequest): Response<ResponseBody>
}
