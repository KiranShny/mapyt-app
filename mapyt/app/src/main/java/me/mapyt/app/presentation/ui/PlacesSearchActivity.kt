package me.mapyt.app.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.mapyt.app.R
import me.mapyt.app.presentation.base.AppActivityBase
import me.mapyt.app.presentation.base.setFragment
import me.mapyt.app.presentation.base.setupToolbar

class PlacesSearchActivity : AppCompatActivity(), AppActivityBase {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_search)
        setup()
    }

    private fun setup() {
        setupToolbar(R.id.toolbar, R.string.app_name, R.string.search_places)
        setFragment(PlacesSearchFragment.newInstance(), R.id.searchContainerView)
    }
}