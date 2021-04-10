package com.jakting.autotitle

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.jakting.autotitle.api.data.LoginStatus
import com.jakting.autotitle.api.data.TokenBody
import com.jakting.autotitle.api.parse.getTokenBodyMethod
import com.jakting.autotitle.api.parse.getUserInfoMethod
import com.jakting.autotitle.utils.MyApplication.Companion.loginStatus
import com.jakting.autotitle.utils.MyApplication.Companion.sp
import com.jakting.autotitle.utils.MyApplication.Companion.spe
import com.jakting.autotitle.utils.MyApplication.Companion.tokenBody
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.*
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
        Handler(Looper.getMainLooper()).postDelayed({
            val loginStatusString = sp.getString("login_status", "")
            if (loginStatusString != "") {
                //说明有登录数据（但 access_token 不一定有效）
                loginStatus =
                    Gson().fromJson(loginStatusString, LoginStatus::class.java)
                tokenBody =
                    Gson().fromJson(sp.getString("token_body", ""), TokenBody::class.java)
                intentToMainActivity()
            } else {
                //还没登陆，触发动画
                var statusBarHeight = 0
                val resourceId =
                    applicationContext.resources.getIdentifier(
                        "status_bar_height",
                        "dimen",
                        "android"
                    )
                if (resourceId > 0) {
                    statusBarHeight = applicationContext.resources.getDimensionPixelSize(resourceId)
                }
                logd(statusBarHeight.toString())
                val locationIcon = IntArray(2)
                login_app_icon.getLocationInWindow(locationIcon)
                val xIcon: Float = locationIcon[0].toFloat()
                val yIcon: Float = locationIcon[1].toFloat()
                val targetXIcon = this.dip2px(50f)
                val targetYIcon = this.dip2px(210f) + statusBarHeight
                login_app_icon.animate()
                    .translationX(-(xIcon - targetXIcon))
                    .translationY(-(yIcon - targetYIcon))
                    .setDuration(1500)
                    .setInterpolator(AccelerateInterpolator())
                    .withLayer()
                val locationName = IntArray(2)
                login_app_name.getLocationInWindow(locationName)
                val xName: Float = locationName[0].toFloat()
                val yName: Float = locationName[1].toFloat()

                val targetXName = this.dip2px(50f)
                val targetYName = this.dip2px(270f) + statusBarHeight
                login_app_name.animate()
                    .translationX(-(xName - targetXName))
                    .translationY(-(yName - targetYName))
                    .setDuration(1500)
                    .setInterpolator(AccelerateInterpolator())
                    .withLayer()
                showEverything()
            }
        }, 1000)
    }

    private fun intentToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun showEverything() {
        val appearAnimation = AlphaAnimation(0f, 1f)
        appearAnimation.duration = 3000
        login_layout_body.startAnimation(appearAnimation)
        login_layout_body.visibility = View.VISIBLE
    }

    private fun initView() {
        login_text_bottom.movementMethod = LinkMovementMethod.getInstance()
        login_button_login.setOnClickListener {
            val usernameInput = login_inputEdit_username.text.toString()
            val passwordInput = login_inputEdit_password.text.toString()
            when {
                usernameInput.isEmpty() -> {
                    toast(getString(R.string.login_toast_login_empty_username))
                }
                passwordInput.isEmpty() -> {
                    toast(getString(R.string.login_toast_login_empty_password))
                }
                else -> {
                    loginStatus.username = Base64Util.encode(usernameInput)
                    loginStatus.password = Base64Util.encode(passwordInput)
                    logd("loginStatus.username = ${loginStatus.username}      loginStatus.password = ${loginStatus.password}")
                    spe.putString("login_status", Gson().toJson(loginStatus))
                    spe.apply()
                    getTokenBodyMethod(usernameInput, passwordInput, object : RetrofitCallback {
                        override fun onSuccess(value: Any) {
                            tokenBody = value as TokenBody
                            spe.putString("token_body", Gson().toJson(tokenBody))
                            spe.apply()
                            getUserInfoMethod(object : RetrofitCallback {
                                override fun onSuccess(value: Any) {
                                    intentToMainActivity()
                                }

                                override fun onError(t: Throwable) {
                                    TODO("Not yet implemented")
                                }

                            })
                        }

                        override fun onError(t: Throwable) {
                            if (getErrorStatusCode(t) == 401) {
                                toast(getErrorString(t))
                            }
                        }
                    })
                }
            }
        }
    }


}