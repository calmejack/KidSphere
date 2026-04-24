package com.kidsphere.game.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AliyunTtsApiService {
    @POST("stream/v1/tts")
    @Headers("Content-Type: application/json")
    suspend fun synthesize(
        @Header("Authorization") token: String,
        @Body body: Map<String, Any>
    ): Response<ResponseBody>
}
