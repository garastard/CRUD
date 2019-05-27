package com.blackbeautystudio.curd.di

import android.os.Bundle

interface Event

class NavEvent(val destination: Destination, val args: Bundle? = null) : Event {
    enum class Destination { ONE, TWO }
}

sealed class ListUpdateEvent : Event {
    class DeleteEvent(val truckId: Int) : ListUpdateEvent()
}

