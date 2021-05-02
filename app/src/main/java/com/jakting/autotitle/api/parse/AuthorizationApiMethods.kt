package com.jakting.autotitle.api.parse

import com.jakting.autotitle.api.accessAPI
import com.jakting.autotitle.api.data.AccessTokenBody
import com.jakting.autotitle.api.data.TokenBody
import com.jakting.autotitle.api.data.UserInfo
import com.jakting.autotitle.utils.MyApplication.Companion.tokenBody
import com.jakting.autotitle.utils.MyApplication.Companion.userInfo
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
            tokenBody.access_token = accessTokenBody.access_token
            logd("所返回的token为：${accessTokenBody.access_token}")
            getUserInfoMethod(object : RetrofitCallback {
                override fun onSuccess(value: Any) {
                    userInfo = value as UserInfo
                    callback.onSuccess(accessTokenBody)
                }

                override fun onError(t: Throwable) {

                }

            })
        }) { t ->
        logd("onError // getRefreshTokenMethod")
        callback.onError(t)
    }
}
