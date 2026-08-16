package com.konkuk.moru.di

import com.konkuk.moru.data.repositoryimpl.HomeUserRepositoryImpl
import com.konkuk.moru.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: HomeUserRepositoryImpl
    ): UserRepository
}
