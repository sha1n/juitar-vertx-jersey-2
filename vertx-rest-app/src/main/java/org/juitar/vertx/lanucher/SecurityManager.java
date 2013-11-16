package org.juitar.vertx.lanucher;

import java.security.AccessControlContext;
import java.security.Permission;

/**
 * @author sha1n
 * Date: 11/16/13
 */
public class SecurityManager extends java.lang.SecurityManager {

    @Override
    public void checkPermission(Permission perm) {
        // Allow all by default
    }


    @Override
    public void checkPermission(Permission perm, Object context) {
        checkIfPlatformPermission(perm, context);
    }

    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("Process spawning is not allowed in this context");
    }

    @Override
    public void checkExit(int status) {
        throw new SecurityException("Exit is not allowed in this context");
    }

    private static boolean checkIfPlatformPermission(Permission perm, Object context) {
        boolean checked = false;

        if (perm instanceof ApplicationPermission) {
            if (context instanceof AccessControlContext) {
                ((AccessControlContext) context).checkPermission(perm);
                checked = true;
            } else {
                throw new SecurityException("access denied " + perm);
            }
        }

        return checked;
    }
}
