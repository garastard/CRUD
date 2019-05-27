package com.blackbeautystudio.curd.utils

import android.support.annotation.LongDef
import android.view.InflateException
import android.widget.Toast
import com.blackbeautystudio.curd.App

@LongDef(DurationConst.LENGTH_SHORT, DurationConst.LENGTH_LONG)
@Retention(AnnotationRetention.SOURCE)
annotation class Duration

object DurationConst {
    const val LENGTH_SHORT = 0L
    const val LENGTH_LONG = 1L
}

fun String.showShortToast() = showToast(DurationConst.LENGTH_SHORT)
fun String.showLongToast() = showToast(DurationConst.LENGTH_LONG)

fun String.showToast(@Duration duration: Long) {
    val context = App.appComponent.appContext()
    if (this.isNotEmpty()) {
        val toast: Toast?
        try {
            toast = Toast.makeText(
                    context,
                    this,
                    duration.toInt()
            )
        } catch (e: InflateException) {
            e.printStackTrace()
            return
        }
        toast?.show()
    }
}