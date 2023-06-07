package com.github.crayonxiaoxin.abc.utils

import android.util.Log

object Logger {
    var enable: Boolean = true
    const val defaultTag = "Logger"
    fun provide(enable: Boolean) {
        this.enable = enable
    }
}

fun _v(tag: String = Logger.defaultTag, msg: String = "") {
    if (Logger.enable) Log.v(tag, msg)
}

fun _d(tag: String = Logger.defaultTag, msg: String = "") {
    if (Logger.enable) Log.d(tag, msg)
}

fun _i(tag: String = Logger.defaultTag, msg: String = "") {
    if (Logger.enable) Log.i(tag, msg)
}

fun _w(tag: String = Logger.defaultTag, msg: String = "") {
    if (Logger.enable) Log.w(tag, msg)
}

fun _e(tag: String = Logger.defaultTag, msg: String = "") {
    if (Logger.enable) Log.e(tag, msg)
}

fun _wtf(tag: String = Logger.defaultTag, msg: String = "") {
    if (Logger.enable) Log.wtf(tag, msg)
}