package com.blackbeautystudio.curd.ui

import android.view.View
import com.blackbeautystudio.curd.App
import com.blackbeautystudio.curd.R
import com.blackbeautystudio.curd.di.NavEvent
import com.blackbeautystudio.curd.model.Truck
import com.blackbeautystudio.curd.utils.getString
import com.blackbeautystudio.curd.utils.shortSubscription
import com.blackbeautystudio.curd.utils.showLongToast
import com.blackbeautystudio.curd.utils.showShortToast

class AddTruckViewModel : EditTruckViewModel() {

    override var editTruckTitle = R.string.create_new_truck.getString()
    override val onClickListener = View.OnClickListener {
        truckApi.addTruck(Truck(null, nameText.get(), priceText.get(), commentText.get()))
                .shortSubscription({
                    App.appComponent.event().onNext(NavEvent(NavEvent.Destination.ONE))
                    R.string.adding_complete.getString().showShortToast()
                }, { R.string.server_error.getString().showLongToast() })
    }
}
