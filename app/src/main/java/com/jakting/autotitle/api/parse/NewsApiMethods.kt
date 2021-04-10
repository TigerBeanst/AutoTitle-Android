package com.jakting.autotitle.api.parse

import com.jakting.autotitle.api.accessAPI
import com.jakting.autotitle.api.data.AutoTitleObject
import com.jakting.autotitle.api.data.News
import com.jakting.autotitle.utils.MyApplication.Companion.tokenBody
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.bearer
import com.jakting.autotitle.utils.tools.logd
import okhttp3.MediaType
import okhttp3.RequestBody

fun getNewsListMethod(kind: String, start: Int, count: Int, callback: RetrofitCallback) {
    accessAPI(
        {
            getNews(
                bearer(tokenBody.access_token),
                kind, start.toString(), count.toString()
            )
        }, { objectReturn ->
            val news = objectReturn as News
            callback.onSuccess(news)
        }) { t ->
        logd("onError // getTokenBody")
        t.printStackTrace()
        callback.onError(t)
    }
}

fun getAutoTitleMethod(content:String, callback: RetrofitCallback) {
    val createDestinationPostBody: RequestBody =
        RequestBody.create(
            MediaType.parse("application/json"),
            "{\"content\":\"$content\"}"
        )
    accessAPI(
        {
            getAutoTitle(
                bearer(tokenBody.access_token),
                createDestinationPostBody
            )
        }, { objectReturn ->
            val autoTitleObject = objectReturn as AutoTitleObject
            callback.onSuccess(autoTitleObject)
        }) { t ->
        logd("onError // getTokenBody")
        t.printStackTrace()
        callback.onError(t)
    }
}