package org.juitar.vertx.lanucher;

/**
 * @author sha1n
 * Date: 11/15/13
 */
public interface Lifecycle {

    boolean isOperational();

    boolean isShuttingDown();

    boolean isStartingUp();

    Phase getPhase();


}
