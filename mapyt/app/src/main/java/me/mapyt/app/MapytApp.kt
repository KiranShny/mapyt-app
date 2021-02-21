package me.mapyt.app

import android.app.Application
import com.squareup.picasso.Picasso
import timber.log.Timber

class MapytApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupLogger()
        setupImageLoader()
    }

    private fun setupLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupImageLoader() {
        Picasso.get().isLoggingEnabled = BuildConfig.DEBUG
    }
}