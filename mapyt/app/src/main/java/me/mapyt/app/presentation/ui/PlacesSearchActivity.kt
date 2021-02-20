package me.mapyt.app.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import me.mapyt.app.R
import me.mapyt.app.presentation.base.AppActivityBase
import me.mapyt.app.presentation.base.setFragment
import me.mapyt.app.presentation.base.setupToolbar
import timber.log.Timber

class PlacesSearchActivity : AppCompatActivity(), AppActivityBase {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_search)
        setup()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_places_search, menu)
        setupSearchViewFromMenuItem(menu.findItem(R.id.searchMenuItem))
        return super.onCreateOptionsMenu(menu)
    }

    private fun setup() {
        setupToolbar(R.id.toolbar, R.string.app_name, R.string.search_places)
        setFragment(PlacesSearchFragment.newInstance(), R.id.searchContainerView)
    }

    private fun setupSearchViewFromMenuItem(searchMenuItem: MenuItem?) {
        if(searchMenuItem == null) {
            Timber.e("null searchMenuItem")
            return
        }
        val searchView = searchMenuItem.actionView as? SearchView ?: return
        with(searchView) {
            isActivated = false
        
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Timber.i("New query: %s", query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }
    }
}