# flutter_system_bars (WIP)

Flutter plugin that helps you create immersive experience for your app by defining margins based on system bars dimensions.

![Screenshot](https://i.imgur.com/tfOu1bF.png)

## Usage

This plugin has two main widgets:

### SystemBarsInfoProvider

This widget provides info about system bar heights and if software navigation bar is present to its descendants.

```dart
SystemBarsInfoProvider(
  builder: (context, systemBarsInfo, orientation) {
    return Center(
      child: Text(systemBarsInfo.navigationBarHeight, 
        textAlign: TextAlign.center, 
        style: TextStyle(color: Colors.white, fontSize: 20)
      ),
    );
  }
);
```

You have to pass in the builder in which you get SystemBarsInfo object and device Orientation for convenience.

SystemBarsInfo contains information about system bars:

```dart
class SystemBarsInfo {
  final bool hasSoftwareNavigationBar;
  final double navigationBarHeight;
  final double statusBarHeight;
}
```

### SystemBarsMarginBox

This widgets encapsulates SystemBarsInfoProvider and lets you choose which margins you want to show based on system bars in specified orientations.

```dart
SystemBarsMarginBox(
  portraitMargin: true,
  landscapeMargin: false,
  navigationBarMargin: true,
  statusBarMargin: true,
  child: Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text('Content is below',
            textAlign: TextAlign.center,
            style: TextStyle(color: Colors.white, fontSize: 20)),
        Text('While the app occupies the entire screen.',
            textAlign: TextAlign.center,
            style: TextStyle(color: Colors.white, fontSize: 20)),
        Text('and above system bars.',
            textAlign: TextAlign.center,
            style: TextStyle(color: Colors.white, fontSize: 20)),
      ]),
);
```
SystemBarsMarginBox settings and their default values:

portraitMargin - default: true - specifies if margin is enabled in portrait mode
landscapeMargin - default: false - specifies if margin is enabled in landscape mode
navigationBarMargin - default: true - specifies if margin is enabled for navigation bar
portraitMargin - default: true - specifies if margin is enabled for status bar

## Disclaimer

Plugin is currently implemented only for Android platform. On iOS it reports system bar heights as 0 and navigation bar as non existent. That is because I am a poor student who doesn´t have money for iPhone and Mac so I can't write iOS implementation. If you want to implement iOS functionality please open a pull request.
