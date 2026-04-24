package com.kidsphere.game.di

import android.content.Context
import androidx.room.Room
import com.kidsphere.game.BuildConfig
import com.kidsphere.game.data.local.KidSphereDatabase
import com.kidsphere.game.data.local.dao.GameDao
import com.kidsphere.game.data.local.dao.NpcConversationDao
import com.kidsphere.game.data.remote.api.OpenAiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val OPENAI_BASE_URL = "https://api.openai.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(OPENAI_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenAiService(retrofit: Retrofit): OpenAiService {
        return retrofit.create(OpenAiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KidSphereDatabase {
        return Room.databaseBuilder(
            context,
            KidSphereDatabase::class.java,
            KidSphereDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideGameDao(database: KidSphereDatabase): GameDao = database.gameDao()

    @Provides
    @Singleton
    fun provideNpcConversationDao(database: KidSphereDatabase): NpcConversationDao =
        database.npcConversationDao()
}
