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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AddTruckViewModel : EditTruckViewModel() {

    private var mAddTruckDisposable: Disposable? = null
    override var editTruckTitle = R.string.create_new_truck.getString()
    override val onClickListener = View.OnClickListener {
        mAddTruckDisposable = truckApi.addTruck(Truck(null, nameText.get(), priceText.get(), commentText.get()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .shortSubscription({
                    App.appComponent.event().onNext(NavEvent(NavEvent.Destination.ONE))
                    R.string.adding_complete.getString().showShortToast()
                }, { R.string.server_error.getString().showLongToast() })
    }

    override fun onCleared() {
        super.onCleared()
        mAddTruckDisposable?.dispose()
    }
}
