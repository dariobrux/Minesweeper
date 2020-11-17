package com.dariobrux.minesweeper.di

import android.app.Activity
import com.dariobrux.minesweeper.logic.GameFactory
import com.dariobrux.minesweeper.logic.manager.TimerManager
import com.dariobrux.minesweeper.other.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ActivityContext
import org.aviran.cookiebar2.CookieBar
import javax.inject.Named
import javax.inject.Singleton

/**
 *
 * Created by Dario Bruzzese on 14/11/2020.
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

    @Provides
    @Named("shortTimer")
    fun provideTimerStartGame() = TimerManager(Constants.TIMER_COUNTDOWN)

    @Provides
    @Named("longTimer")
    fun provideTimerPlayGame() = TimerManager(Constants.TIMER_GAME)
}