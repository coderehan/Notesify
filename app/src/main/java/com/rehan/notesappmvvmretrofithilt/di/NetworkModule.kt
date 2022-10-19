package com.rehan.notesappmvvmretrofithilt.di

import com.rehan.notesappmvvmretrofithilt.api.AuthInterceptor
import com.rehan.notesappmvvmretrofithilt.api.NotesAPI
import com.rehan.notesappmvvmretrofithilt.api.UserAPI
import com.rehan.notesappmvvmretrofithilt.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)   // SingletonComponent describes retrofit instance is created at application level
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    // In UserAPI, we don't need token. So we don't need to add client method here
    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): UserAPI {
        return retrofitBuilder.build().create(UserAPI::class.java)
    }

    // In NotesAPI, we need token. So here we will add client method and pass okHttpClient object to it
    @Singleton
    @Provides
    fun providesNotesAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): NotesAPI {
        return retrofitBuilder.client(okHttpClient).build().create(NotesAPI::class.java)
    }
}