package org.juitar.jmx;

import org.juitar.server.http.vertx.VertxHttpServer;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * @author sha1n
 * Date: 2/2/13
 */
@ManagedResource(
        objectName = "org.juitar.vertx.http.server:name=VertxHttpServer",
        description = "Vert.x HTTP Server")
public class HttpServerJmxWrapper {

    private VertxHttpServer httpServer;

    public final void setHttpServer(VertxHttpServer httpServer) {
        this.httpServer = httpServer;
    }


    @ManagedAttribute(description = "HTTP Listener port number")
    public int getPort() {
        return httpServer.getPort();
    }

    @ManagedOperation(description = "Starts the HTTP server if it is not already started.")
    public void start() {
        httpServer.start();
    }

    @ManagedOperation(description = "Stops the HTTP server if it is currently running.")
    public void stop() {
        httpServer.stop();
    }
}
