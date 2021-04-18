package com.jakting.autotitle.api.parse

import com.jakting.autotitle.R
import com.jakting.autotitle.api.accessAPI
import com.jakting.autotitle.api.data.UserInfo
import com.jakting.autotitle.utils.MyApplication
import com.jakting.autotitle.utils.MyApplication.Companion.userInfo
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.bearer
import com.jakting.autotitle.utils.tools.logd
import com.jakting.autotitle.utils.tools.toast


fun getUserInfoMethod(callback: RetrofitCallback) {
    accessAPI(
        {
            getUserInfo(bearer(MyApplication.tokenBody.access_token))
        }, { objectReturn ->
            userInfo = objectReturn as UserInfo
            toast(MyApplication.appContext.getString(R.string.login_toast_login_success))
            callback.onSuccess(userInfo)

        }) { t ->
        logd("onError // getUserInfo")
        callback.onError(t)
    }
}