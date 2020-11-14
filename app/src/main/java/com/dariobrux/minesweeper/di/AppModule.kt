package com.dariobrux.minesweeper.di

import com.dariobrux.minesweeper.logic.GameFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGameFactory() = GameFactory()
}