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
import me.mapyt.app.databinding.ActivityPlaceDetailsBindingImpl
import me.mapyt.app.presentation.utils.*
import me.mapyt.app.presentation.viewmodels.MainViewModelFactory
import me.mapyt.app.presentation.viewmodels.MapPlace
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceMasterState
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceMasterState.*
import timber.log.Timber

class PlaceDetailsActivity : AppCompatActivity(), AppActivityBase, OnMapReadyCallback {

    companion object {
        const val PLACE_PARAM = "place"
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    private val viewModel: PlaceDetailsViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory).get(PlaceDetailsViewModel::class.java)
    }

    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY) ?: Bundle().also {
            outState.putBundle(MAPVIEW_BUNDLE_KEY, it)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }

    /*
    * Se optó por implementar de forma básica MapView en lugar de Fragment
    * para tratar de optimizar el rendimiento, es por esta razón que también se usa modo lite (ver xml)
    * TODO: mover esto a 1 fragment y aplicar más optimizaciones allí.
    * */
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
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
        val mapViewBundle = savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY)
        mapView = findViewById(R.id.details_map)
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
    }

    private fun setupSubscriptions() {
        viewModel.masterEvents.observe(this, Observer(this::validateMasterEvents))
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

    private fun showMasterInfo(master: MapPlace) {
        supportActionBar?.title = getString(R.string.place_details_title_with_name, master.name)
    }
}