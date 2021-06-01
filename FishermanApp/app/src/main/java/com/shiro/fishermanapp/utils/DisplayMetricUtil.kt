package com.shiro.fishermanapp.utils

import android.app.Activity
import android.util.DisplayMetrics

class DisplayMetricUtil(activity: Activity) {
    private val displayMetrics = DisplayMetrics()

    init {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(displayMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager?.defaultDisplay
            @Suppress("displayMetrics")
            display?.getMetrics(displayMetrics)
        }
    }

    fun getWidth() = displayMetrics.widthPixels
    fun getHeight() = displayMetrics.heightPixels
}