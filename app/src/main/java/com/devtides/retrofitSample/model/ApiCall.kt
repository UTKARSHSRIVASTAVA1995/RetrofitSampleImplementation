package com.devtides.retrofitSample.model

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*


interface ApiCall {

    @GET("apiCall")
    fun callGet(): Call<ApiCallResponse>

    @POST("apiCall")
    fun callPost(): Call<ApiCallResponse>

    @GET("apiCall")
    fun callQuery(@Query("name") n: String, @Query("age") a: Int): Call<ApiCallResponse>

    @FormUrlEncoded
    @POST("apiCall")
    fun callFormData(
        @Field("first_name") firstname: String,
        @Field("last_name") lastName: String
    ): Call<ApiCallResponse>

    @FormUrlEncoded
    @POST("apiCall")
    fun callFormData(@FieldMap fields: Map<String, String>): Call<ApiCallResponse>

    //Below we use Rxjava Single because Single is observer that emits one value and finish it.
    @GET("apiCall")
    fun callGetRx(): Single<ApiCallResponse>
}