package com.github.crayonxiaoxin.abc.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

fun View.show(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun View.hide(hide: Boolean = true) {
    this.show(!hide)
}

fun View.isDisplay(): Boolean {
    return this.visibility == View.VISIBLE
}

fun Context.inflater(): LayoutInflater {
    return LayoutInflater.from(this)
}

fun ViewGroup.inflater(): LayoutInflater {
    return LayoutInflater.from(this.context)
}

/**
 * 改变窗体的透明度
 * @param f 透明度 0.0~1.0
 *
 * @Author: Lau
 * @Date: 2022/7/13 12:07 下午
 */
fun Activity?.windowAlpha(f: Float = 1.0f) {
    this?.let {
        val winAttrs = it.window.attributes
        winAttrs.alpha = f
        it.window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            attributes = winAttrs
        }
    }
}

