package dev.navids.android_instrumentor;

import soot.PackManager;
import soot.Scene;
import soot.Transform;
import soot.options.Options;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Instrumentor {

    public static void instrument(String output, List<InstrumentationBodyTransformer> bodyTransformers, String apk, String androidJAR) {
        final File[] files = (new File(output)).listFiles();
        if (files != null && files.length > 0) {
            Arrays.asList(files).forEach(File::delete);
        }
        initializeSoot(output, apk, androidJAR);
        bodyTransformers.forEach(bodyTransformer -> PackManager.v().getPack("jtp").add(new Transform("jtp.myAnalysis"+bodyTransformer.hashCode(), bodyTransformer)));
        PackManager.v().runPacks();
        PackManager.v().writeOutput();
        System.out.println("The APK is instrumented");
    }

    public static void initializeSoot(String output, String apk, String androidJAR) {
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_validate(true);
        Options.v().set_output_format(Options.output_format_dex);
        Options.v().set_output_dir(output);
        Options.v().set_process_dir(Collections.singletonList(apk));
        Options.v().set_android_jars(androidJAR);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_multiple_dex(true);
        Options.v().set_soot_classpath(androidJAR);
        Scene.v().loadNecessaryClasses();
    }
}
