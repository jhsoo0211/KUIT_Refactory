package com.konkuk.kuitproject1.di

import com.konkuk.kuitproject1.data.service.HomeService
import com.konkuk.kuitproject1.presentation.navigation.Route
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
@Provides
@Singleton
fun providesHomeService(retrofit: Retrofit):HomeService=
    retrofit.create(HomeService::class.java)

}