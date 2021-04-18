package com.jakting.autotitle.utils.tools

import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.jakting.autotitle.utils.MyApplication.Companion.appContext


fun logd(message: String) =
    Log.d("hjt", message)

fun toast(message: CharSequence) =
    Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()

fun longtoast(message: CharSequence) =
    Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()

fun View.sbar(message: CharSequence) =
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)

fun View.sbarlong(message: CharSequence) =
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)

fun View.sbarin(message: CharSequence) =
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)

fun bearer(str: String) = "Bearer $str"

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
