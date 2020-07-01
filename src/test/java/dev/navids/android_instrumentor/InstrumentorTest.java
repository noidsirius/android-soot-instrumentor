package dev.navids.android_instrumentor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmlpull.v1.XmlPullParserException;
import soot.*;
import soot.jimple.JimpleBody;
import soot.jimple.infoflow.android.manifest.ProcessManifest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InstrumentorTest {

    private String output;
    private String apk;
    private String androidJAR;
    private String packageName;
    private String tag;

    @BeforeEach
    void setUp() throws IOException, XmlPullParserException {
        output = System.getProperty("java.io.tmpdir");
        apk = "example/calc.apk";
        androidJAR = System.getProperty("user.home") + "/Library/Android/sdk/platforms";
        if(System.getenv().containsKey("ANDROID_HOME"))
            androidJAR = System.getenv("ANDROID_HOME");
        System.out.println("========= Android Home: " + androidJAR);
        final ProcessManifest processManifest = new ProcessManifest(apk);
        tag = InstrumentationBodyTransformer.INSTRUMENTATION_TAG;
        packageName = processManifest.getPackageName();
        assertEquals(packageName, "com.numix.calculator");

        Instrumentor.initializeSoot(output, apk, androidJAR);
        assertNotNull(Scene.v().grabMethod("<com.numix.calculator.view.MatrixView: java.lang.String getSeparator(android.content.Context)>"));
    }

    @Test
    void instrument(){
        PackManager.v().getPack("jtp").add(new Transform("jtp.begin", new BeginningLoggerBodyTransformer(packageName, null, tag)));
        PackManager.v().getPack("jtp").add(new Transform("jtp.being", new EndingLoggerBodyTransformer(packageName, null, tag)));
        PackManager.v().runPacks();
        SootMethod method = Scene.v().grabMethod("<com.numix.calculator.view.MatrixView: java.lang.String getSeparator(android.content.Context)>");
        JimpleBody body = (JimpleBody) method.getActiveBody();
        String logStr = body.getFirstNonIdentityStmt().toString();
        assertEquals(logStr, "staticinvoke <android.util.Log: int i(java.lang.String,java.lang.String)>(\"ANDROID_SOOT_INSTRUMENT\", \"Beginning of method <com.numix.calculator.view.MatrixView: java.lang.String getSeparator(android.content.Context)>\")");
    }

    @Test
    void instrumentWithFilter(){
        PackManager.v().getPack("jtp").add(new Transform("jtp.begin", new BeginningLoggerBodyTransformer(packageName, Arrays.asList("onCreate"), tag)));
        PackManager.v().getPack("jtp").add(new Transform("jtp.being", new EndingLoggerBodyTransformer(packageName, Arrays.asList("onCreate"), tag)));
        PackManager.v().runPacks();
        SootMethod method = Scene.v().grabMethod("<com.numix.calculator.view.MatrixView: java.lang.String getSeparator(android.content.Context)>");
        JimpleBody body = (JimpleBody) method.getActiveBody();
        String logStr = body.getFirstNonIdentityStmt().toString();
        assertNotEquals(logStr, "staticinvoke <android.util.Log: int i(java.lang.String,java.lang.String)>(\"ANDROID_SOOT_INSTRUMENT\", \"Beginning of method <com.numix.calculator.view.MatrixView: java.lang.String getSeparator(android.content.Context)>\")");
    }

    @AfterEach
    void tearUp(){
        G.reset();
        assertEquals(Scene.v().getClasses().size(), 0);
    }
}