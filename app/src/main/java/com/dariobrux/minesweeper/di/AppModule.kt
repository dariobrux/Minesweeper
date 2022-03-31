package com.dariobrux.minesweeper.di

import com.dariobrux.minesweeper.logic.GameFactory
import com.dariobrux.minesweeper.logic.manager.TimerManager
import com.dariobrux.minesweeper.other.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 *
 * Created by Dario Bruzzese on 14/11/2020.
 *
 * This singleton object is a bucket from where we will get all the dependencies from.
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGameFactory() = GameFactory()

    @Singleton
    @Provides
    @ShortTimer
    fun provideTimerStartGame() : TimerManager = TimerManager(Constants.TIMER_COUNTDOWN)

    @Singleton
    @Provides
    @LongTimer
    fun provideTimerPlayGame() : TimerManager = TimerManager(Constants.TIMER_GAME)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ShortTimer

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LongTimer