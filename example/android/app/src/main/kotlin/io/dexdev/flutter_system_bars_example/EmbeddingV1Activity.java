package io.dexdev.flutter_system_bars_example;

import android.os.Bundle;
import io.flutter.app.FlutterActivity;
import io.dexdev.flutter_system_bars.FlutterSystemBarsPlugin;

public class EmbeddingV1Activity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlutterSystemBarsPlugin.registerWith(registrarFor("io.dexdev.flutter_system_bars.FlutterSystemBarsPlugin"));
    }
}