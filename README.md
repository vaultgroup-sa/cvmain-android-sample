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
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.18.1'
    implementation fileTree(dir: "libs", include: ["*.aar"])
   }
   ```

### Step 2: Sync the Project

After completing the above steps, sync your project with Gradle files to ensure all dependencies are correctly recognized.

### Step 3: Configure cvmain

Before starting the application, ensure that the necessary configurations are properly set up. Below
are the details of the configuration parameters and how to initialize them:

The `CvMainConfiguration` and `CvMaster` defines the main configuration settings for the system.

### CvMainConfiguration Builder Parameters

- **`localServer`**:  
  Specifies the address of the local server.  
  Example: `"127.0.0.1:8888"`

- **`mapping`**:  
  Defines the locker column mapping. Specifies the number of lockers in each column.  
  **Note**: Maximum of 16 columns and 6 lockers per column.  
  Example: `new int[]{4, 6, 8}`

- **`useCvLocks`**:  
  Specifies whether the system uses VG's custom in-house lock mechanism.
  - `true`: Use the in-house lock mechanism.
  - `false`: Use a standard lock.  
    Example: `false`

- **`useMultistateSlave`**:  
  Specifies whether to use multi-state slave firmware.
  - `true`: Use multi-state slave boards.
  - `false`: Use single-state systems.  
    Example: `false`

- **`useKeypad`**:  
  Specifies whether the system uses a numeric keypad. Enabling this option connects a keypad to the
  VG master board.  
  Example: `false`

### CvMasterConfiguration Builder Parameters

- **`setTcp485Passthrough`**:  
  Specifies the TCP 485 passthrough address.
  Example: "192.168.8.3:2320"

### Example Initialization

```java
CvMainConfiguration configuration = new CvMainConfiguration.Builder()
        .comms("net")
        .localServer(new CvMainConfiguration.LocalServer("0.0.0.0:7777"))
        .mapping(new int[]{6})
        .useCvLocks(false)
        .useMultistateSlave(false)
        .useKeypad(false)
        .build();

CvMasterConfiguration cvMasterConfiguration = new CvMasterConfiguration.Builder()
        .setTcp485Passthrough("192.168.8.3:2320")
        .build();

AuthConfiguration authConfiguration = new AuthConfiguration.Builder()
        .username("username")
        .password("password")
        .build();

```
### Usage

To start the CvMain service, use CvMainService. It is essential to configure and start CvMain only
once during the application's lifecycle.\
A common approach is to extend the Application class and start the process in the onCreate() method. Here’s an example:

```java
public class YourApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

      CvMainConfiguration configuration = new CvMainConfiguration.Builder()
              .comms("net")
              .localServer(new CvMainConfiguration.LocalServer("0.0.0.0:7777"))
              .mapping(new int[]{6})
              .useCvLocks(false)
              .useMultistateSlave(false)
              .useKeypad(false)
              .build();

      CvMasterConfiguration cvMasterConfiguration = new CvMasterConfiguration.Builder()
              .setTcp485Passthrough("192.168.8.3:2320")
              .build();

      AuthConfiguration authConfiguration = new AuthConfiguration.Builder()
              .username("username")
              .password("password")
              .build();

      CvMainService.configure(getApplicationContext(), configuration, cvMasterConfiguration, authConfiguration);
      MqttRunner.getInstance().startProcess(getApplicationContext(), (topicSplit, s) -> new Mqtt.RecvMsg(true));
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
For Flutter apps, use a `MethodChannel` to communicate with the `CvMainService`. Example:

```java
MethodChannel channel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "com.example/cvmain_service");
channel.setMethodCallHandler((call, result) -> {
    if (call.method.equals("startService")) {
        try {
            CvMainConfiguration mainConfig = new CvMainConfiguration.Builder()
                .comms("net")
                .localServer(new CvMainConfiguration.LocalServer("0.0.0.0:7777"))
                .mapping(new int[]{25, 3, 25, 7})
                .useCvLocks(false)
                .useMultistateSlave(false)
                .useKeypad(false)
                .build();

            CvMasterConfiguration masterConfig = new CvMasterConfiguration.Builder()
                .setTcp485Passthrough("192.168.8.3:2320")
                .build();

            AuthConfiguration authConfig = new AuthConfiguration.Builder()
                .username(call.argument("username"))
                .password(call.argument("password"))
                .build();

            CvMainService.configure(getApplicationContext(), mainConfig, masterConfig, authConfig, false);
            result.success(true);
        } catch (Exception e) {
            result.error("CONFIG_ERROR", "Failed to configure CvMainService", e.getMessage());
        }
    } else if (call.method.equals("stopService")) {
        CvMainService.stop();
        result.success(true);
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

  static Future<bool> startService(String username, String password) async {
    try {
      final bool result = await platform.invokeMethod('startService', {
        'username': username,
        'password': password,
      });
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
}
```
