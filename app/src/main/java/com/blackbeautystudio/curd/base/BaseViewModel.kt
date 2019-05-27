package com.blackbeautystudio.curd.base

import android.arch.lifecycle.ViewModel
import com.blackbeautystudio.curd.ui.EditTruckViewModel
import com.blackbeautystudio.curd.ui.TruckListViewModel
import com.blackbeautystudio.curd.di.component.DaggerViewModelInjector
import com.blackbeautystudio.curd.di.component.ViewModelInjector
import com.blackbeautystudio.curd.di.module.NetworkModule

abstract class BaseViewModel: ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is TruckListViewModel -> injector.inject(this)
            is EditTruckViewModel -> injector.inject(this)
        }
    }
}