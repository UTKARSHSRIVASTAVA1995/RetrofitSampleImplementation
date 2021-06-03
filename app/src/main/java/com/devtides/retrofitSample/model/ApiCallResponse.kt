package com.devtides.retrofitSample.model

import com.google.gson.annotations.SerializedName

data class ApiCallResponse(
    val method: String?,
    val query: Map<String, String>?,
    @SerializedName("headers")
    val heads: Map<String, String>?
) {
    fun flatten(): List<Item> {
        val flatpack = arrayListOf<Item>()
        method?.let { flatpack.add(Item("method", method, TYPE_ITEM)) }

        query?.let {
            if (!query.values.isEmpty()) {
                flatpack.add(Item("query", "", TYPE_CATEGORY))
                addMapItems(query, flatpack)
            }
        }

        heads?.let {

            if (!heads.values.isEmpty()) {
                flatpack.add(Item("headers", "", TYPE_CATEGORY))
                addMapItems(heads, flatpack)
            }
        }
        return flatpack
    }

    private fun addMapItems(map: Map<String, String>?, flatpack: ArrayList<Item>) {
        for (key in map!!.keys) {
            flatpack.add(Item(key, map.getValue(key), TYPE_ITEM))
        }
        return
    }
}