package com.blackbeautystudio.curd.utils

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SimpleItemAnimator
import android.view.View

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("setSwipeRefreshListener")
fun setSwipeRefreshListener(refreshLayout: SwipeRefreshLayout, refreshListener: SwipeRefreshLayout.OnRefreshListener) =
        refreshLayout.setOnRefreshListener(refreshListener)

@BindingAdapter("setRefresh")
fun setRefresh(refreshLayout: SwipeRefreshLayout, isRefresh: Boolean) {
    refreshLayout.isRefreshing = isRefresh
}

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value ->
            view.visibility = value ?: View.VISIBLE
        })
    }
}