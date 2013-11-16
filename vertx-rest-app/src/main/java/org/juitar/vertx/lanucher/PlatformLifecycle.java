package org.juitar.vertx.lanucher;

import java.lang.SecurityManager;
import java.security.AccessControlContext;
import java.security.AccessController;

/**
 * @author sha1n
 * Date: 11/15/13
 */
public class PlatformLifecycle implements Lifecycle {

    private static final PlatformLifecycle LIFECYCLE = new PlatformLifecycle();
    private static final LifecyclePermission CHANGE_PHASE_PERMISSION = new LifecyclePermission("toPhase");

    private volatile Phase currentPhase = Phase.GENESIS;

    public static Lifecycle get() {
        return LIFECYCLE;
    }

    public static Lifecycle toPhase(Phase to) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            AccessControlContext acc = AccessController.getContext();
            securityManager.checkPermission(CHANGE_PHASE_PERMISSION, acc);
        }
        LIFECYCLE.currentPhase = to;
        return LIFECYCLE;
    }

    @Override
    public boolean isOperational() {
        return currentPhase.isOperational();
    }

    @Override
    public boolean isShuttingDown() {
        return currentPhase.isShuttingDown();
    }

    @Override
    public boolean isStartingUp() {
        return currentPhase.isStartingUp();
    }

    @Override
    public Phase getPhase() {
        return currentPhase;
    }

    private PlatformLifecycle() {
    }
}
