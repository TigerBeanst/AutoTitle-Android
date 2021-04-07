package com.jakting.autotitle.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import java.security.MessageDigest


val API_BASE_URL = "http://192.168.1.48:8000/"

fun logd(message: String) =
    Log.d("hjt", message)

fun toast(message: CharSequence) =
    Toast.makeText(MyApplication.appContext, message, Toast.LENGTH_SHORT).show()

fun longtoast(message: CharSequence) =
    Toast.makeText(MyApplication.appContext, message, Toast.LENGTH_LONG).show()

fun View.sbar(message: CharSequence) =
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)

fun View.sbarlong(message: CharSequence) =
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)

fun View.sbarin(message: CharSequence) =
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)

fun getErrorString(t: Throwable): String {
    return (t as HttpException).response()?.errorBody()?.string().let { return@let it.toString() }
}

fun md5(content: String): String {
    val hash = MessageDigest.getInstance("MD5").digest(content.toByteArray())
    val hex = StringBuilder(hash.size * 2)
    for (b in hash) {
        var str = Integer.toHexString(b.toInt())
        if (b < 0x10) {
            str = "0$str"
        }
        hex.append(str.substring(str.length - 2))
    }
    return hex.toString()
}

fun bearer(str: String) = "Bearer $str"

fun Context.dip2px(dp: Float): Float {
    val density: Float = this.resources.displayMetrics.density // 密度
    return dp * density
}

fun Context.px2dip(px: Float): Int {
    val density = this.resources.displayMetrics.density // 密度
    return (px / density + 0.5f).toInt()
}

fun getPostBody(jsonForPost: String): RequestBody {
    return RequestBody.create(
        MediaType.parse("application/json"), jsonForPost
    )
}


/**
 * 获取 Toolbar 图标的 View
 * @param drawable Drawable?
 * @return View?
 */
fun Toolbar.getToolBarItemView(drawable: Drawable?): View? {
    val size: Int = this.childCount
//        logd("获取底部栏的详情：size为$size")
    for (i in 0 until size) {
        val child: View = this.getChildAt(i)
//            logd("获取底部栏的详情：view为$child")
        if (child is ImageButton) {
            if (child.drawable === drawable) {
//                    logd("获取底部栏的详情：drawable${child.drawable}")
                return child
            }
        }
    }
    return null
}
