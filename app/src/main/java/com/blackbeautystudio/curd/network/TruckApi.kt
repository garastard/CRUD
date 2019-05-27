package com.blackbeautystudio.curd.network

import com.blackbeautystudio.curd.model.Truck
import io.reactivex.Observable
import retrofit2.http.*

interface TruckApi {

    @GET("trucks")
    fun getTruckList(): Observable<List<Truck>>

    @POST("truck/add")
    fun addTruck(@Body body: Truck): Observable<Truck>

    @PATCH("truck/{id}")
    fun editTruck(@Path("id") truckId: Int,
                  @Body body: Truck): Observable<Truck>

    @DELETE("truck/{id}")
    fun deleteTruck(@Path("id") truckId: Int): Observable<Int>
}