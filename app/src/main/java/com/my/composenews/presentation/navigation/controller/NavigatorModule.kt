package com.my.composenews.presentation.navigation.controller

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigatorModule {

    @Binds
    @Singleton
    fun bindNavigator(
        navigatorImpl: NavigatorImpl
    ): Navigator
}