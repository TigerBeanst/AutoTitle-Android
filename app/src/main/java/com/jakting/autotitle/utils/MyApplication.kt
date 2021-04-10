package com.jakting.autotitle.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.drake.brv.utils.BRV
import com.jakting.autotitle.BR
import com.jakting.autotitle.api.data.LoginStatus
import com.jakting.autotitle.api.data.TokenBody
import com.jakting.autotitle.api.data.UserInfo
import com.jakting.autotitle.utils.SmartRefresh.DeliveryHeader
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class MyApplication : Application() {
    @SuppressLint("CommitPrefEdits")
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        sp = getSharedPreferences("data", MODE_PRIVATE)
        spe = getSharedPreferences("data", MODE_PRIVATE).edit()
        BRV.modelId = BR.newObject
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout -> DeliveryHeader(this) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> ClassicsFooter(this) }
    }

    companion object {
        var loginStatus: LoginStatus = LoginStatus("", "")
        var tokenBody = TokenBody("", "")
        lateinit var appContext: Context
        lateinit var sp: SharedPreferences
        lateinit var spe: SharedPreferences.Editor
        lateinit var userInfo: UserInfo
    }
}