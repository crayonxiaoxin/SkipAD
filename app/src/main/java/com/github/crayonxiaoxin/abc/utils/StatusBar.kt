package com.github.crayonxiaoxin.abc.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.github.crayonxiaoxin.abc.App

object StatusBar {
    /**
     * @param decorFitsSystemWindows 状态栏是否覆盖内容，true=不覆盖
     * @param darkIcons 状态栏文字图标颜色，true=暗色
     * @param fullscreen 状态栏是否隐藏，true=隐藏
     */
    fun fitSystemBar(
        activity: Activity,
        decorFitsSystemWindows: Boolean = true,
        darkIcons: Boolean = false,
        fullscreen: Boolean = false
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        val window = activity.window
        val decorView = window.decorView
        // false 状态栏覆盖在 window 之上，true 不会覆盖
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, decorFitsSystemWindows)
        } else {
            val decorFitsFlags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            val sysUiVis = decorView.systemUiVisibility
            decorView.systemUiVisibility =
                if (decorFitsSystemWindows) sysUiVis and decorFitsFlags.inv() else sysUiVis or decorFitsFlags
        }
        // 绘制 状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        // 设置 状态栏文字图标 颜色
        val windowInsetsController = ViewCompat.getWindowInsetsController(decorView)
        if (windowInsetsController != null) {
            windowInsetsController.isAppearanceLightStatusBars = darkIcons
        } else {
            var visibility = decorView.systemUiVisibility
            visibility = if (darkIcons) {
                visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                visibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = visibility
        }
        // 设置 是否全屏
        if (fullscreen && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val attributes = window.attributes
            attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = attributes
        }
    }

    // 获取状态栏高度
    val height: Int
        get() {
            var result = 0
            val context = App.appContext
            val resourceId =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    val navigationHeight: Int
        get() {
            var result = 0
            val context = App.appContext
            val resourceId =
                context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }
}

fun Activity.fitSystemBar(
    decorFitsSystemWindows: Boolean = true,
    darkIcons: Boolean = false,
    fullscreen: Boolean = false
) {
    StatusBar.fitSystemBar(this, decorFitsSystemWindows, darkIcons, fullscreen)
}