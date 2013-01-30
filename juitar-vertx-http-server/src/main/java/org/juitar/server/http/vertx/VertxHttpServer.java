package org.juitar.server.http.vertx;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author sha1n
 * Date: 1/30/13
 */
public class VertxHttpServer {

    private final Handler<HttpServerRequest> requestHandler;
    private final int port;
    private final String routeMatcherPattern;
    private final ExecutorService executorService;

    public VertxHttpServer(final int port, final String routeMatcherPattern, final Handler<HttpServerRequest> requestHandler) {
        this.requestHandler = requestHandler;
        this.port = port;
        this.routeMatcherPattern = routeMatcherPattern;
        this.executorService = Executors.newFixedThreadPool(1);
    }

    public void start() {
        executorService.execute(new ServerRunnable());
    }

    public void stop() {
        executorService.shutdownNow();
    }

    private final class ServerRunnable implements Runnable {

        @Override
        public void run() {
            Vertx vertx = Vertx.newVertx();

            RouteMatcher routeMatcher = new RouteMatcher();
            routeMatcher.all(routeMatcherPattern, requestHandler);

            vertx.createHttpServer().requestHandler(routeMatcher).listen(port);
            System.out.println("HTTP Server listening on port " + port);

        }
    }
}
