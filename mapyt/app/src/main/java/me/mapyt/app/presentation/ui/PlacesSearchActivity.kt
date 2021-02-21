package me.mapyt.app.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import me.mapyt.app.R
import me.mapyt.app.presentation.utils.AppActivityBase
import me.mapyt.app.presentation.utils.setFragment
import me.mapyt.app.presentation.utils.setupToolbar

class PlacesSearchActivity : AppCompatActivity(), AppActivityBase {

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

}