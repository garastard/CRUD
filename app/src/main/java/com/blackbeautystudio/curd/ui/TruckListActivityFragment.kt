package com.blackbeautystudio.curd.ui

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blackbeautystudio.curd.R
import com.blackbeautystudio.curd.databinding.FragmentTruckListBinding
import com.blackbeautystudio.curd.di.ViewModelFactory
import com.blackbeautystudio.curd.utils.MarginItemDecoration

class TruckListActivityFragment : Fragment() {
    private var mViewModel: TruckListViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            activity?.let {
                mViewModel = ViewModelProviders.of(this, ViewModelFactory()).get(TruckListViewModel::class.java)
                DataBindingUtil.inflate<FragmentTruckListBinding>(it.layoutInflater, R.layout.fragment_truck_list, null, false)?.apply {
                    truckList.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                    truckList.addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.item_padding).toInt()))
                    viewModel = mViewModel
                }
            }?.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel?.load()
    }
}
