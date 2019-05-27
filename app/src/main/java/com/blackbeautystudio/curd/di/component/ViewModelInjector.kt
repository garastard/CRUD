package com.blackbeautystudio.curd.di.component

import com.blackbeautystudio.curd.ui.EditTruckViewModel
import com.blackbeautystudio.curd.ui.TruckListViewModel
import com.blackbeautystudio.curd.di.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {
    fun inject(truckListViewModel: TruckListViewModel)
    fun inject(editTruckViewModel: EditTruckViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
    }
}