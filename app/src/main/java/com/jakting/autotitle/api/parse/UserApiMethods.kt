package com.jakting.autotitle.api.parse

import com.jakting.autotitle.api.accessAPI
import com.jakting.autotitle.api.data.UserAvatarBody
import com.jakting.autotitle.api.data.UserInfo
import com.jakting.autotitle.api.data.UserInfoUpdate
import com.jakting.autotitle.utils.MyApplication.Companion.tokenBody
import com.jakting.autotitle.utils.MyApplication.Companion.userInfo
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.bearer
import com.jakting.autotitle.utils.tools.logd
import okhttp3.MultipartBody
import okhttp3.RequestBody


fun getUserInfoMethod(callback: RetrofitCallback) {
    accessAPI(
        {
            getUserInfo(bearer(tokenBody.access_token))
        }, { objectReturn ->
            userInfo = objectReturn as UserInfo
            callback.onSuccess(userInfo)

        }) { t ->
        logd("onError // getUserInfo")
        callback.onError(t)
        getUserInfoMethod(callback)
    }
}

fun updateUserInfoMethod(requestBody: RequestBody, callback: RetrofitCallback) {
    accessAPI(
        {
            updateUserInfo(bearer(tokenBody.access_token), requestBody)
        }, { objectReturn ->
            val userInfoUpdate = objectReturn as UserInfoUpdate
            callback.onSuccess(userInfoUpdate)

        }) { t ->
        logd("onError // updateUserInfo")
        callback.onError(t)
        updateUserInfoMethod(requestBody, callback)
    }
}

fun updateUserAvatarMethod(part: MultipartBody.Part, callback: RetrofitCallback) {
    accessAPI(
        {
            updateUserAvatar(bearer(tokenBody.access_token), part)
        }, { objectReturn ->
            callback.onSuccess(objectReturn)
        }) { t ->
        logd("onError // updateUserAvatar")
        callback.onError(t)
        updateUserAvatarMethod(part, callback)
    }
}