package com.example.famousapp.famous.utils.display

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.WindowManager
import javax.inject.Inject

object ScreenUtils {



    fun getScreenHeight(context: Context): Int {
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    @JvmStatic
    fun getScreenWidth(context: Context?): Int {
        var context = context
        if (context == null) context = context
        val windowManager =
            context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }



}