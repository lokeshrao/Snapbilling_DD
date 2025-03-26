package com.snapbizz.onboarding.downSync

import com.google.gson.Gson
import com.google.gson.JsonArray


fun <U> prepareDataForUpload(gson: Gson, dataList: List<U>): JsonArray {
    val jsonArray = JsonArray()
    for (data in dataList) {
        jsonArray.add(gson.toJsonTree(data))
    }
    return jsonArray
}


