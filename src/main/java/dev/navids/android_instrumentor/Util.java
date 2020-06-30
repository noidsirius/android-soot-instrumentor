package dev.navids.android_instrumentor;

import java.util.regex.Pattern;

public class Util {
    private static String classPattern = "([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*";
    private static String methodPattern = "[a-zA-Z_$][a-zA-Z\\d_$]*";
    public static boolean isMethodSignature(String signature){
        Pattern methodNamePattern = Pattern.compile(String.format("<%s:\\s+%s\\s+%s\\(.*\\)>",classPattern, classPattern, methodPattern));
        return methodNamePattern.matcher(signature).matches();
    }

    public static boolean isMethodSubsignature(String subsig){
        Pattern methodNamePattern = Pattern.compile(String.format("%s\\s+%s\\(.*\\)",classPattern, methodPattern));
        return methodNamePattern.matcher(subsig).matches();
    }

    public static boolean isMethodName(String name){
        Pattern methodNamePattern = Pattern.compile(methodPattern);
        return methodNamePattern.matcher(name).matches();
    }
}
