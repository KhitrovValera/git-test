package com.example.gittest.di

import android.content.Context
import android.content.SharedPreferences
import com.example.gittest.data.local.KeyValueStorage
import com.example.gittest.data.remote.api.GitHubService
import com.example.gittest.data.remote.api.AuthInterceptor
import com.example.gittest.data.remote.result.ApiCaller
import com.example.gittest.data.remote.source.GitRemoteDataSource
import com.example.gittest.data.remote.source.impl.GitRemoteDataSourceImpl
import com.example.gittest.data.repository.AppRepositoryImpl
import com.example.gittest.domain.repository.AppRepository
import com.example.gittest.domain.useCase.GetReadMeUseCase
import com.example.gittest.domain.useCase.GetRepoDetailsUseCase
import com.example.gittest.domain.useCase.GetRepoPageUseCase
import com.example.gittest.domain.useCase.GetReposUseCase
import com.example.gittest.domain.useCase.SignInUseCase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val contentType = "application/json".toMediaType()

    @Provides
    @Singleton
    fun provideJson() = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(KeyValueStorage.PREFS_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideKeyValueStorage(prefs: SharedPreferences) = KeyValueStorage(prefs)

    @Provides
    @Singleton
    fun provideAuthInterceptor(storage: KeyValueStorage): AuthInterceptor {
        return AuthInterceptor(storage)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @OptIn(InternalSerializationApi::class)
    @Provides
    @Singleton
    fun provide(
        json: Json,
        okHttpClient: OkHttpClient
    ): GitHubService {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
            .create(GitHubService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiCaller(): ApiCaller {
        return ApiCaller()
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        gitHubService: GitHubService,
        apiCaller: ApiCaller
    ): GitRemoteDataSource {
        return GitRemoteDataSourceImpl(gitHubService, apiCaller)
    }

    @Provides
    @Singleton
    fun provideAppRepository(
        gitRemoteDataSource: GitRemoteDataSource
    ): AppRepository {
        return AppRepositoryImpl(gitRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideSignInUseCase(
        appRepository: AppRepository
    ) : SignInUseCase {
        return SignInUseCase(appRepository)
    }

    @Provides
    @Singleton
    fun provideGetReposUseCase(
        appRepository: AppRepository
    ) : GetReposUseCase {
        return GetReposUseCase(appRepository)
    }


    @Provides
    @Singleton
    fun provideGetRepoDetailsUseCase(
        appRepository: AppRepository
    ): GetRepoDetailsUseCase {
        return GetRepoDetailsUseCase(appRepository)
    }

    @Provides
    @Singleton
    fun provideGetReadMeUseCase(
        appRepository: AppRepository
    ) : GetReadMeUseCase {
        return GetReadMeUseCase(appRepository)
    }

    @Provides
    @Singleton
    fun provideGetRepoPageUseCase(
        getRepoDetailsUseCase: GetRepoDetailsUseCase,
        getReadMeUseCase: GetReadMeUseCase
    ) : GetRepoPageUseCase {
        return GetRepoPageUseCase(
            getRepoDetailsUseCase,
            getReadMeUseCase
        )
    }
}