package me.mapyt.app.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import me.mapyt.app.R
import me.mapyt.app.databinding.FragmentSavedPlacesBinding
import me.mapyt.app.presentation.adapters.SavedPlacesAdapter
import me.mapyt.app.presentation.utils.AppFragmentBase
import me.mapyt.app.presentation.utils.Event
import me.mapyt.app.presentation.utils.MessageBar
import me.mapyt.app.presentation.utils.shouldBeVisible
import me.mapyt.app.presentation.viewmodels.MainViewModelFactory
import me.mapyt.app.presentation.viewmodels.MapPlace
import me.mapyt.app.presentation.viewmodels.SavedPlacesViewModel
import me.mapyt.app.presentation.viewmodels.SavedPlacesViewModel.SavedPlacesState
import me.mapyt.app.presentation.viewmodels.SavedPlacesViewModel.SavedPlacesState.*
import timber.log.Timber

class SavedPlacesFragment : Fragment(), AppFragmentBase {

    private val viewModel: SavedPlacesViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(activity?.application)).get(
            SavedPlacesViewModel::class.java)
    }

    private lateinit var binding: FragmentSavedPlacesBinding
    private lateinit var placesAdapter: SavedPlacesAdapter
    private lateinit var savedPlacesListener: SavedPlacesListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_saved_places,
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
            savedPlacesListener = context as SavedPlacesListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement SavedPlacesListener")
        }
    }

    private fun setup() {
        placesAdapter = SavedPlacesAdapter { place ->
            Timber.d(place.toString())
        }
        binding.rvSavedPlaces.apply {
            adapter = placesAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        setupSubscriptions()
        viewModel.start()
    }

    private fun setupSubscriptions() {
        viewModel.placesEvents.observe(viewLifecycleOwner, Observer(this::validatePlaceEvents))
    }

    private fun validatePlaceEvents(event: Event<SavedPlacesState>?) {
        event?.getContentIfNotHandled()?.let { state ->
            when (state) {
                is LoadPlaces -> {
                    if(state.places.isEmpty()) {
                        MessageBar.showInfo(context, binding.root, getString(R.string.no_saved_places))
                        return
                    }
                    placesAdapter.updateData(state.places)
                }
                is ShowPlacesError -> MessageBar.showError(context, binding.root, state.error.message)
                is LoadingPlaces -> binding.pgPlaces.shouldBeVisible(state.isLoading, true)
            }
        }
    }

    private fun navigateToDetails(place: MapPlace) {
        savedPlacesListener.navigateToDetails(place)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SavedPlacesFragment()
    }

    interface SavedPlacesListener {
        fun navigateToDetails(place: MapPlace)
    }

}