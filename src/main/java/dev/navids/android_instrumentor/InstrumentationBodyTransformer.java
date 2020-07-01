package dev.navids.android_instrumentor;

import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;

import java.util.*;
import java.util.stream.Collectors;

public abstract class InstrumentationBodyTransformer extends BodyTransformer {

    private String packageName;
    private boolean isWhiteList = false;
    private Set<String> instrumentedMethodNames = new HashSet<>();
    private Set<String> instrumentedMethodSubsignatures = new HashSet<>();
    private Set<String> instrumentedMethodSignatures = new HashSet<>();



    static String INSTRUMENTATION_TAG = "ANDROID_SOOT_INSTRUMENT";

    InstrumentationBodyTransformer(String packageName, List<String> instrumentedMethodIdentifiers, String tag) {
        this.packageName = packageName;
        INSTRUMENTATION_TAG = tag;
        if(instrumentedMethodIdentifiers != null){
            isWhiteList = true;
            instrumentedMethodNames.addAll(instrumentedMethodIdentifiers.stream().filter(Util::isMethodName).collect(Collectors.toList()));
            instrumentedMethodSubsignatures.addAll(instrumentedMethodIdentifiers.stream().filter(Util::isMethodSubsignature).collect(Collectors.toList()));
            instrumentedMethodSignatures.addAll(instrumentedMethodIdentifiers.stream().filter(Util::isMethodSignature).collect(Collectors.toList()));
        }
    }

    @Override
    protected void internalTransform(Body body, String arg0, @SuppressWarnings("rawtypes") Map arg1) {
        JimpleBody jimpleBody = (JimpleBody) body;
        if (!shouldSkipInstrumentationCriteria(body)) {
            Iterator<Unit> i = body.getUnits().snapshotIterator();
            while (i.hasNext()) {
                Stmt stmt = (Stmt) i.next();
                boolean injected = injectCode(stmt, jimpleBody);
//                if (injected) {
//                    System.out.println("-- Code Injected in " + body.getMethod().getSignature());
//                    break;
//                }
            }
        }
    }

    protected abstract boolean injectCode(Stmt stmt, JimpleBody body);

    protected boolean shouldSkipInstrumentationCriteria(Body body) {
        if(isWhiteList){
            if(instrumentedMethodNames.contains(body.getMethod().getName()))
                return false;
            if(instrumentedMethodSubsignatures.contains(body.getMethod().getSubSignature()))
                return false;
            if(instrumentedMethodSignatures.contains(body.getMethod().getSignature()))
                return false;
            return true;
        }
        return !body.getMethod().getDeclaringClass().getPackageName().startsWith(packageName);
    }

}
