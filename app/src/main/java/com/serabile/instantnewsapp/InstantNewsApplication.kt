package com.serabile.instantnewsapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class InstantNewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Init Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
