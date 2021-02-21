package me.mapyt.app.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import me.mapyt.app.R
import timber.log.Timber

class PlacesSearchFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mSearchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_places_search, container, false)
        setupSearchView(rootView)
        return rootView
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
            addMarker(MarkerOptions().position(defaultPosition)
                .title("¡Hola! ¿qué estás buscando?"))
            moveCamera(CameraUpdateFactory.newLatLngZoom(defaultPosition, 16.0F))
        }
    }

    private fun setupSearchView(rootView: View) {
        //TODO: usar binding
        mSearchView = rootView.findViewById(R.id.svPlaces)
        with(mSearchView) {
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

    companion object {
        @JvmStatic
        fun newInstance() = PlacesSearchFragment()
    }

}