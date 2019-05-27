package com.blackbeautystudio.curd.ui

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import com.blackbeautystudio.curd.App
import com.blackbeautystudio.curd.R
import com.blackbeautystudio.curd.base.BaseViewModel
import com.blackbeautystudio.curd.di.ListUpdateEvent
import com.blackbeautystudio.curd.di.NavEvent
import com.blackbeautystudio.curd.network.TruckApi
import com.blackbeautystudio.curd.utils.*
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TruckListViewModel : BaseViewModel() {
    @Inject
    lateinit var truckApi: TruckApi
    private val mEventObservable by lazy { App.appComponent.event() }
    val truckListAdapter: TruckListAdapter = TruckListAdapter().apply { setHasStableIds(true) }
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val onClickListener: View.OnClickListener = View.OnClickListener {
        mEventObservable.onNext(NavEvent(NavEvent.Destination.TWO))
    }

    val isRefresh = ObservableField<Boolean>(false)

    val swipeRefreshListener: SwipeRefreshLayout.OnRefreshListener =
            SwipeRefreshLayout.OnRefreshListener { isRefresh.set(true) }

    private var mLoadObservableEmitter: ObservableEmitter<Int>? = null
    private val mLoadObservable = Observable.create<Int> { mLoadObservableEmitter = it }
    private var mTruckListDisposable: Disposable? = null
    private var mEventSubscription: Disposable? = null

    init {
        isRefresh.likeRx()
                .filter { it }
                .doOnNext { load() }
                .doOnNext { isRefresh.set(false) }
                .execute()
        mEventSubscription = mEventObservable.subscribe {
            if (it is ListUpdateEvent.DeleteEvent) {
                truckApi.deleteTruck(it.truckId).doOnNext { load() }.execute()
            }
        }
        mTruckListDisposable = mLoadObservable
                .flatMap { truckApi.getTruckList() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingVisibility.value = View.VISIBLE }
                .doOnTerminate { loadingVisibility.value = View.GONE }
                .shortSubscription(
                        { truckListAdapter.updateTruckList(it) },
                        { R.string.truck_error.getString().showLongToast() }
                )
    }

    override fun onCleared() {
        super.onCleared()
        mEventSubscription?.dispose()
        mTruckListDisposable?.dispose()
    }

    fun load() {
        mLoadObservableEmitter?.onNext(0)
    }
}