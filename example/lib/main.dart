import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_system_bars/flutter_system_bars.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle(
      statusBarColor: Colors.transparent,
      systemNavigationBarColor: Colors.transparent,
    ));

    return MaterialApp(
      home: Scaffold(
        resizeToAvoidBottomInset: false,
        body: Container(
          decoration: BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.centerLeft,
              end: Alignment.centerRight,
              stops: [0.0, 1.0],
              colors: [Color.fromARGB(255, 223, 61, 139), Color.fromARGB(255, 86, 147, 219)],
            ),
          ),
          child: LayoutBuilder(
            builder: (BuildContext context, BoxConstraints viewportConstraints) {
              return SingleChildScrollView(
                child: ConstrainedBox(
                    constraints: BoxConstraints(
                      minHeight: viewportConstraints.maxHeight,
                    ),
                    child: IntrinsicHeight(
                      child: SystemBarsMarginBox(
                          child: Column(
                              crossAxisAlignment: CrossAxisAlignment.stretch,
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                            Text('Content is below',
                                textAlign: TextAlign.center, style: TextStyle(color: Colors.white, fontSize: 20)),
                            Text('While the app occupies the entire screen.',
                                textAlign: TextAlign.center, style: TextStyle(color: Colors.white, fontSize: 20)),
                            Text('and above system bars.',
                                textAlign: TextAlign.center, style: TextStyle(color: Colors.white, fontSize: 20)),
                          ])),
                    )),
              );
            },
          ), /*SystemBarsMarginBox(
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
          ),*/
        ),
      ),
    );
  }
}
