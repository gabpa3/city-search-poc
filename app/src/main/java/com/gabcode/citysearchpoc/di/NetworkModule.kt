package com.gabcode.citysearchpoc.di

import com.gabcode.citysearchpoc.data.service.CityService
import com.gabcode.citysearchpoc.data.service.UrlProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addNetworkInterceptor(logger)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okhttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(UrlProvider.baseUrl)
            .client(okhttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    fun provideCityService(retrofit: Retrofit): CityService =
        retrofit.create(CityService::class.java)

}
