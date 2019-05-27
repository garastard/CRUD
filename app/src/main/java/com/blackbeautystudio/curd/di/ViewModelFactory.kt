package com.blackbeautystudio.curd.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import com.blackbeautystudio.curd.ui.AddTruckViewModel
import com.blackbeautystudio.curd.ui.ChangeTruckViewModel
import com.blackbeautystudio.curd.ui.EditTruckViewModel
import com.blackbeautystudio.curd.ui.TruckListViewModel

class ViewModelFactory(private val mBundle: Bundle? = null) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == TruckListViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            return TruckListViewModel() as T
        }
        if (modelClass == EditTruckViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            return (mBundle?.let { ChangeTruckViewModel(it) } ?: AddTruckViewModel()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}