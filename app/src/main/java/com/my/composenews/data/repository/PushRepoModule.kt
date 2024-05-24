package com.my.composenews.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PushRepoModule {

    @Binds
    @Singleton
    fun bindPushRepo(
        pushRepositoryImpl: PushRepositoryImpl
    ): PushRepository
}