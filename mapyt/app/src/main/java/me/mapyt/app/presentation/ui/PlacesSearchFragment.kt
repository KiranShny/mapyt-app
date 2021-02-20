package me.mapyt.app.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import me.mapyt.app.R

class PlacesSearchFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_places_search, container, false)
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
        val defaultPosition = LatLng(12.129210669854233, -86.26649843639939)
        with(mMap) {
            addMarker(MarkerOptions().position(defaultPosition).title("¡Hola! ¿qué estás buscando?"))
            moveCamera(CameraUpdateFactory.newLatLngZoom(defaultPosition, 16.0F))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlacesSearchFragment()
    }

}