package com.devtides.retrofitSample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devtides.retrofitSample.model.ApiCallResponse
import com.devtides.retrofitSample.model.ApiCallService
import com.devtides.retrofitSample.model.Item
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        GlobalScope.launch(Dispatchers.Main) {
            onError("Exception: ${throwable.localizedMessage}")
        }
    }

    val apiResponse = MutableLiveData<List<Item>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    
    // Asynchronous Communication
    fun fetchData() {
        loading.value = true
        val call = ApiCallService.call()
        call.enqueue(object : Callback<ApiCallResponse> {
            override fun onResponse(
                call: Call<ApiCallResponse>,
                response: Response<ApiCallResponse>
            ) {

                val body = response.body()
                apiResponse.value = body?.flatten()
                error.value = null
                loading.value = false

            }

            override fun onFailure(call: Call<ApiCallResponse>, t: Throwable) {
                if (call.isCanceled) {
                    onError("The call was can canceled")
                } else {
                    onError(t.localizedMessage)
                }
            }
        })
    }


    // Synchronous Communication
    fun fetchDatSync() {
        loading.value = true

        //Background thread IO to perform Api call. (Coroutines)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = ApiCallService.call().execute()

            //Below line is used to switch from Background thread to Main Thread to display Result.
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val body = response.body()
                    apiResponse.value = body?.flatten()
                    error.value = null
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }


    private fun onError(message: String) {
        error.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}