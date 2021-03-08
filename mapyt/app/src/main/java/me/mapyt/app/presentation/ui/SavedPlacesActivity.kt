package me.mapyt.app.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.mapyt.app.R
import me.mapyt.app.presentation.utils.*
import me.mapyt.app.presentation.viewmodels.MapPlace

class SavedPlacesActivity : AppCompatActivity(), AppActivityBase,
    SavedPlacesFragment.SavedPlacesListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_places)
        setup()
    }

    override fun onSupportNavigateUp() =
        applyBackButtonDefaultBehavior(super.onSupportNavigateUp())

    private fun setup() {
        setupToolbar(R.id.toolbar, R.string.saved_places)
        enableBackNavigation(true)
        setFragment(SavedPlacesFragment.newInstance(), R.id.fcvSavedPlaces)
    }
    
    override fun navigateToDetails(place: MapPlace) {
        startActivity<PlaceDetailsActivity> {
            putExtra(PlaceDetailsActivity.PLACE_PARAM, place)
        }
    }
}