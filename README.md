# Android Instrumentor

*Android Instrumentor* is an open-source tool for instrumenting Android APKs using [Soot](https://github.com/Sable/soot).   

## Setup
- Requires Java version below 9, e.g., 8.
- Build the jar file: ``./gradlew clean jar`` (the jar is located in `build/libs/android-instrumentor-1.0-SNAPSHOT.jar`)

## Execute
The command `java -jar build/libs/android-instrumentor-1.0-SNAPSHOT.jar -a <APK_PATH> -o <OUT_DIR> -b` instruments the app located in `<APK_PATH>` to log the execution of each method and put it in directory `<OUT_DIR>`. In order to install the instrumented app, sign it by running `./sign.sh <INSTRUMENTED_APK> <KEYSTORE> <PASS>`. 

### Example
```
java -jar build/libs/android-instrumentor-1.0-SNAPSHOT.jar -apk example/calc.apk -output instrumented -b -e -l "onCreate;boolean verify(android.content.Context,com.numix.calculator.MutableString);<com.numix.calculator.view.MatrixView: java.lang.String getSeparator(android.content.Context)>"
./sign.sh instrumented/calc.apk example/key android
adb install -r -t instrumented/calc.apk 
``` 
Notes:
- The option `-e` logs the end of methods (when their execution is finished). 
- The option `-l` determines the methods to be instrumented. In the example above, only methods are instrumented where their name is `onCreate`, or their subsignature is `boolean verify(android.content.Context,com.numix.calculator.MutableString)`, or their signature is `<com.numix.calculator.view.MatrixView: java.lang.String getSeparator(android.content.Context)>`.