package com.blackbeautystudio.curd.utils

import android.databinding.ObservableField
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

fun <T> ObservableField<T>.likeRx() = Observable.create<T> { e ->
    val propertyChangeCallback = object : android.databinding.Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: android.databinding.Observable?, propertyId: Int) {
            this@likeRx.get()?.let { e.onNext(it) }
        }
    }
    this@likeRx.addOnPropertyChangedCallback(propertyChangeCallback)
    e.setCancellable { this@likeRx.removeOnPropertyChangedCallback(propertyChangeCallback) }
}

fun <T> Observable<T>.shortSubscription(onNext: (T) -> Unit,
                                        onError: (Throwable) -> Unit = { it.printStackTrace() },
                                        onComplete: () -> Unit = {}): Disposable = subscribe(onNext, onError, onComplete)

fun <T> Observable<T>.execute() = shortSubscription(onNext = {})