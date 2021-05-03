package com.jakting.autotitle.api.parse

import com.jakting.autotitle.api.accessAPI
import com.jakting.autotitle.api.data.AutoTitleObject
import com.jakting.autotitle.api.data.News
import com.jakting.autotitle.api.data.NewsSearch
import com.jakting.autotitle.utils.MyApplication.Companion.tokenBody
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.bearer
import com.jakting.autotitle.utils.tools.getPostBody
import com.jakting.autotitle.utils.tools.logd

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
        callback.onError(t)
        getNewsListMethod(kind, start, count, callback)
    }
}

fun getAutoTitleMethod(content: String, callback: RetrofitCallback) {
    val createDestinationPostBody = getPostBody(
        "{\"content\":\"$content\"}"
    )
    accessAPI(
        {
            getAutoTitle(
                bearer(tokenBody.access_token),
                createDestinationPostBody
            )
        }, { objectReturn ->
            callback.onSuccess(objectReturn)
        }) { t ->
        logd("onError // getTokenBody")
        callback.onError(t)
        getAutoTitleMethod(content, callback)
    }
}

fun searchNewsListMethod(keyword: String, callback: RetrofitCallback) {
    val createDestinationPostBody = getPostBody(
        "{\"keyword\":\"$keyword\"}"
    )
    accessAPI(
        {
            searchNews(
                bearer(tokenBody.access_token),
                createDestinationPostBody
            )
        }, { objectReturn ->
            callback.onSuccess(objectReturn)
        }) { t ->
        logd("onError // searchNewsListMethod")
        callback.onError(t)
        searchNewsListMethod(keyword, callback)
    }
}