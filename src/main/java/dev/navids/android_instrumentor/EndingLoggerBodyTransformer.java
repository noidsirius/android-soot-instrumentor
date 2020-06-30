package dev.navids.android_instrumentor;

import soot.jimple.JimpleBody;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;

import java.util.List;

public class EndingLoggerBodyTransformer extends InstrumentationBodyTransformer {

    EndingLoggerBodyTransformer(String packageName, List<String> instrumentedMethodIdentifiers, String tag) {
        super(packageName, instrumentedMethodIdentifiers, tag);
    }

    @Override
    protected boolean injectCode(Stmt stmt, JimpleBody body) {
        if ( !(stmt instanceof ReturnStmt) && !(stmt instanceof ReturnVoidStmt))
            return false;
        String message = String.format("Ending of method %s", body.getMethod().getSignature());
        InstUtil.addLogStmt(stmt, body, message, INSTRUMENTATION_TAG , true);
        body.validate();
        return true;
    }
}
