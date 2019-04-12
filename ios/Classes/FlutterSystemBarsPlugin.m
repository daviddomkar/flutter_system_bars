#import "FlutterSystemBarsPlugin.h"
#import <flutter_system_bars/flutter_system_bars-Swift.h>

@implementation FlutterSystemBarsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterSystemBarsPlugin registerWithRegistrar:registrar];
}
@end
