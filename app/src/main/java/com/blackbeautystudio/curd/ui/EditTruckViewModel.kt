package com.blackbeautystudio.curd.ui

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.blackbeautystudio.curd.App
import com.blackbeautystudio.curd.R
import com.blackbeautystudio.curd.base.BaseViewModel
import com.blackbeautystudio.curd.network.TruckApi
import com.blackbeautystudio.curd.utils.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

abstract class EditTruckViewModel : BaseViewModel() {

    companion object {
        const val EDIT_TRUCK = "EditTruck.Extra.Truck"
        private const val INPUT_DELAY = 50L
        private const val FIELD_MIN_LENGTH = 2
        private const val NAME_FIELD_LENGTH = 50
        private const val PRICE_FIELD_LENGTH = 10
        private const val COMMENT_FIELD_LENGTH = 500
    }

    @Inject
    lateinit var truckApi: TruckApi
    private val mInternetStateObservable by lazy { App.appComponent.internetState() }
    abstract var editTruckTitle: String
    abstract val onClickListener: View.OnClickListener

    val truckNameError = ObservableField<String>(EMPTY)
    val nameText = ObservableField<String>(EMPTY)
    val truckPriceError = ObservableField<String>(EMPTY)
    val priceText = ObservableField<String>()
    val commentPriceError = ObservableField<String>(EMPTY)
    val commentText = ObservableField<String>()
    val internetErrorVisibility = ObservableInt(INVISIBLE)

    private var mNameTextCorrect = false
    private var mPriceTextCorrect = false
    private var mCommentTextCorrect = false

    val isButtonEnabled = ObservableBoolean(false)

    private var mTextChangeDisposable: Disposable? = null

    init {
        mInternetStateObservable.shortSubscription({
            if (!it.available()) internetConnectionUnavailable() else
                internetConnectionAvailable()
        })
        mTextChangeDisposable = Observable.merge((commentText.likeRx()
                .distinctUntilChanged()
                .throttleLast(INPUT_DELAY, TimeUnit.MILLISECONDS)
                .doOnNext {
                    mCommentTextCorrect = isValidateInput(it, commentPriceError, COMMENT_FIELD_LENGTH)
                    mCommentTextCorrect
                }), (priceText.likeRx()
                .distinctUntilChanged()
                .throttleLast(INPUT_DELAY, TimeUnit.MILLISECONDS)
                .doOnNext {
                    mPriceTextCorrect = isValidateInput(it, truckPriceError, PRICE_FIELD_LENGTH)
                    mPriceTextCorrect
                }), (nameText.likeRx()
                .distinctUntilChanged()
                .throttleLast(INPUT_DELAY, TimeUnit.MILLISECONDS)
                .doOnNext {
                    mNameTextCorrect = isValidateInput(it, truckNameError, NAME_FIELD_LENGTH)
                    mNameTextCorrect
                }))
                .flatMap { mInternetStateObservable }
                .filter { it.available() }
                .doOnNext { internetConnectionAvailable() }
                .shortSubscription({ updateButton() })
    }

    private fun isValidateInput(inputString: String, errorField: ObservableField<String>, fieldMaxLimit: Int) =
            when {
                inputString.isBlank() -> {
                    errorField.set("Поле не должно быть пустым")
                    false
                }
                inputString.length > fieldMaxLimit -> {
                    errorField.set("Не более $fieldMaxLimit знаков")
                    false
                }
                inputString.length < FIELD_MIN_LENGTH -> {
                    errorField.set("Не менее $FIELD_MIN_LENGTH знаков")
                    false
                }
                else -> {
                    errorField.set("")
                    true
                }
            }

    private fun updateButton() = isButtonEnabled.set(mPriceTextCorrect && mNameTextCorrect && mCommentTextCorrect)

    private fun internetConnectionAvailable() {
        internetErrorVisibility.set(INVISIBLE)
        internetErrorVisibility.notifyChange()
    }

    private fun internetConnectionUnavailable() {
        R.string.internet_unavailable.getString().showShortToast()
        internetErrorVisibility.set(VISIBLE)
        internetErrorVisibility.notifyChange()
    }

    override fun onCleared() {
        super.onCleared()
        mTextChangeDisposable?.dispose()
    }
}