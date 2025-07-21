package com.example.twocentschallenge

import com.example.twocentschallenge.api.ApiService
import com.example.twocentschallenge.repository.PostRepository
import com.example.twocentschallenge.repository.PostRepositoryImpl
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.math.BigDecimal
import java.time.Instant
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindItemRepository(
        impl: PostRepositoryImpl
    ): PostRepository
}

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.twocents.money/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()


    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(BigDecimalAdapter())
        .add(InstantAdapter())
        .build()
}

class BigDecimalAdapter {

    @FromJson
    fun fromJson(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    @ToJson
    fun toJson(value: BigDecimal?): String? {
        return value?.toPlainString()
    }
}

class InstantAdapter {

    @FromJson
    fun fromJson(value: String?): Instant? {
        return value?.let { Instant.parse(it) }
    }

    @ToJson
    fun toJson(value: Instant?): String? {
        return value?.toString()
    }
}