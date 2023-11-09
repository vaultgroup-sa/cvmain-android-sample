# cvmain-android-sample
This is the cvmain library integration manual. You will need binary located in app/libs/cvmain.aar
Supported ABI: armeabi-v7a 

## Setup
Copy cvmain.aar binary to libs folder in project
Add below line in app level build.gradle
```shell
implementation fileTree(dir: "libs", include: ["*.aar"])
```

Add below line in AndroidManifest.xml

```shell
android:extractNativeLibs="true"
```

Sync project

## Usage

Initialize CvRunner and start the CvMain process
Make sure that you are starting CvRunner once per application start-up
One of the ways to achieve this is to extend the Application class and start process in onCreate()
method

```java
CvRunner cvRunner=CvRunner.getInstance();
        cvRunner.startProcess(getApplicationContext());
```

To stop the CvMain process run

```java
cvRunner.stopProcess();
```
