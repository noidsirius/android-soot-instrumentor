# Android Isntrumentor

- **WARNING:** By running this command, all files in the output directory will be removed.

## Setup
- Build the jar file: ``./gradlew clean jar`` (the jar is located in `build/libs/android-instrumentor-1.0-SNAPSHOT.jar`)

## Execute
Run `./instrument.sh` with the following options
- `-a <APK_PATH>`: APK Path (Required)
- `-o <OUT_DIR>`: Output directory (the instrumented APK will be located here)
- `-k <KEY_PATH>`: Key path (to sign the APK) 
    - `-p <PASS>`: Keystore pass
- `-j <JAR_PATH>`: Instrumentor JAR file (default=`build/libs/android-instrumentor-1.0-SNAPSHOT.jar`)
- `-b <BUILDTOOL_PATH>`: The path to Android build tools which contains `zipsign` and `apksigner` executable files (default=`~/Library/Android/sdk/build-tools/28.0.3`)

### Example
The following command instrument `app.apk` using the JAR located in `inst.jar`, then sign it with the key in `~/.android/debug.keystore` with password `android`, and put the final apk in `/tmp/instrumented/app.apk`.      

```
./instrument.sh -j inst.jar -a app.apk -k ~/.android/debug.keystore -p android -o /tmp/instrumented
``` 

