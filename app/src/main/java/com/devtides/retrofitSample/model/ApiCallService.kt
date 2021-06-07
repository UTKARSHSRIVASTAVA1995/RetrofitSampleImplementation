package com.devtides.retrofitSample.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiCallService {

    private val BASE_URL = "https://us-central1-apis2-e78c3.cloudfunctions.net/"


    // Intercept everything and log into console.
    val okHttpclient = OkHttpClient.Builder()

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        okHttpclient.addInterceptor(logging)
        okHttpclient.addInterceptor { chain ->
            val request = chain.request()
            val newRequest = request.newBuilder()
                .addHeader("useragent", "Android")
                .build()
            chain.proceed(newRequest)
        }
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpclient.build())
        .build()
        .create(ApiCall::class.java)

    // fun call() = api.callGet()
    //fun call() = api.callPost()
    fun call()=api.callQuery("James",23)
    //fun call() = api.callFormData(hashMapOf(Pair("Name", "Alex"), Pair("lastname", "Srivastava")))
}