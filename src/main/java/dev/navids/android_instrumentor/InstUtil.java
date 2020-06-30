package dev.navids.android_instrumentor;

import soot.*;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.*;

import java.util.ArrayList;
import java.util.List;

public class InstUtil {

    public static void addLogStmt(Unit u, Body b, String msg, String tag, boolean insertBefore) {
        List<Unit> generated = new ArrayList<Unit>();
        Value logMessage = StringConstant.v(msg);
        Value logType = StringConstant.v(tag);
        Value logMsg = logMessage;
        SootMethod sm = Scene.v().getMethod("<android.util.Log: int i(java.lang.String,java.lang.String)>");
        StaticInvokeExpr invokeExpr = Jimple.v().newStaticInvokeExpr(sm.makeRef(), logType, logMsg);
        generated.add(Jimple.v().newInvokeStmt(invokeExpr));
        if (insertBefore) {
            b.getUnits().insertBefore(generated, u);
        } else {
            b.getUnits().insertAfter(generated, u);
        }
        b.validate();
    }
}
