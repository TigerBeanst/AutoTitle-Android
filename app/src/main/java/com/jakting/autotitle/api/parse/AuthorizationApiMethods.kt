package com.jakting.autotitle.api.parse

import com.jakting.autotitle.api.accessAPI
import com.jakting.autotitle.api.data.AccessTokenBody
import com.jakting.autotitle.api.data.TokenBody
import com.jakting.autotitle.utils.MyApplication.Companion.tokenBody
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.bearer
import com.jakting.autotitle.utils.tools.getPostBody
import com.jakting.autotitle.utils.tools.logd
import com.jakting.autotitle.utils.tools.md5


fun getTokenBodyMethod(username: String, password: String, callback: RetrofitCallback) {
    val createDestinationPostBody = getPostBody(
        "{\"username\":\"$username\"," +
                "\"password\":\"${md5(password)}\"}"
    )
    accessAPI(
        {
            getToken(createDestinationPostBody)
        }, { objectReturn ->
            tokenBody = objectReturn as TokenBody
            callback.onSuccess(tokenBody)
        }) { t ->
        logd("onError // getTokenBody")
        callback.onError(t)
    }
}

fun getAccessTokenMethod(callback: RetrofitCallback) {
    logd("tokenBody.refresh_token为${tokenBody.refresh_token}")
    accessAPI(
        {
            getAccessToken(bearer(tokenBody.refresh_token))
        }, { objectReturn ->
            val accessTokenBody = objectReturn as AccessTokenBody
            callback.onSuccess(accessTokenBody)
        }) { t ->
        logd("onError // getRefreshTokenMethod")
        callback.onError(t)
    }
}
