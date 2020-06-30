package dev.navids.android_instrumentor;

import soot.jimple.JimpleBody;
import soot.jimple.Stmt;

import java.util.List;

public class BeginningLoggerBodyTransformer extends InstrumentationBodyTransformer {

    BeginningLoggerBodyTransformer(String packageName, List<String> instrumentedMethodIdentifiers, String tag) {
        super(packageName, instrumentedMethodIdentifiers, tag);
    }

    @Override
    protected boolean injectCode(Stmt stmt, JimpleBody body) {
        if (stmt != body.getFirstNonIdentityStmt())
            return false;
        String message = String.format("Beginning of method %s", body.getMethod().getSignature());
        InstUtil.addLogStmt(stmt, body, message, INSTRUMENTATION_TAG , true);
        body.validate();
        return true;
    }
}
