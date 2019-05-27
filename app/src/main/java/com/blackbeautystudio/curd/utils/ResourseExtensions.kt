package com.blackbeautystudio.curd.utils

import android.content.Context
import android.content.res.Resources
import com.blackbeautystudio.curd.App

private val mContext by lazy {
    App.appComponent.appContext()
}

@JvmOverloads
fun Int.getString(context: Context = mContext, default: String = EMPTY): String = try {
    context.resources.getString(this)
} catch (e: Resources.NotFoundException) {
    default
}

fun Int.getString(context: Context = mContext, default: Int) = this.getString(context, default.getString())
