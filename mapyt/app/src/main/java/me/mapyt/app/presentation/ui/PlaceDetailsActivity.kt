package me.mapyt.app.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import me.mapyt.app.R
import me.mapyt.app.databinding.ActivityPlaceDetailsBindingImpl
import me.mapyt.app.presentation.utils.*
import me.mapyt.app.presentation.viewmodels.MainViewModelFactory
import me.mapyt.app.presentation.viewmodels.MapPlace
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceMasterState
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceMasterState.*
import timber.log.Timber

class PlaceDetailsActivity : AppCompatActivity(), AppActivityBase {

    companion object {
        const val PLACE_PARAM = "place"
    }

    private val viewModel: PlaceDetailsViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory).get(PlaceDetailsViewModel::class.java)
    }

    private lateinit var binding: ActivityPlaceDetailsBindingImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)
        setup()
    }

    override fun onSupportNavigateUp() =
        applyBackButtonDefaultBehavior(super.onSupportNavigateUp())

    private fun setup() {
        setupBinding()

        val currentPlace = intent.getParcelableExtra<MapPlace>(PLACE_PARAM)
        Timber.d(currentPlace.toString())

        setupToolbar(R.id.toolbar, R.string.place_details_title)
        enableBackNavigation(true)

        viewModel.start(currentPlace)
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_details)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        viewModel.masterEvents.observe(this, Observer(this::validateMasterEvents))
    }

    private fun validateMasterEvents(event: Event<PlaceMasterState>?) {
        event?.getContentIfNotHandled()?.let { state ->
            when (state) {
                is LoadMaster -> state.run { showMasterInfo(master) }
                is ShowMasterError -> state.run {
                    MessageBar.showError(this@PlaceDetailsActivity, binding.root, error.message)
                }
            }
        }
    }

    private fun showMasterInfo(master: MapPlace) {
        supportActionBar?.title = getString(R.string.place_details_title_with_name, master.name)
    }
}