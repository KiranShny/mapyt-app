package me.mapyt.app.presentation.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import me.mapyt.app.R
import me.mapyt.app.presentation.utils.*
import me.mapyt.app.presentation.viewmodels.MapPlace

class PlacesSearchActivity : AppCompatActivity(), AppActivityBase,
    PlacesSearchFragment.OnPlaceSearchFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_search)
        setup()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_places_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setup() {
        setupToolbar(R.id.toolbar, R.string.app_name, R.string.search_places)
        setFragment(PlacesSearchFragment.newInstance(), R.id.searchContainerView)
    }

    override fun navigateToPlaceDetails(place: MapPlace) {
        hideKeyboard()
        startActivity<PlaceDetailsActivity> {
            putExtra(PlaceDetailsActivity.PLACE_PARAM, place)
        }
    }

}