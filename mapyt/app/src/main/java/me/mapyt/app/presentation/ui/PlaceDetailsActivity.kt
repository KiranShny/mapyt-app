package me.mapyt.app.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.mapyt.app.R
import me.mapyt.app.presentation.utils.AppActivityBase
import me.mapyt.app.presentation.utils.applyBackButtonDefaultBehavior
import me.mapyt.app.presentation.utils.enableBackNavigation
import me.mapyt.app.presentation.utils.setupToolbar
import me.mapyt.app.presentation.viewmodels.MapPlace
import timber.log.Timber

class PlaceDetailsActivity : AppCompatActivity(), AppActivityBase {

    companion object {
        const val PLACE_PARAM = "place"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)
        setup()
    }

    override fun onSupportNavigateUp() =
        applyBackButtonDefaultBehavior(super.onSupportNavigateUp())

    private fun setup() {
        val currentPlace = intent.getParcelableExtra<MapPlace>(PLACE_PARAM)
        Timber.d(currentPlace.toString())

        val title = currentPlace?.name ?: getString(R.string.app_name)
        setupToolbar(R.id.toolbar, title)
        enableBackNavigation(true)
    }
}