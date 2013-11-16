package org.juitar.vertx.lanucher;

/**
 * @author sha1n
 * Date: 11/15/13
 */
public enum Phase {

    GENESIS,
    STARTUP,
    RUNNING,
    SHUTDOWN,
    ARMAGEDDON;

    boolean isOperational() {
        return this == RUNNING;
    }

    boolean isShuttingDown() {
        return this.ordinal() >= SHUTDOWN.ordinal();
    }

    boolean isStartingUp() {
        return this.ordinal() < RUNNING.ordinal();
    }
}
