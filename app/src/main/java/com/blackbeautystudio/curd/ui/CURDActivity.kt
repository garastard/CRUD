package com.blackbeautystudio.curd.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.blackbeautystudio.curd.App
import com.blackbeautystudio.curd.R
import com.blackbeautystudio.curd.di.NavEvent
import com.blackbeautystudio.curd.utils.hideSoftKeyboard


class CURDActivity : AppCompatActivity() {

    private var navController: NavController? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_truck_list)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        App.appComponent.event()
                .doOnNext { this.hideSoftKeyboard() }
                .subscribe {
                    if (it is NavEvent) {
                        when (it.destination) {
                            NavEvent.Destination.TWO -> navController?.navigate(R.id.addTruckFragment, it.args)
                            NavEvent.Destination.ONE -> navController?.popBackStack()
                        }
                    }
                }
    }
}