package me.mapyt.app.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.mapyt.app.core.domain.entities.PlaceReview
import me.mapyt.app.databinding.ItemPlaceReviewBinding
import me.mapyt.app.databinding.ItemPlaceReviewBindingImpl

class PlaceReviewsAdapter(private val listener: (PlaceReview) -> Unit) :
    RecyclerView.Adapter<PlaceReviewsAdapter.PlaceReviewViewHolder>() {

    private val dataList: MutableList<PlaceReview> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceReviewViewHolder {
        val binding =
            ItemPlaceReviewBindingImpl.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceReviewViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PlaceReviewViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size

    fun updateData(newData: List<PlaceReview>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }

    class PlaceReviewViewHolder(
        private val binding: ItemPlaceReviewBinding,
        private val listener: (PlaceReview) -> Unit,
    ) : RecyclerView.ViewHolder(
        binding.root) {

        fun bind(item: PlaceReview){
            binding.item = item
            binding.root.setOnClickListener {
                listener(item)
            }
        }
    }
}