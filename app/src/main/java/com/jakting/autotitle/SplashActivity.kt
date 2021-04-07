package com.jakting.autotitle

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakting.autotitle.api.data.TokenBody
import com.jakting.autotitle.api.data.UserInfo
import com.jakting.autotitle.utils.*
import com.jakting.rn6pan.api.accessAPI
import kotlinx.android.synthetic.main.activity_splash.*
import okhttp3.MediaType
import okhttp3.RequestBody


class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
        Handler(Looper.getMainLooper()).postDelayed({
            var status_bar_height = 0
            val resourceId =
                applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                status_bar_height = applicationContext.resources.getDimensionPixelSize(resourceId)
            }
            logd(status_bar_height.toString())
            val locationIcon = IntArray(2)
            login_app_icon.getLocationInWindow(locationIcon)
            val xIcon: Float = locationIcon[0].toFloat()
            val yIcon: Float = locationIcon[1].toFloat()
            val targetXIcon = this.dip2px(50f)
            val targetYIcon = this.dip2px(210f) + status_bar_height
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
            val targetYName = this.dip2px(270f) + status_bar_height
            login_app_name.animate()
                .translationX(-(xName - targetXName))
                .translationY(-(yName - targetYName))
                .setDuration(1500)
                .setInterpolator(AccelerateInterpolator())
                .withLayer()
            showEverything()
        }, 1000)
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
                    getTokenBody(usernameInput, passwordInput)
                }
            }
        }
    }

    /**
     * 获取 access_token
     * @param username String
     * @param password String
     */
    private fun getTokenBody(username: String, password: String) {
        val createDestinationPostBody: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json"),
                "{\"username\":\"$username\"," +
                        "\"password\":\"${md5(password)}\"}"
            )
        accessAPI(
            {
                getToken(createDestinationPostBody)
            }, { objectReturn ->
                val tokenBody = objectReturn as TokenBody
                BaseActivity.access_token = tokenBody.access_token
                getUserInfo()

            }) { t ->
            logd("onError // getUserInfo")
            t.printStackTrace()
        }
    }

    /**
     * 获取用户信息
     * @param username String
     * @param password String
     * @return UserInfo
     */
    private fun getUserInfo() {
        accessAPI(
            {
                getUserInfo(bearer(BaseActivity.access_token))
            }, { objectReturn ->
                BaseActivity.userInfo = objectReturn as UserInfo
                toast(getString(R.string.login_toast_login_success))
                MaterialAlertDialogBuilder(this)
                    .setTitle("测试")
                    .setMessage("用户名：${BaseActivity.userInfo.username}\n昵称：${BaseActivity.userInfo.nickname}\n邮箱：${BaseActivity.userInfo.email}\n")
                    .show()

            }) { t ->
            logd("onError // getUserInfo")
            t.printStackTrace()
        }
    }

}