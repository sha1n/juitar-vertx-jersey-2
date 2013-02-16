package org.juitar.vertx.lanucher;

import org.juitar.server.http.vertx.VertxHttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sha1n
 * Date: 2/2/13
 */
public class AppShutdownHook implements VertxHttpServer.ShutdownHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppShutdownHook.class);

    @Override
    public void beforeShutdown() {
        LOGGER.info("Server is shutting down...");
    }

    @Override
    public void afterShutdown() {
        LOGGER.info("Server halted!");
        System.exit(0);
    }
}
