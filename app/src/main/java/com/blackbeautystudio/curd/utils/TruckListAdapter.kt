package com.blackbeautystudio.curd.utils

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.blackbeautystudio.curd.R
import com.blackbeautystudio.curd.databinding.ItemTruckBinding
import com.blackbeautystudio.curd.model.Truck
import com.blackbeautystudio.curd.ui.TruckViewModel

class TruckListAdapter : RecyclerView.Adapter<TruckListAdapter.ViewHolder>() {
    private lateinit var truckList: List<Truck>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.item_truck, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(truckList[position])

    override fun getItemCount() = if (::truckList.isInitialized) truckList.size else 0

    fun updateTruckList(truckList: List<Truck>) {
        this.truckList = truckList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemTruckBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(truck: Truck) {
            binding.viewModel = TruckViewModel(truck)
        }
    }
}