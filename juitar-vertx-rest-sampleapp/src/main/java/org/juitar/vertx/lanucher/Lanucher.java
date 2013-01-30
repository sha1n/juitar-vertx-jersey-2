package org.juitar.vertx.lanucher;

import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.scanning.PackageNamesScanner;
import org.juitar.server.http.vertx.VertxHttpServer;
import org.juitar.vertx.jersey.VertxContainerHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author sha1n
 * Date: 1/29/13
 */
public class Lanucher {

    public static void main(String... args) throws IOException, URISyntaxException {

        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.registerFinder(new PackageNamesScanner(
                new String[]{
                        "org.juitar.web.rest.resources",
                }, true));

        VertxHttpServer httpServer = new VertxHttpServer(
                8080,
                "/api/.*",
                new VertxContainerHandler(
                        new URI("http://localhost:8080/api/"),
                        new ApplicationHandler(resourceConfig)));

        httpServer.start();
    }
}
