package com.devtides.retrofitSample.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.devtides.retrofitSample.R
import com.devtides.retrofitSample.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val itemsAdapter = ListAdapter(arrayListOf())
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.fetchDataRx()
        //viewModel.fetchData()
        //viewModel.fetchDatSync()

        items_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = itemsAdapter
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.apiResponse.observe(this, Observer { items ->
            items?.let {
                items_list.visibility = View.VISIBLE
                itemsAdapter.updateItems(it)
            }
        })

        viewModel.error.observe(this, Observer { errorMsg ->
            list_error.visibility = if (errorMsg == null) View.GONE else View.VISIBLE
            list_error.text = "Error\n$errorMsg"
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    list_error.visibility = View.GONE
                    items_list.visibility = View.GONE
                }
            }
        })
    }
}
