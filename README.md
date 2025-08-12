# cvmain Library Integration Manual

This document provides detailed instructions for integrating the `cvmain` library into your Android project.

## Prerequisites

You will need the following files for successful integration:

1. **Android Library**: Located at `app/libs/cvmain.aar`

**Supported ABI**: `armeabi-v7a`, `arm64-v8a`

## Setup Instructions

### Step 1: Add the cvmain Library and jackson dependency

1. Copy the `cvmain.aar` file into the `libs` folder of your project.
2. Open AndroidManifest.xml and add the following lines:

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

3. Open your app-level `build.gradle` file and add the following lines:

   ```groovy

   android {
        packaging {
            jniLibs {
                useLegacyPackaging true
            }
        }
   }

   dependencies {

    implementation 'com.fasterxml.jackson.core:jackson-databind:2.13.4'
    implementation 'com.squareup.okhttp3:okhttp:4.9.1'
    implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.0'
    implementation 'io.grpc:grpc-okhttp:1.47.0'
    implementation 'io.grpc:grpc-protobuf-lite:1.47.0'
    implementation 'io.grpc:grpc-stub:1.47.0'
    implementation 'org.apache.commons:commons-lang3:3.12.0'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.13.4'
    implementation fileTree(dir: "libs", include: ["*.aar"])
   }
   ```

### Step 2: Sync the Project

After completing the above steps, sync your project with Gradle files to ensure all dependencies are correctly recognized.

### Step 3: Configuration

Configuration is now handled through a dedicated activity. Use the following intent to open the
configuration screen:

#### Opening Configuration Activity

From an Activity:

```java
Intent intent = new Intent(this, co.za.vaultgroup.cvmain_android.ui.CvConfigurationActivity.class);

startActivity(intent);
```

From a Fragment:
```java
Intent intent = new Intent(getActivity(), co.za.vaultgroup.cvmain_android.ui.CvConfigurationActivity.class);

startActivity(intent);
```

#### Example: Adding Configuration Button to MainActivity

```java
public class MainActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // Add a button to open configuration
      Button configButton = findViewById(R.id.config_button);
      configButton.setOnClickListener(v -> openConfiguration());
   }

   private void openConfiguration() {
      Intent intent = new Intent(this, co.za.vaultgroup.cvmain_android.ui.CvConfigurationActivity.class);
      startActivity(intent);
   }
}
```

After pressing save, the configuration is saved and cvmain restarts with the updated information.
The same applies to unit registration.

![Configuration Screen](Screenshot%201.png)
![Unit Registration](Screenshot%202.png)
### Usage

To start the CvMain service, use CvMainService. It is essential to start CvMain only
once during the application's lifecycle.\
A common approach is to extend the Application class and start the process in the onCreate() method. Here’s an example:

```java
public class YourApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

       CvMainService.start(getApplicationContext(), false);
       System.out.println("Lib version " + CvMainService.getLibVersion());
    }
}
```

### Stopping the CvMain Process
To stop the CvMain process, call:
```
CvMainService.stop();
```

### Debug
```
adb logcat | grep cvmain-android
```


### Integration with Flutter

For Flutter apps, use a `MethodChannel` to communicate with the `CvMainService` and open
configuration activities. Example:

```java
MethodChannel channel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "com.example/cvmain_service");
channel.setMethodCallHandler((call, result) -> {
    if (call.method.equals("startService")) {
        try {
        CvMainService.

start(getApplicationContext());
            result.success(true);
        } catch (Exception e) {
        result.

error("START_ERROR","Failed to start CvMainService",e.getMessage());
        }
    } else if (call.method.equals("stopService")) {
        CvMainService.stop();
        result.success(true);
    }else if(call.method.

equals("openConfiguration")){
        try{
Intent intent = new Intent(this, co.za.vaultgroup.cvmain_android.ui.CvConfigurationActivity.class);

startActivity(intent);
            result.

success(true);
        }catch(
Exception e){
        result.

error("CONFIG_ERROR","Failed to open configuration activity",e.getMessage());
        }
    } else {
        result.notImplemented();
    }
});
```

Corresponding Flutter code:

```dart
import 'package:flutter/services.dart';

class CvMainService {
  static const platform = MethodChannel('com.example/cvmain_service');

  static Future<bool> startService() async {
    try {
       final bool result = await platform.invokeMethod('startService');
      return result;
    } catch (e) {
      print('Error starting CvMainService: $e');
      return false;
    }
  }

  static Future<bool> stopService() async {
    try {
      final bool result = await platform.invokeMethod('stopService');
      return result;
    } catch (e) {
      print('Error stopping CvMainService: $e');
      return false;
    }
  }

  static Future<bool> openConfiguration() async {
     try {
        final bool result = await platform.invokeMethod('openConfiguration');
        return result;
     } catch (e) {
        print('Error opening configuration: $e');
        return false;
     }
  }
}
```

#### Example Usage in Flutter Widget:

```dart
class MyApp extends StatelessWidget {
   @override
   Widget build(BuildContext context) {
      return MaterialApp(
         home: Scaffold(
            appBar: AppBar(title: Text('CvMain Example')),
            body: Center(
               child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                     ElevatedButton(
                        onPressed: () async {
                           bool success = await CvMainService.startService();
                           print('Start service: $success');
                        },
                        child: Text('Start Service'),
                     ),
                     SizedBox(height: 20),
                     ElevatedButton(
                        onPressed: () async {
                           bool success = await CvMainService.openConfiguration();
                           print('Open configuration: $success');
                        },
                        child: Text('Open Configuration'),
                     ),
                     SizedBox(height: 20),
                     ElevatedButton(
                        onPressed: () async {
                           bool success = await CvMainService.stopService();
                           print('Stop service: $success');
                        },
                        child: Text('Stop Service'),
                     ),
                  ],
               ),
            ),
         ),
      );
   }
}
```
