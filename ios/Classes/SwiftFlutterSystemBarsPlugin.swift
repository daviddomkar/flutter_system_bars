import Flutter
import UIKit

public class SwiftFlutterSystemBarsPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "flutter_system_bars", binaryMessenger: registrar.messenger())
        let instance = SwiftFlutterSystemBarsPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
            case "hasSoftwareNavigationBar":
                hasSoftKeys(result: result)
            case "softwareNavigationBarPhysicalHeight":
                getNavBarHeight(result: result)
            case "statusBarPhysicalHeight":
                getStatusBarHeight(result: result)
            default:
                result(FlutterMethodNotImplemented)
        }
    }

    private func hasSoftKeys(result: FlutterResult) {
        result(Bool(false))
    }

    private func getNavBarHeight(result: FlutterResult) {
        if #available(iOS 11.0, *) {
            if (UIApplication.shared.delegate != nil && UIApplication.shared.delegate!.window != nil) {
                result(Int(UIApplication.shared.delegate!.window!!.safeAreaInsets.bottom * UIScreen.main.scale))
            } else {
                result(Int(0))
            }
        } else {
            result(Int(0))
        }
    }

    private func getStatusBarHeight(result: FlutterResult) {
        if #available(iOS 11.0, *) {
            if (UIApplication.shared.delegate != nil && UIApplication.shared.delegate!.window != nil) {
                result(Int(UIApplication.shared.delegate!.window!!.safeAreaInsets.top * UIScreen.main.scale))
            } else {
                result(Int(0))
            }
        } else {
            result(Int(0))
        }
    }
}
