package com.blackbeautystudio.curd.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @property id the unique identifier of the truck
 * @property nameTruck the name of truck
 * @property price the price of truck
 * @property comment the comment for the truck
 */

@Parcelize
data class Truck(val id: Int?, val nameTruck: String?, val price: String?, val comment: String?) : Parcelable
