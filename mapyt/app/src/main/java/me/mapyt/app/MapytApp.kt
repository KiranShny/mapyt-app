package me.mapyt.app

import android.app.Application
import com.squareup.picasso.Picasso
import timber.log.Timber

class MapytApp : Application() {

    lateinit var dbContainer: DbContainer

    override fun onCreate() {
        super.onCreate()
        setupLogger()
        setupContainers()
        setupImageLoader()
    }

    private fun setupContainers() {
        dbContainer = DbContainer(this)
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