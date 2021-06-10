package io.dexdev.flutter_system_bars

import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull
import io.flutter.Log
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry.Registrar

class FlutterSystemBarsPlugin: FlutterPlugin, ActivityAware {

  private var channel: MethodChannel? = null
  private var handler: MethodCallHandlerImpl? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    Log.w(MethodCallHandlerImpl.channelName, "FlutterPlugin#onAttachedToEngine")
    setupChannel(flutterPluginBinding.binaryMessenger, flutterPluginBinding.applicationContext, null)
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    Log.w(MethodCallHandlerImpl.channelName, "FlutterPlugin#onDetachedFromEngine")
    teardownChannel()
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    Log.w(MethodCallHandlerImpl.channelName, "ActivityAware#onAttachedToActivity")
    handler?.activity = binding.activity
  }

  override fun onDetachedFromActivity() {
    Log.w(MethodCallHandlerImpl.channelName, "ActivityAware#onDetachedFromActivity")
    handler?.activity = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    Log.w(MethodCallHandlerImpl.channelName, "ActivityAware#onReattachedToActivityForConfigChanges")
    handler?.activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {
    Log.w(MethodCallHandlerImpl.channelName, "ActivityAware#onDetachedFromActivityForConfigChanges")
    handler?.activity = null
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
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val plugin = FlutterSystemBarsPlugin()
      plugin.setupChannel(registrar.messenger(), registrar.context(), registrar.activity())
    }
  }

  private fun setupChannel(messenger: BinaryMessenger,  context: Context,  activity: Activity?) {
    channel = MethodChannel(messenger, MethodCallHandlerImpl.channelName);
    handler = MethodCallHandlerImpl(context, activity);
    channel?.setMethodCallHandler(handler);
  }

  private fun teardownChannel() {
    channel?.setMethodCallHandler(null);
    channel = null;
    handler = null;
  }

}
