package com.blackbeautystudio.curd.di.module

import android.content.Context
import com.blackbeautystudio.curd.di.Event
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import dagger.Module
import dagger.Provides
import io.reactivex.processors.PublishProcessor
import javax.inject.Singleton

@Module
class AppModule(private val mContext: Context) {
    @Provides
    fun provideAppContext() = mContext

    @Provides
    @Singleton
    fun providesEvents(): PublishProcessor<Event> = PublishProcessor.create()

    @Provides
    fun providesInternetStateObservable() = ReactiveNetwork.observeNetworkConnectivity(mContext)
}