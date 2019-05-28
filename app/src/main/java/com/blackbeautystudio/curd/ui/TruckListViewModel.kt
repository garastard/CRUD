package com.blackbeautystudio.curd.ui

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TruckListViewModel : BaseViewModel() {
    @Inject
    lateinit var truckApi: TruckApi
    private val mEventObservable by lazy { App.appComponent.event() }
    private val mInternetStateObservable by lazy { App.appComponent.internetState() }
    val truckListAdapter: TruckListAdapter = TruckListAdapter()
    val loadingVisibility = ObservableInt(GONE)
    val internetErrorVisibility = ObservableInt(GONE)
    val truckListVisibility = ObservableInt(VISIBLE)

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
        mInternetStateObservable.shortSubscription({ if (!it.available()) internetConnectionUnavailable() })
        mTruckListDisposable = mLoadObservable
                .throttleLast(100L, TimeUnit.MILLISECONDS)
                .flatMap { mInternetStateObservable }
                .filter { it.available() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    internetConnectionAvailable()
                    loadingVisibility.set(VISIBLE)
                }
                .flatMap { truckApi.getTruckList() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate {
                    loadingVisibility.set(GONE)
                    loadingVisibility.notifyChange()
                }
                .map { it.filter { !it.comment.isNullOrBlank() && !it.price.isNullOrBlank() && !it.nameTruck.isNullOrBlank() } }
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

    private fun internetConnectionAvailable() {
        internetErrorVisibility.set(GONE)
        truckListVisibility.set(VISIBLE)
    }

    private fun internetConnectionUnavailable() {
        R.string.internet_unavailable.getString().showShortToast()
        internetErrorVisibility.set(VISIBLE)
        internetErrorVisibility.notifyChange()
        loadingVisibility.set(GONE)
        loadingVisibility.notifyChange()
        truckListVisibility.set(GONE)
        truckListVisibility.notifyChange()
    }

    fun load() {
        mLoadObservableEmitter?.onNext(0)
    }
}