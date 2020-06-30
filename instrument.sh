#!/bin/bash
ANDROID_PLATFORM=~/Library/Android/sdk/platforms
INSTRUMENTOR_JAR=build/libs/android-instrumentor-1.0-SNAPSHOT.jar
BUILDTOOLS=~/Library/Android/sdk/build-tools/28.0.3
OUTDIR="./instrumented"
while getopts j:b:k:p:o:a: option
do
case "${option}"
in
j) INSTRUMENTOR_JAR=${OPTARG};;
a) APK=${OPTARG};;
b) BUILDTOOLS=${OPTARG};;
k) KEY=${OPTARG};;
p) KEYSTORE_PASS=${OPTARG};;
o) OUTDIR=${OPTARG};;
esac
done
mkdir -p $OUTDIR
APKNAME=$(basename "$APK")
zipalign=$BUILDTOOLS/zipalign
apksigner=$BUILDTOOLS/apksigner
INSTRUMENTED_APP=$OUTDIR/$APKNAME
TMP_FILE=/tmp/tmpfile.apk
echo "Instrumenting $APKNAME..."
java -jar $INSTRUMENTOR_JAR -platform $ANDROID_PLATFORM -apk $APK -output $OUTDIR


if [ ! -z "$KEY" ]; then
  PASS_OPT="--ks-pass pass:$KEYSTORE_PASS"
  [[ -z "$KEYSTORE_PASS" ]] && PASS_OPT=""
  $zipalign -f 4 $INSTRUMENTED_APP $TMP_FILE
  $apksigner sign --ks $KEY  $PASS_OPT $TMP_FILE
  cp $TMP_FILE $INSTRUMENTED_APP
  rm $TMP_FILE
  echo "The $APKNAME is signed and located in $INSTRUMENTED_APP"
fi
