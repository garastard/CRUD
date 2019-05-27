package com.blackbeautystudio.curd.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import com.blackbeautystudio.curd.App
import com.blackbeautystudio.curd.R
import com.blackbeautystudio.curd.di.NavEvent
import com.blackbeautystudio.curd.model.Truck
import com.blackbeautystudio.curd.utils.getString
import com.blackbeautystudio.curd.utils.shortSubscription
import com.blackbeautystudio.curd.utils.showLongToast
import com.blackbeautystudio.curd.utils.showShortToast

class ChangeTruckViewModel(arguments: Bundle) : EditTruckViewModel() {

    private val mTruck: Truck? = arguments.getParcelable(EDIT_TRUCK)
    override var editTruckTitle = R.string.change_truck.getString()
    override val onClickListener = View.OnClickListener {
        mTruck?.id?.let { truckId ->
            truckApi.editTruck(truckId, Truck(null, nameText.get(), priceText.get(), commentText.get()))
                    .shortSubscription({
                        App.appComponent.event().onNext(NavEvent(NavEvent.Destination.ONE))
                        R.string.changing_complete.getString().showShortToast()
                    }, { R.string.server_error.getString().showLongToast() })
        }
    }

    init {
        nameText.set(mTruck?.nameTruck)
        priceText.set(mTruck?.price)
        commentText.set(mTruck?.comment)
    }
}
