package com.konkuk.moru.di

import com.konkuk.moru.data.repositoryimpl.AuthRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.CRImageRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.CreateRoutineRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.FcmRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.InsightRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.MyActRecordRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.MyActScrapRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.MyActTagRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.MyRoutineRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.NotificationRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.OBUserRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.RoutineFeedRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.RoutineUserRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.SearchRepositoryImpl
import com.konkuk.moru.data.repositoryimpl.SocialRepositoryImpl
import com.konkuk.moru.domain.repository.AuthRepository
import com.konkuk.moru.domain.repository.CRImageRepository
import com.konkuk.moru.domain.repository.CreateRoutineRepository
import com.konkuk.moru.domain.repository.FcmRepository
import com.konkuk.moru.domain.repository.InsightRepository
import com.konkuk.moru.domain.repository.MyActRecordRepository
import com.konkuk.moru.domain.repository.MyActScrapRepository
import com.konkuk.moru.domain.repository.MyActTagRepository
import com.konkuk.moru.domain.repository.MyRoutineRepository
import com.konkuk.moru.domain.repository.NotificationRepository
import com.konkuk.moru.domain.repository.OBUserRepository
import com.konkuk.moru.domain.repository.RoutineFeedRepository
import com.konkuk.moru.domain.repository.RoutineUserRepository
import com.konkuk.moru.domain.repository.SearchRepository
import com.konkuk.moru.domain.repository.SocialRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindOBUserRepository(impl: OBUserRepositoryImpl): OBUserRepository

    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds @Singleton
    abstract fun bindInsightRepository(impl: InsightRepositoryImpl): InsightRepository

    @Binds @Singleton
    abstract fun bindRoutineFeedRepository(impl: RoutineFeedRepositoryImpl): RoutineFeedRepository

    @Binds @Singleton
    abstract fun bindRoutineUserRepository(impl: RoutineUserRepositoryImpl): RoutineUserRepository

    @Binds @Singleton
    abstract fun bindMyRoutineRepository(impl: MyRoutineRepositoryImpl): MyRoutineRepository

    @Binds @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository

    @Binds @Singleton
    abstract fun bindSocialRepository(impl: SocialRepositoryImpl): SocialRepository

    @Binds @Singleton
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds @Singleton
    abstract fun bindMyActTagRepository(impl: MyActTagRepositoryImpl): MyActTagRepository

    @Binds @Singleton
    abstract fun bindMyActScrapRepository(impl: MyActScrapRepositoryImpl): MyActScrapRepository

    @Binds @Singleton
    abstract fun bindMyActRecordRepository(impl: MyActRecordRepositoryImpl): MyActRecordRepository

    @Binds @Singleton
    abstract fun bindFcmRepository(
        fcmRepositoryImpl: FcmRepositoryImpl
    ): FcmRepository

    @Binds @Singleton
    abstract fun bindCreateRoutineRepository(
        impl: CreateRoutineRepositoryImpl
    ): CreateRoutineRepository

    @Binds @Singleton
    abstract fun bindCRImageRepository(impl: CRImageRepositoryImpl): CRImageRepository
}