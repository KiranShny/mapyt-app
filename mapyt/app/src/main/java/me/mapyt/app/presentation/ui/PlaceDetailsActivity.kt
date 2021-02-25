package me.mapyt.app.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import me.mapyt.app.R
import me.mapyt.app.core.domain.entities.PlaceDetails
import me.mapyt.app.databinding.ActivityPlaceDetailsBindingImpl
import me.mapyt.app.presentation.utils.*
import me.mapyt.app.presentation.viewmodels.MainViewModelFactory
import me.mapyt.app.presentation.viewmodels.MapPlace
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceDetailsState
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceDetailsState.*
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceMasterState
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceMasterState.*
import timber.log.Timber

class PlaceDetailsActivity : AppCompatActivity(), AppActivityBase, OnMapReadyCallback {

    companion object {
        const val PLACE_PARAM = "place"
    }

    private val viewModel: PlaceDetailsViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory).get(PlaceDetailsViewModel::class.java)
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityPlaceDetailsBindingImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup(savedInstanceState)
    }

    override fun onSupportNavigateUp() =
        applyBackButtonDefaultBehavior(super.onSupportNavigateUp())

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        setupMapBinding(mMap)
    }

    private fun setup(savedInstanceState: Bundle?) {
        setupBinding()
        setupMapView(savedInstanceState)
        setupSubscriptions()

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
    }

    private fun setupMapView(savedInstanceState: Bundle?) {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.detailsMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupSubscriptions() {
        viewModel.masterEvents.observe(this, Observer(this::validateMasterEvents))
        viewModel.detailsEvents.observe(this, Observer(this::validateDetailsEvents))
    }

    private fun setupMapBinding(map: GoogleMap) {
        viewModel.placeMaster.observe(this, { masterValue ->
            masterValue?.let { master -> setCoordsInMap(map, master) }
        })
    }

    private fun setCoordsInMap(map: GoogleMap, master: MapPlace) {
        val position = LatLng(master.lat, master.lng)
        map.addMarker(MarkerOptions()
            .position(position)
            .title(getString(R.string.place_location))
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        )
        //TODO: obtener zoom desde vmodel
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10.0f))
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

    private fun validateDetailsEvents(event: Event<PlaceDetailsState>?) {
        event?.getContentIfNotHandled()?.let { state ->
            when (state) {
                is LoadDetails -> state.run { showDetailsInfo(details) }
                is ShowDetailsError -> state.run {
                    MessageBar.showError(this@PlaceDetailsActivity, binding.root, error.message)
                }
                HideLoadingDetails -> MessageBar.showInfo(this@PlaceDetailsActivity,
                    binding.root,
                    getString(R.string.loading_reviews))
                ShowLoadingDetails -> {
                }
            }
        }
    }

    private fun showMasterInfo(master: MapPlace) {
        supportActionBar?.title = getString(R.string.place_details_title_with_name, master.name)
    }

    private fun showDetailsInfo(details: PlaceDetails) {
        Timber.d(details.toString())
    }
}