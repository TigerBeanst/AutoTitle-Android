package com.jakting.autotitle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jakting.autotitle.api.data.UserInfo

open class BaseActivity : AppCompatActivity() {

    companion object {
        var access_token = ""
        lateinit var userInfo: UserInfo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}