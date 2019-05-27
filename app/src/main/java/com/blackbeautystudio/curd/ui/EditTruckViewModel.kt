package com.blackbeautystudio.curd.ui

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.view.View
import com.blackbeautystudio.curd.base.BaseViewModel
import com.blackbeautystudio.curd.network.TruckApi
import com.blackbeautystudio.curd.utils.EMPTY
import com.blackbeautystudio.curd.utils.likeRx
import com.blackbeautystudio.curd.utils.shortSubscription
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

abstract class EditTruckViewModel : BaseViewModel() {

    companion object {
        const val EDIT_TRUCK = "EditTruck.Extra.Truck"
        private const val INPUT_DELAY = 50L
        private const val NAME_FIELD_LENGTH = 50
        private const val PRICE_FIELD_LENGTH = 10
        private const val COMMENT_FIELD_LENGTH = 500
    }

    @Inject
    lateinit var truckApi: TruckApi

    abstract var editTruckTitle: String
    abstract val onClickListener: View.OnClickListener

    val truckNameError = ObservableField<String>(EMPTY)
    val nameText = ObservableField<String>(EMPTY)
    val truckPriceError = ObservableField<String>(EMPTY)
    val priceText = ObservableField<String>()
    val commentPriceError = ObservableField<String>(EMPTY)
    val commentText = ObservableField<String>()

    private var mNameTextCorrect = false
    private var mPriceTextCorrect = false
    private var mCommentTextCorrect = false

    val isButtonEnabled = ObservableBoolean(false)

    private var mTextChangeDisposable: Disposable? = null

    init {
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
                .shortSubscription({ updateButton() })
    }

    private fun isValidateInput(inputString: String, errorField: ObservableField<String>, fieldLimit: Int) =
            when {
                inputString.isBlank() -> {
                    errorField.set("Поле не должно быть пустым")
                    false
                }
                inputString.length > fieldLimit -> {
                    errorField.set("Не более 10 знаков")
                    false
                }
                else -> {
                    errorField.set("")
                    true
                }
            }

    private fun updateButton() = isButtonEnabled.set(mPriceTextCorrect && mNameTextCorrect && mCommentTextCorrect)

    override fun onCleared() {
        super.onCleared()
        mTextChangeDisposable?.dispose()
    }
}