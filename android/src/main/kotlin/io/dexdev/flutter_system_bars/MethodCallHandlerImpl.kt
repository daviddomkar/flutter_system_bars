package io.dexdev.flutter_system_bars

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import io.flutter.Log
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class MethodCallHandlerImpl(val context: Context, var activity: Activity?): MethodCallHandler {

    companion object {
        const val channelName = "flutter_system_bars"
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        Log.w(MethodCallHandlerImpl.channelName, "MethodCallHandler#onMethodCall - ${call.method}")
        Log.w(MethodCallHandlerImpl.channelName, "Has activity ${activity != null}")


        when(call.method) {
            "getPlatformVersion" -> result.success("Android ${Build.VERSION.RELEASE}")
            "hasSoftwareNavigationBar" -> result.success(hasSoftKeys())
            "softwareNavigationBarPhysicalHeight" -> result.success(getNavBarHeight())
            "statusBarPhysicalHeight" -> result.success(getStatusBarHeight())
            else -> result.notImplemented()
        }
    }

    private fun hasSoftKeys(): Boolean {
        val hasSoftwareKeys: Boolean

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val d = activity!!.windowManager.defaultDisplay

            val realDisplayMetrics = DisplayMetrics()
            d.getRealMetrics(realDisplayMetrics)

            val realHeight = realDisplayMetrics.heightPixels
            val realWidth = realDisplayMetrics.widthPixels

            val displayMetrics = DisplayMetrics()
            d.getMetrics(displayMetrics)

            val displayHeight = displayMetrics.heightPixels
            val displayWidth = displayMetrics.widthPixels

            hasSoftwareKeys = realWidth - displayWidth > 0 || realHeight - displayHeight > 0
        } else {
            val hasMenuKey = ViewConfiguration.get(activity!!).hasPermanentMenuKey()
            val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            hasSoftwareKeys = !hasMenuKey && !hasBackKey
        }
        return hasSoftwareKeys
    }

    private fun getNavBarHeight(): Int {
        val result = 0

        if (hasSoftKeys()) {
            //The device has a navigation bar
            val resources = activity!!.resources

            val orientation = resources.configuration.orientation
            val resourceId: Int = if (isTablet()) {
                resources.getIdentifier(if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_height_landscape", "dimen", "android")
            } else {
                resources.getIdentifier(if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_width", "dimen", "android")
            }

            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = activity!!.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = activity!!.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun isTablet(): Boolean {
        return activity!!.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

}