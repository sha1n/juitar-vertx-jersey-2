package org.juitar.vertx.lanucher;

import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.scanning.PackageNamesScanner;
import org.juitar.server.http.vertx.VertxHttpServer;
import org.juitar.vertx.jersey.VertxContainerHandler;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.vertx.java.core.http.RouteMatcher;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author sha1n
 * Date: 2/2/13
 */
@ManagedResource(objectName = "org.juitar.vertx.http.server:name=VertxHttpServer")
class HttpServerJmxWrapper {

    private VertxHttpServer httpServer;

    @PostConstruct
    final void initialize() throws URISyntaxException {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.registerFinder(new PackageNamesScanner(
                new String[]{
                        "org.juitar.web.rest.resources",
                }, true));


        RouteMatcher routeMatcher = new RouteMatcher();
        routeMatcher.all(
                "/api/.*",
                new VertxContainerHandler(
                        new URI("http://localhost:8080/api/"),
                        new ApplicationHandler(resourceConfig)));


        httpServer = new VertxHttpServer(8080, routeMatcher, new AppShutdownHook());
    }

    @ManagedAttribute
    public final int getPort() {
        return httpServer.getPort();
    }

    @ManagedOperation
    public final void start() {
        httpServer.start();
    }

    @ManagedOperation
    public final void stop() {
        httpServer.stop();
    }
}
