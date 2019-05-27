package com.blackbeautystudio.curd.ui

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.blackbeautystudio.curd.R
import com.blackbeautystudio.curd.databinding.EditTruckFragmentBinding
import com.blackbeautystudio.curd.di.ViewModelFactory
import com.blackbeautystudio.curd.model.Truck
import com.blackbeautystudio.curd.ui.EditTruckViewModel.Companion.EDIT_TRUCK

class AddTruckFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) = activity?.let {
        DataBindingUtil.inflate<EditTruckFragmentBinding>(it.layoutInflater, R.layout.edit_truck_fragment, null, false)?.apply {
            viewModel = ViewModelProviders.of(it, ViewModelFactory(arguments)).get(arguments?.getParcelable<Truck>(EDIT_TRUCK)?.nameTruck
                    ?: System.currentTimeMillis().toString(), EditTruckViewModel::class.java)
        }
    }?.root
}
