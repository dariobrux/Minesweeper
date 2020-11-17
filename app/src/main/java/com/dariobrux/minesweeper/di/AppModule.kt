package com.dariobrux.minesweeper.di

import com.dariobrux.minesweeper.logic.GameFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 *
 * Created by Dario Bruzzese on 17/11/2020.
 *
 * This singleton object is a bucket from where we will get all the dependencies from.
 *
 */
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGameFactory() = GameFactory()
}