package io.dexdev.flutter_system_bars

import android.app.Activity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import android.util.DisplayMetrics
import android.os.Build
import android.view.*
import android.content.res.Configuration
import android.view.KeyCharacterMap
import android.view.ViewConfiguration

class FlutterSystemBarsPlugin(private val activity: Activity): MethodCallHandler {
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "flutter_system_bars")
      channel.setMethodCallHandler(FlutterSystemBarsPlugin(registrar.activity()))
    }
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when {
        call.method == "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
        call.method == "hasSoftwareNavigationBar" -> result.success(hasSoftKeys())
        call.method == "softwareNavigationBarPhysicalHeight" -> result.success(getNavBarHeight())
        call.method == "statusBarPhysicalHeight" -> result.success(getStatusBarHeight())
        else -> result.notImplemented()
    }
  }

  private fun hasSoftKeys(): Boolean {
    val hasSoftwareKeys: Boolean

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      val d = activity.windowManager.defaultDisplay

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
      val hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey()
      val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
      hasSoftwareKeys = !hasMenuKey && !hasBackKey
    }
    return hasSoftwareKeys
  }

  private fun getNavBarHeight(): Int {
    val result = 0

    if (hasSoftKeys()) {
      //The device has a navigation bar
      val resources = activity.resources

      val orientation = resources.configuration.orientation
      val resourceId: Int
      resourceId = if (isTablet()) {
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
    val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
      result = activity.resources.getDimensionPixelSize(resourceId)
    }
    return result
  }

  private fun isTablet(): Boolean {
    return activity.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
  }
}
