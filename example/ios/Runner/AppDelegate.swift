import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?
  ) -> Bool {
    let controller : FlutterViewController = window?.rootViewController as! FlutterViewController
    let channel = FlutterMethodChannel(name: "flutter_system_bars", binaryMessenger: controller)
    channel.setMethodCallHandler({
        (call: FlutterMethodCall, result: FlutterResult) -> Void in
        
    })
    channel.setMethodCallHandler({
        [weak self] (call: FlutterMethodCall, result: FlutterResult) -> Void in
        guard call.method == "hasSoftwareNavigationBar"
            else {
                result(FlutterMethodNotImplemented)
                return
        }
        self?.hasSoftKeys(result: result)
        guard call.method == "softwareNavigationBarPhysicalHeight"
            else {
                result(FlutterMethodNotImplemented)
                return
        }
        self?.getNavBarHeight(result: result)
        guard call.method == "statusBarPhysicalHeight"
            else {
                result(FlutterMethodNotImplemented)
                return
        }
        self?.getStatusBarHeight(result: result)
    })
    GeneratedPluginRegistrant.register(with: self)
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
    
    private func hasSoftKeys(result: FlutterResult) {
        result(true)
    }
    
    private func getNavBarHeight(result: FlutterResult) {
        if #available(iOS 11.0, *) {
            result(window?.safeAreaInsets.bottom)
        } else {
            // Fallback on earlier versions
            result(0)
        }
    }
    
    private func getStatusBarHeight(result: FlutterResult) {
        if #available(iOS 11.0, *) {
            result(window?.safeAreaInsets.top)
        } else {
            result(0)
            // Fallback on earlier versions
        }
    }
}

