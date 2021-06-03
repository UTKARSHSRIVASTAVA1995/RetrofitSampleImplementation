package com.devtides.retrofitSample.model

import retrofit2.Call
import retrofit2.http.GET


interface ApiCall {

    @GET("apiCall")
    fun callGet() : Call<ApiCallResponse>
}