package com.devtides.retrofitSample.model

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiCallService {

    private val BASE_URL = "https://us-central1-apis2-e78c3.cloudfunctions.net/"

    private var api: ApiCall? = null


    //Below we pass is the application context in the getApi method
    private fun getApi(context: Context): ApiCall {

        if (api == null) {

            // Intercept everything and log into console.
            val okHttpclient = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpclient.addInterceptor(logging)

            // Below we added custom headers to test it is working in this or not.
            // Run the application for checking
            okHttpclient.addInterceptor { chain ->
                val request = chain.request()
                val newRequest = request.newBuilder()
                    .addHeader("useragent", "Android")
                    .build()
                chain.proceed(newRequest)
            }

            val cacheSize = 5 * 1024 * 1024L
            val cache = Cache(context.cacheDir, cacheSize)
            okHttpclient.cache(cache)

            api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpclient.build())
                .build()
                .create(ApiCall::class.java)
        }
        return api!!
    }

    //Below Method we used for Rxjava line 58 converter factory
    private fun getApiRx() =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiCall::class.java)

    fun callRx() = getApiRx()

    //Below method we are calling using context for each api response from ApiCall.kt
    fun call(context: Context) =
        getApi(context).callGet()
    // getApi(context).callGet()
    // getApi(context).callPost()
    //callQuery("James", 23)
    //getApi(context).callFormData(hashMapOf(Pair("Name", "Alex"), Pair("lastname", "Srivastava")))
}