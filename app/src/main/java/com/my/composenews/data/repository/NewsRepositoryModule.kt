package com.my.composenews.data.repository

import com.my.composenews.data.service.ApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NewsRepositoryModule {

    @Binds
    @Singleton
    fun provideRepository(
        repositoryImpl: NewsRepositoryImpl
    ): NewsRepository
}