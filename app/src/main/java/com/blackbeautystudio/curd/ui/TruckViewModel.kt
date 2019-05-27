package com.blackbeautystudio.curd.ui

import android.os.Bundle
import android.view.View
import com.blackbeautystudio.curd.App
import com.blackbeautystudio.curd.ui.EditTruckViewModel.Companion.EDIT_TRUCK
import com.blackbeautystudio.curd.base.BaseViewModel
import com.blackbeautystudio.curd.di.ListUpdateEvent
import com.blackbeautystudio.curd.di.NavEvent
import com.blackbeautystudio.curd.model.Truck

class TruckViewModel(val truck: Truck) : BaseViewModel() {

    private val mNavigationEventEmitter by lazy { App.appComponent.event()}
    val onDeleteClickListener = View.OnClickListener {
        truck.id?.let { mNavigationEventEmitter.onNext(ListUpdateEvent.DeleteEvent(it)) }
    }
    val onEditClickListener = View.OnClickListener {
        mNavigationEventEmitter.onNext(NavEvent(NavEvent.Destination.TWO, Bundle().apply { putParcelable(EDIT_TRUCK, truck) }))
    }

}