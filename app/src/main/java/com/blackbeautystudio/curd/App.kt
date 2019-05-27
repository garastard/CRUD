package com.blackbeautystudio.curd

import android.app.Application
import android.content.Context
import com.blackbeautystudio.curd.di.component.AppComponent
import com.blackbeautystudio.curd.di.component.DaggerAppComponent
import com.blackbeautystudio.curd.di.module.AppModule

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(context))
                .build()
    }

}