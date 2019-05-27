package com.blackbeautystudio.curd.di.component

import android.content.Context
import com.blackbeautystudio.curd.di.Event
import com.blackbeautystudio.curd.di.module.AppModule
import dagger.Component
import io.reactivex.processors.PublishProcessor
import javax.inject.Singleton

@Component(modules = [(AppModule::class)])
@Singleton
interface AppComponent {
    fun event(): PublishProcessor<Event>
    fun appContext(): Context
}