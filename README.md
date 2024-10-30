# cvmain Library Integration Manual

This document provides detailed instructions for integrating the `cvmain` library into your Android project.

## Prerequisites

You will need the following files for successful integration:

1. **Android Library**: Located at `app/libs/cvmain.aar`
2. **Configuration Files**: Located in `app/src/main/assets/Archive.zip`
3. **Native Libraries**: Located in `app/src/main/jniLibs`

**Supported ABI**: `armeabi-v7a`, `arm64-v8a`

## Setup Instructions

### Step 1: Add the cvmain Library

1. Copy the `cvmain.aar` file into the `libs` folder of your project.
2. Open your app-level `build.gradle` file and add the following lines:

   ```groovy
   
   android {
        packaging {
            jniLibs {
                useLegacyPackaging true
            }
        }
   }
   
   dependencies {
  
    implementation fileTree(dir: "libs", include: ["*.aar"])
   }
   ```



### Step 2: Add Configuration Files

1. Copy the Archive.zip file to the app/src/main/assets directory.

### Step 3: Add Native Libraries

1. Copy the jniLibs directory from app/src/main/jniLibs to your project, ensuring the structure remains intact.
2. Unzip the contents of the jniLibs folder in the same directory.

### Step 4: Sync the Project

After completing the above steps, sync your project with Gradle files to ensure all dependencies are correctly recognized.

### CvMain Configuration

The Archive.zip contains configuration files for CvMain and CvMaster.

1. Unzip Archive.zip and modify the configuration files as needed.
2. Once you have completed your configuration, zip the files back up, maintaining the same structure, and ensure you do not change the name of the archive.

### Usage

To start the CvMain service, use CvMainService. It is essential to start CvMain only once during the application's lifecycle.\
A common approach is to extend the Application class and start the process in the onCreate() method. Here’s an example:

```
public class YourApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CvMainService.start(getApplicationContext());
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
