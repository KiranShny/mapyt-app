package me.mapyt.app.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import me.mapyt.app.R
import me.mapyt.app.databinding.FragmentPlacesSearchBindingImpl
import me.mapyt.app.presentation.viewmodels.PlacesSearchViewModel
import me.mapyt.app.presentation.viewmodels.UserPosition
import timber.log.Timber

class PlacesSearchFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: PlacesSearchViewModel by lazy {
        ViewModelProvider(this).get(PlacesSearchViewModel::class.java)
    }
    private lateinit var binding: FragmentPlacesSearchBindingImpl

    private lateinit var mMap: GoogleMap

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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        viewModel.onMapReady()
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
        viewModel.loadDefaultPosition.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let(::setupMapComponentWithModel)
        })
    }

    private fun setupMapComponentWithModel(userPosition: UserPosition) {
        if (shouldShowMapError()) return
        val position = LatLng(userPosition.lat, userPosition.lng)
        with(mMap) {
            addMarker(MarkerOptions().position(position)
                .title(getString(userPosition.messageId)))
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, userPosition.zoomLevel))
        }
    }

    private fun shouldShowMapError(): Boolean {
        if (this::mMap.isInitialized) return false
        Timber.e("google map instance not initialized")
        Toast.makeText(context, getString(R.string.problem_loading_map), Toast.LENGTH_LONG)
            .show()
        return true
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlacesSearchFragment()
    }

}