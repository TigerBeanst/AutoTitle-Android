package com.jakting.autotitle.utils.SmartRefresh

import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable


/**
 * 画笔 Drawable
 * Created by scwang on 2017/6/16.
 */
abstract class PaintDrawable protected constructor() : Drawable() {
    protected var mPaint = Paint()
    fun setColor(color: Int) {
        mPaint.color = color
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    init {
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        mPaint.color = -0x555556
    }
}