package com.example.kuitandroidapiexample.di

import com.example.kuitandroidapiexample.data.service.AnimalService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun providesAnimalService(retrofit: Retrofit): AnimalService =
        retrofit.create(
            AnimalService::class.java
        )
}