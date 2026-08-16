package com.konkuk.moru.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.konkuk.moru.BuildConfig
import com.konkuk.moru.data.interceptor.AuthInterceptor
import com.konkuk.moru.data.interceptor.TokenAuthenticator
import com.konkuk.moru.data.service.AuthService
import com.konkuk.moru.data.service.FcmService
import com.konkuk.moru.data.service.ImageService
import com.konkuk.moru.data.service.InsightService
import com.konkuk.moru.data.service.MyActLogService
import com.konkuk.moru.data.service.MyActSocialService
import com.konkuk.moru.data.service.MyActTagService
import com.konkuk.moru.data.service.MyRoutineService
import com.konkuk.moru.data.service.RoutineService
import com.konkuk.moru.data.service.NotificationService
import com.konkuk.moru.data.service.OBUserService
import com.konkuk.moru.data.service.RoutineFeedService
import com.konkuk.moru.data.service.RoutineUserService
import com.konkuk.moru.data.service.SearchService
import com.konkuk.moru.data.service.SocialService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("obUserAuthed")
    fun provideOBUserServiceAuthed(@Named("jsonRetrofit") retrofit: Retrofit): OBUserService =
        retrofit.create(OBUserService::class.java)

    // [추가] 무인증용 OBUserService (닉네임 체크 전용)
    @Provides
    @Singleton
    @Named("obUserAuthless")
    fun provideOBUserServiceAuthless(@Named("authlessRetrofit") retrofit: Retrofit): OBUserService =
        retrofit.create(OBUserService::class.java)

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    // 2-2. Gson 컨버터 Retrofit (인증 포함)
    @Provides
    @Singleton
    @Named("gsonRetrofit")
    fun provideGsonRetrofit(
        baseUrl: String,
        okHttp: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    // 2-3. RoutineFeedService는 Gson Retrofit으로 제공
    @Provides
    @Singleton
    fun provideRoutineFeedService(@Named("gsonRetrofit") retrofit: Retrofit): RoutineFeedService =
        retrofit.create(RoutineFeedService::class.java)


    @Provides
    @Singleton
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
            redactHeader("Authorization")
        }
    }

    @Provides
    @Singleton
    @Named("authlessOkHttp")
    fun provideAuthlessOkHttp(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    @Named("authlessRetrofit")
    fun provideAuthlessRetrofit(
        @Named("authlessOkHttp") ok: OkHttpClient,
        baseUrl: String,
        json: Json
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(ok)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideAuthInterceptor(authInterceptor: AuthInterceptor): Interceptor = authInterceptor

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: Interceptor,
        tokenAuthenticator: TokenAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, okHttp: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttp)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    @Named("jsonRetrofit")
    fun provideJsonRetrofit(
        baseUrl: String,
        okHttp: OkHttpClient,
        json: Json
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttp)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideInsightService(retrofit: Retrofit): InsightService =
        retrofit.create(InsightService::class.java)

    @Provides
    @Singleton
    fun provideRoutineService(retrofit: Retrofit): RoutineService =
        retrofit.create(RoutineService::class.java)

    @Provides
    @Singleton
    fun provideMyRoutineService(retrofit: Retrofit): MyRoutineService =
        retrofit.create(MyRoutineService::class.java)

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): com.konkuk.moru.data.service.HomeUserService =
        retrofit.create(com.konkuk.moru.data.service.HomeUserService::class.java)


    @Provides
    @Singleton
    fun provideRoutineUserService(@Named("gsonRetrofit") retrofit: Retrofit): RoutineUserService =
        retrofit.create(RoutineUserService::class.java)


    @Provides
    @Singleton
    fun provideNotificationService(@Named("gsonRetrofit") r: Retrofit): NotificationService =
        r.create(NotificationService::class.java)

    @Provides
    @Singleton
    fun provideSocialService(@Named("gsonRetrofit") r: Retrofit): SocialService =
        r.create(SocialService::class.java)


    @Provides
    @Singleton
    fun provideSearchService(@Named("gsonRetrofit") r: Retrofit): SearchService =
        r.create(SearchService::class.java)

    @Provides
    @Singleton
    fun provideImageService(retrofit: Retrofit): ImageService =
        retrofit.create(ImageService::class.java) // [추가]

    @Singleton
    @Provides
    fun provideFcmService(retrofit: Retrofit): FcmService { // FcmService 추가
        return retrofit.create(FcmService::class.java)
    }

    @Provides @Singleton
    fun provideTagService(retrofit: Retrofit): MyActTagService =
        retrofit.create(MyActTagService::class.java)

    @Provides @Singleton
    fun provideMyActSocialService(retrofit: Retrofit): MyActSocialService =
        retrofit.create(MyActSocialService::class.java)

    @Provides @Singleton
    fun provideMyActLogService(retrofit: Retrofit): MyActLogService =
        retrofit.create(MyActLogService::class.java)

    @Provides
    @Singleton
    fun provideCreateRoutineService(retrofit: Retrofit): com.konkuk.moru.data.service.CreateRoutineService =
        retrofit.create(com.konkuk.moru.data.service.CreateRoutineService::class.java)

    @Provides
    @Singleton
    fun provideCRImageService(
        retrofit: Retrofit
    ): com.konkuk.moru.data.service.CRImageService =
        retrofit.create(com.konkuk.moru.data.service.CRImageService::class.java)

    @Provides
    @Singleton
    fun provideMyActUserService(
        retrofit: Retrofit
    ): com.konkuk.moru.data.service.MyActUserService =
        retrofit.create(com.konkuk.moru.data.service.MyActUserService::class.java)



}
