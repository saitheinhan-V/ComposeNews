package com.my.composenews.data.agora

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AgoraModule {

    @Binds
    @Singleton
    fun bindAgoraHandler(
        agoraHandlerImpl: AgoraHandlerImpl
    ): AgoraHandler
}