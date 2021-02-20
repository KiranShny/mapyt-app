package me.mapyt.app

import android.app.Application
import timber.log.Timber

class MapytApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupLogger()
    }

    private fun setupLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}