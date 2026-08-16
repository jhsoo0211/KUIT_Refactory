package com.konkuk.moru.di

import com.konkuk.moru.data.service.ImageUploadService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageUploadModule {
    @Provides
    @Singleton
    fun provideImageUploadService(
        retrofit: Retrofit // ★ 무자격 공용 Retrofit 주입 (NetworkModule.provideRetrofit)
    ): ImageUploadService = retrofit.create(ImageUploadService::class.java)
}