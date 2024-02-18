package com.ethanmao.klib.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.ethanmao.klib.R


/**
 *  沉浸式工具类
 */
object StatusBarHelper {
    val STATUS_BAR_VIEW = R.id.status_bar_view

    fun setUp(activity: Activity, statusColor: Int, darkMode: Boolean, isFullScreen : Boolean = false) {
        val window = activity.window
        val decorView = window.decorView as ViewGroup
        // Window 的 Flag,不同于 View
        var windowVisibility = decorView.systemUiVisibility

        //在 Android 5 上设置,5以下不做处理
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // 两个属性会冲突,需要 clear
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 设置状态栏颜色
            window.statusBarColor = statusColor
        }
        // Android 6 上设置
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            windowVisibility = if (darkMode) {
                // 白底黑字
                windowVisibility or  View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                // 黑底白字
                windowVisibility and  View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }

        if (isFullScreen){
            // 全屏,并且使得时间,电量等可见
            windowVisibility = windowVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        decorView.systemUiVisibility = windowVisibility
    }

    /**
     * 处理布局和状态栏重叠
     */
    fun with(view : View){
        view.setPadding(0, getStatusBarHeight(view.context), 0, 0)
    }

    fun getStatusBarHeight(ctx : Context) : Int {
        var statusBarHeight = 0
        val resourceId: Int = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = ctx.getResources().getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

}