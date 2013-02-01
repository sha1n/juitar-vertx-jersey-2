package org.juitar.server.http.vertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;


/**
 * @author sha1n
 * Date: 1/30/13
 */
public class VertxHttpServer {

    public interface ShutdownHook {
        void beforeShutdown();

        void afterShutdown();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(VertxHttpServer.class);
    private static final ShutdownHook DUMMY_SHUTDOWN_HOOK = new ShutdownHook() {
        @Override
        public void beforeShutdown() {
        }

        @Override
        public void afterShutdown() {
        }
    };

    private final Vertx vertx = Vertx.newVertx();
    private final HttpServer httpServer;
    private final int port;
    private final ShutdownHook shutdownHook;

    /**
     * Constructs a new VertxHttpServer instance.
     *
     * @param port the port to listen on
     * @param routeMatcher a {@link RouteMatcher} which is <em>already set</em> with appropriate {@link Handler}(s).
     */
    public VertxHttpServer(final int port, final RouteMatcher routeMatcher) {
        this(port, routeMatcher, DUMMY_SHUTDOWN_HOOK);
    }

    /**
     * Constructs a new VertxHttpServer instance.
     *
     * @param port the port to listen on
     * @param routeMatcher a {@link RouteMatcher} which is <em>already set</em> with appropriate {@link Handler}(s).
     * @param shutdownHook a {@link Runnable} object to be called when the server shuts down.
     */
    public VertxHttpServer(final int port, final RouteMatcher routeMatcher, ShutdownHook shutdownHook) {
        this.port = port;
        this.shutdownHook = shutdownHook != null ? shutdownHook : DUMMY_SHUTDOWN_HOOK;
        this.httpServer = vertx.createHttpServer();
        this.httpServer.requestHandler(routeMatcher);
    }

    /**
     * Returns the port this server is set to listen on.
     * @return int
     */
    public final int getPort() {
        return port;
    }

    /**
     * Starts the HTTP server.
     */
    public final void start() {
        httpServer.listen(port);
        LOGGER.info("HTTP Server listening on port " + port);
    }

    /**
     * Stops the HTTP server.
     */
    public final void stop() {
        try {
            shutdownHook.beforeShutdown();
        } finally {
            try {
                httpServer.close();
            } finally {
                shutdownHook.afterShutdown();
            }
        }
    }

}
