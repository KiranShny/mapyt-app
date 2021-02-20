package me.mapyt.app.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.mapyt.app.R
import me.mapyt.app.presentation.base.AppActivityBase
import me.mapyt.app.presentation.base.setFragment

class PlacesSearchActivity : AppCompatActivity(), AppActivityBase {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        setFragment(PlacesSearchFragment.newInstance(), R.id.search_container_view)
    }
}