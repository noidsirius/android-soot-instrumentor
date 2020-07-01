package dev.navids.android_instrumentor;


import org.apache.commons.cli.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import soot.jimple.infoflow.android.manifest.ProcessManifest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private final static String USER_HOME = System.getProperty("user.home");
    private static String androidJar = USER_HOME + "/Library/Android/sdk/platforms";

    private static Options createClOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "Show this help.");
        options.addOption(Option.builder("platform")
                .desc("the directory containing android platforms").hasArg().argName("platform").build());
        options.addOption(Option.builder("apk")
                .desc("the APK path").hasArg().argName("apk").required().build());
        options.addOption(Option.builder("output")
                .desc("the output directory").hasArg().argName("output").required().build());
        options.addOption(Option.builder("tag")
                .desc(String.format("the logging tag (default='%s')", InstrumentationBodyTransformer.INSTRUMENTATION_TAG))
                .hasArg().argName("tag").build());
        options.addOption(Option.builder("b").desc("logs methods' beginning").build());
        options.addOption(Option.builder("e").desc("logs methods' ending").build());
        options.addOption(Option.builder("l")
                .desc("the list of methods (signature, subsignature, or name) to be instrumented separated by semicolon (default=all methods)").hasArg()
                .argName("instrumented_methods").build());
        return options;
    }

    public static void main(String[] args) {
        // Configure log4j
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        // Parse commandline input
        CommandLineParser parser = new DefaultParser();
        final Options options = createClOptions();
        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("platform"))
                androidJar = line.getOptionValue("platform");
            String outputDir = line.getOptionValue("output");
            String apkPath = line.getOptionValue("apk");
            final ProcessManifest processManifest = new ProcessManifest(apkPath);
            String instrumentationAddress = new File(outputDir).getAbsolutePath();
            String tag = InstrumentationBodyTransformer.INSTRUMENTATION_TAG;
            if (line.hasOption("tag"))
                tag = line.getOptionValue("tag");
            String packageName = processManifest.getPackageName();
            List<String> instrumentedMethodIdentifiers = null;
            if (line.hasOption("l"))
                instrumentedMethodIdentifiers = Arrays.asList(line.getOptionValue("l").split(";"));
            List<InstrumentationBodyTransformer> transformers = new ArrayList<>();
            if(line.hasOption("b"))
                transformers.add(new BeginningLoggerBodyTransformer(packageName, instrumentedMethodIdentifiers, tag));
            if(line.hasOption("e"))
                transformers.add(new EndingLoggerBodyTransformer(packageName, instrumentedMethodIdentifiers, tag));
            Instrumentor.instrument(instrumentationAddress, transformers, apkPath, androidJar);
        } catch (ParseException exp) {
            HelpFormatter formatter = new HelpFormatter();
            System.err.println("Invalid args:" + exp.getMessage());
            formatter.printHelp("Instrumentor", options);
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
