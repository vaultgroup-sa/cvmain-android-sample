# cvmain-android-sample
This is the cvmain library integration manual.

## Setup
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
Initialize CvRunner and start process
Make sure that you are starting CvRunner once per application start-up
One of the way to achieve is to extend Application class and start in onCreate() method 

```java
 CvRunner cvRunner = CvRunner.getInstance();
 cvRunner.startProcess(getApplicationContext());
```

