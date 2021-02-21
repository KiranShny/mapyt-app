package me.mapyt.app.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
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
import timber.log.Timber

class PlacesSearchFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: PlacesSearchViewModel by lazy {
        ViewModelProvider(this).get(PlacesSearchViewModel::class.java)
    }
    private lateinit var binding: FragmentPlacesSearchBindingImpl

    private lateinit var mMap: GoogleMap
    private lateinit var mSearchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        setupMapFragment()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setupMapComponent()
    }

    private fun setupMapFragment() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupMapComponent() {
        //TODO: mover a viewmodel
        val defaultPosition = LatLng(12.129210669854233, -86.26649843639939)
        with(mMap) {
            addMarker(MarkerOptions().position(defaultPosition)
                .title("¡Hola! ¿qué estás buscando?"))
            moveCamera(CameraUpdateFactory.newLatLngZoom(defaultPosition, 16.0F))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlacesSearchFragment()
    }

}