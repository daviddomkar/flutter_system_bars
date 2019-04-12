import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

class FlutterSystemBars {
  static const MethodChannel _channel = const MethodChannel('flutter_system_bars');

  static Future<bool> get hasSoftwareNavigationBar async {
    final bool hasSoftwareNavigationBar = await _channel.invokeMethod('hasSoftwareNavigationBar');
    return hasSoftwareNavigationBar;
  }

  static Future<int> get softwareNavigationBarPhysicalHeight async {
    final int softwareNavigationBarPhysicalHeight =
        await _channel.invokeMethod('softwareNavigationBarPhysicalHeight');
    return softwareNavigationBarPhysicalHeight;
  }

  static Future<int> get statusBarPhysicalHeight async {
    final int statusBarPhysicalHeight = await _channel.invokeMethod('statusBarPhysicalHeight');
    return statusBarPhysicalHeight;
  }
}

class SystemBarsInfo {
  SystemBarsInfo(this.hasSoftwareNavigationBar, this.navigationBarHeight, this.statusBarHeight);

  final bool hasSoftwareNavigationBar;
  final double navigationBarHeight;
  final double statusBarHeight;
}

typedef SystemBarsInfoProviderWidgetBuilder = Widget Function(
    BuildContext context, SystemBarsInfo sustemBarsInfo, Orientation orientation);

class SystemBarsInfoProvider extends StatefulWidget {
  const SystemBarsInfoProvider({
    Key key,
    @required this.builder,
  })  : assert(builder != null),
        super(key: key);

  final SystemBarsInfoProviderWidgetBuilder builder;

  @override
  _SystemBarsInfoProviderState createState() => _SystemBarsInfoProviderState();
}

class _SystemBarsInfoProviderState extends State<SystemBarsInfoProvider> {
  SystemBarsInfo _systemBarsInfo = SystemBarsInfo(false, 0.0, 0.0);

  @override
  void initState() {
    super.initState();
    getSystemBarsInfo(context);
  }

  Future<void> getSystemBarsInfo(BuildContext context) async {
    SystemBarsInfo systemBarsInfo;

    double devicePixelRatio = MediaQuery.of(context).devicePixelRatio;

    try {
      systemBarsInfo = SystemBarsInfo(
          await FlutterSystemBars.hasSoftwareNavigationBar,
          await FlutterSystemBars.softwareNavigationBarPhysicalHeight / devicePixelRatio,
          await FlutterSystemBars.statusBarPhysicalHeight / devicePixelRatio);
    } on PlatformException {
      systemBarsInfo = SystemBarsInfo(false, 0.0, 0.0);
    }

    if (!mounted) return;

    setState(() {
      _systemBarsInfo = systemBarsInfo;
    });
  }

  @override
  Widget build(BuildContext context) {
    return OrientationBuilder(
      builder: (context, orientation) {
        getSystemBarsInfo(context);
        return this.widget.builder(context, _systemBarsInfo, orientation);
      },
    );
  }
}

class SystemBarsMarginBox extends StatelessWidget {
  final Widget child;
  final bool portraitMargin;
  final bool landscapeMargin;
  final bool navigationBarMargin;
  final bool statusBarMargin;

  const SystemBarsMarginBox(
      {Key key,
      this.child,
      this.portraitMargin = true,
      this.landscapeMargin = false,
      this.navigationBarMargin = true,
      this.statusBarMargin = true})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SystemBarsInfoProvider(builder: (context, systemBarsInfo, orientation) {
      return Container(
        margin: EdgeInsets.only(
            left: 0.0,
            top: ((portraitMargin && orientation == Orientation.portrait) ||
                        (landscapeMargin && orientation == Orientation.landscape)) &&
                    statusBarMargin
                ? systemBarsInfo.statusBarHeight
                : 0.0,
            right: landscapeMargin && orientation == Orientation.landscape && navigationBarMargin
                ? systemBarsInfo.navigationBarHeight
                : 0.0,
            bottom: portraitMargin && orientation == Orientation.portrait && navigationBarMargin
                ? systemBarsInfo.navigationBarHeight
                : 0.0),
        child: this.child,
      );
    });
  }
}
