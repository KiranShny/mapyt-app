package me.mapyt.app.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.mapyt.app.core.domain.entities.PlaceDetails
import me.mapyt.app.databinding.ItemSavedPlaceBinding

class SavedPlacesAdapter(private val listener: (PlaceDetails) -> Unit) :
    RecyclerView.Adapter<SavedPlacesAdapter.SavedPlaceViewHolder>() {

    private val dataList: MutableList<PlaceDetails> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedPlaceViewHolder {
        val binding =
            ItemSavedPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedPlaceViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: SavedPlaceViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun updateData(newData: List<PlaceDetails>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }

    class SavedPlaceViewHolder(
        private val binding: ItemSavedPlaceBinding,
        private val listener: (PlaceDetails) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlaceDetails) {
            binding.item = item
            binding.root.setOnClickListener {
                listener(item)
            }
        }
    }
}