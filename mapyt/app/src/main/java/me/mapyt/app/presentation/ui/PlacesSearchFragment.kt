package me.mapyt.app.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import me.mapyt.app.R
import me.mapyt.app.databinding.FragmentPlacesSearchBindingImpl
import me.mapyt.app.presentation.utils.*
import me.mapyt.app.presentation.viewmodels.MainViewModelFactory
import me.mapyt.app.presentation.viewmodels.MapPlace
import me.mapyt.app.presentation.viewmodels.PlacesSearchViewModel
import me.mapyt.app.presentation.viewmodels.PlacesSearchViewModel.SearchPlacesState
import me.mapyt.app.presentation.viewmodels.PlacesSearchViewModel.SearchPlacesState.*
import me.mapyt.app.presentation.viewmodels.UserPosition
import timber.log.Timber

class PlacesSearchFragment : Fragment(), AppFragmentBase,
    OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapClickListener {

    private val viewModel: PlacesSearchViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(activity?.application)).get(
            PlacesSearchViewModel::class.java)
    }
    private lateinit var mListener: OnPlaceSearchFragmentListener
    private lateinit var binding: FragmentPlacesSearchBindingImpl

    private lateinit var mMap: GoogleMap
    private var mUserMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_places_search,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as OnPlaceSearchFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnPlaceSearchFragmentListener")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        mMap.setOnInfoWindowClickListener(this)
        viewModel.onMapReady()
    }

    override fun onMapClick(coords: LatLng) {
        viewModel.onNewCoordsSelected(coords.latitude, coords.longitude)
    }

    override fun onInfoWindowClick(selectedMarker: Marker) {
        if (isUserMarker(selectedMarker)) return
        viewModel.onMapPlaceDetailsSelected(selectedMarker.tag.toString())
    }

    private fun setup() {
        setupMapFragment()
        setupBindings()
        viewModel.start()
    }

    private fun setupMapFragment() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupBindings() {
        viewModel.loadUserPosition.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let(::setUserPositionMarker)
        })
        viewModel.placesEvents.observe(viewLifecycleOwner, Observer(this::validatePlacesEvents))
        viewModel.navigateToPlaceDetails.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let(::navigateToDetails)
        })
    }

    private fun setUserPositionMarker(userPosition: UserPosition) {
        if (shouldShowMapError()) return
        val position = LatLng(userPosition.lat, userPosition.lng)
        mUserMarker?.let {
            it.position = position
            it.tag = userPosition.code
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, userPosition.zoomLevel))
        } ?: run {
            val markerTitle = userPosition.title ?: getString(R.string.you_are_here)
            val marker = mMap.addMarker(MarkerOptions()
                .position(position)
                .title(markerTitle)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
            marker.tag = userPosition.code
            mUserMarker = marker
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, userPosition.zoomLevel))
        }
    }

    private fun validatePlacesEvents(event: Event<SearchPlacesState>?) {
        event?.getContentIfNotHandled()?.let { state ->
            when (state) {
                is LoadPlaces -> state.run { loadMarkersWithModel(places) }
                is ShowPlacesError -> state.run {
                    MessageBar.showError(context, binding.root, error.message)
                }
                ShowLoading -> {
                    toggleProgress(binding.root, true)
                }
                HideLoading -> {
                    toggleProgress(binding.root, false)
                }
            }
        }
    }

    private fun loadMarkersWithModel(places: List<MapPlace>) {
        MessageBar.showInfo(context, binding.root, getString(R.string.found_places, places.size))
        if (shouldShowMapError()) return
        clearMapComponents()
        places.forEach { place ->
            val marker = mMap.addMarker(place.toMarkerOpts())
            marker.tag = place.code
        }
    }

    private fun navigateToDetails(place: MapPlace) {
        mListener.navigateToPlaceDetails(place)
    }

    private fun isUserMarker(marker: Marker) = mUserMarker?.tag == marker.tag ?: false

    private fun shouldShowMapError(): Boolean {
        if (this::mMap.isInitialized) return false
        Timber.e("google map instance not initialized")
        Toast.makeText(context, getString(R.string.problem_loading_map), Toast.LENGTH_LONG)
            .show()
        return true
    }

    private fun clearMapComponents() {
        mMap.clear()
        mUserMarker = null
        viewModel.onMapCleared()
    }

    interface OnPlaceSearchFragmentListener {
        fun navigateToPlaceDetails(place: MapPlace)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlacesSearchFragment()
    }
}

