package com.my.composenews.data.push

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PushServiceModule {

    @Binds
    @Singleton
    fun bindPushServiceHandler(
        pushServiceHandlerImpl: PushServiceHandlerImpl
    ): PushServiceHandler
}