package me.mapyt.app.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import me.mapyt.app.R
import me.mapyt.app.core.domain.entities.PlaceDetails
import me.mapyt.app.databinding.ActivityPlaceDetailsBinding
import me.mapyt.app.presentation.adapters.PlaceReviewsAdapter
import me.mapyt.app.presentation.exceptions.ViewModelOperationException
import me.mapyt.app.presentation.utils.*
import me.mapyt.app.presentation.viewmodels.MainViewModelFactory
import me.mapyt.app.presentation.viewmodels.MapPlace
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.*
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceDetailsState.*
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceMasterState.*
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.SavedPlaceState.*
import timber.log.Timber

class PlaceDetailsActivity : AppCompatActivity(), AppActivityBase, OnMapReadyCallback {

    companion object {
        const val PLACE_PARAM = "place"
    }

    private val viewModel: PlaceDetailsViewModel by lazy {
        ViewModelProvider(this,
            MainViewModelFactory(application)).get(PlaceDetailsViewModel::class.java)
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityPlaceDetailsBinding
    private lateinit var reviewsAdapter: PlaceReviewsAdapter

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

        setupToolbar(R.id.toolbar, R.string.place_details_title)
        enableBackNavigation(true)

        reviewsAdapter = PlaceReviewsAdapter { model ->
            Timber.d(model.toString())
        }
        binding.rvReviews.apply {
            adapter = reviewsAdapter
            addItemDecoration(DividerItemDecoration(this@PlaceDetailsActivity,
                DividerItemDecoration.VERTICAL))
        }
        binding.fabSave.setOnClickListener {
            viewModel.trySavePlace()
        }

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
        viewModel.saveEvents.observe(this, Observer(this::validateSaveEvents))
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
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
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
                ShowLoadingDetails -> binding.pgReviews.shouldBeVisible(true)
                HideLoadingDetails -> binding.pgReviews.shouldBeVisible(false, forceToGone = true)
            }
        }
    }

    private fun validateSaveEvents(event: Event<SavedPlaceState>?) {
        event?.getContentIfNotHandled()?.let { state ->
            when (state) {
                HideSavingPlace -> binding.fabSave.isEnabled = true
                ShowSavingPlace -> binding.fabSave.isEnabled = false
                is OnPlaceSaved -> state.run {
                    MessageBar.showSuccess(this@PlaceDetailsActivity,
                        binding.root,
                        getString(R.string.saved_place))
                }
                is OnPlaceDeleted -> {
                    MessageBar.showInfo(this@PlaceDetailsActivity,
                        binding.root,
                        getString(R.string.deleted_place))
                }
                is OnSavePlaceError -> state.run {
                    var errorMessage = getString(R.string.something_went_wrong)
                    if (error is ViewModelOperationException) {
                        errorMessage = getString(error.messageId)
                    }
                    MessageBar.showError(this@PlaceDetailsActivity, binding.root, errorMessage)
                }
            }
        }
    }

    private fun showMasterInfo(master: MapPlace) {
        supportActionBar?.title = getString(R.string.place_details_title_with_name, master.name)
    }

    private fun showDetailsInfo(details: PlaceDetails) {
        if (details.reviews.isEmpty()) {
            MessageBar.showInfo(this, binding.root, getString(R.string.no_reviews))
            return
        }
        reviewsAdapter.updateData(details.reviews)
    }
}