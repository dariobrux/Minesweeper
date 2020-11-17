package com.dariobrux.minesweeper

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree


/**
 *
 * Created by Dario Bruzzese on 14/11/2020.
 *
 * This is the application class declared in Manifest.
 *
 */
@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Init Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}