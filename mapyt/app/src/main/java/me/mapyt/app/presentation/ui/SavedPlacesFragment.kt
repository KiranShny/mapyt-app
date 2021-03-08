package me.mapyt.app.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.mapyt.app.R
import me.mapyt.app.databinding.FragmentSavedPlacesBinding
import me.mapyt.app.presentation.utils.AppFragmentBase
import me.mapyt.app.presentation.viewmodels.MainViewModelFactory
import me.mapyt.app.presentation.viewmodels.PlacesSearchViewModel
import me.mapyt.app.presentation.viewmodels.SavedPlacesViewModel

class SavedPlacesFragment : Fragment(), AppFragmentBase {

    private lateinit var binding: FragmentSavedPlacesBinding

    private val viewModel: SavedPlacesViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(activity?.application)).get(
            SavedPlacesViewModel::class.java)
    }

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

    private fun setup() {

    }

    companion object {
        @JvmStatic
        fun newInstance() = SavedPlacesFragment()
    }
}