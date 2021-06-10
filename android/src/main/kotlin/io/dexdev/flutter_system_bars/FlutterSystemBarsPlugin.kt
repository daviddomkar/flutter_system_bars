package io.dexdev.flutter_system_bars

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
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
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

class FlutterSystemBarsPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {

  private lateinit var channel: MethodChannel
  private var activityBinding: ActivityPluginBinding? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, channelName)
    channel.setMethodCallHandler(FlutterSystemBarsPlugin())
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activityBinding = binding
  }

  override fun onDetachedFromActivityForConfigChanges() {
    activityBinding = null
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  companion object {
    const val channelName = "flutter_system_bars"

    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), channelName)
      channel.setMethodCallHandler(FlutterSystemBarsPlugin())
    }
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if(activityBinding == null) {
      result.notImplemented()
      return
    }

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
      val d = activityBinding!!.activity.windowManager.defaultDisplay

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
      val hasMenuKey = ViewConfiguration.get(activityBinding?.activity).hasPermanentMenuKey()
      val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
      hasSoftwareKeys = !hasMenuKey && !hasBackKey
    }
    return hasSoftwareKeys
  }

  private fun getNavBarHeight(): Int {
    val result = 0

    if (hasSoftKeys()) {
      //The device has a navigation bar
      val resources = activityBinding!!.activity.resources

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
    val resourceId = activityBinding!!.activity.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
      result = activityBinding!!.activity.resources.getDimensionPixelSize(resourceId)
    }
    return result
  }

  private fun isTablet(): Boolean {
    return activityBinding!!.activity.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
  }

  override fun onDetachedFromActivity() {
  }

}
