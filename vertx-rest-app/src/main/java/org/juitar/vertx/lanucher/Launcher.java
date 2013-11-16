package org.juitar.vertx.lanucher;

import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.scanning.PackageNamesScanner;
import org.juitar.jmx.ApiHandlerJmxWrapper;
import org.juitar.jmx.HttpServerJmxWrapper;
import org.juitar.server.http.vertx.VertxHttpServer;
import org.juitar.spring.SpringContextLoader;
import org.juitar.vertx.jersey.VertxContainerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.vertx.java.core.http.RouteMatcher;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author sha1n
 * Date: 1/29/13
 */
public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static ApplicationContext applicationContext;

    public static void main(String... args) throws IOException, URISyntaxException {

        if (System.getProperties().contains("java.security.policy")) {
            System.setSecurityManager(new SecurityManager());
        }
        PlatformLifecycle.toPhase(Phase.STARTUP);

        GenericApplicationContext genericApplicationContext;

        LOGGER.info("Loading Spring context...");
        genericApplicationContext = new SpringContextLoader().load();
        genericApplicationContext.refresh();
        genericApplicationContext.start();
        applicationContext = genericApplicationContext;

        RouteMatcher routeMatcher = createApiRouteMatcher();

        VertxHttpServer httpServer = new VertxHttpServer(8080, routeMatcher, new AppShutdownHook());
        // Create JMX wrapper for the server.
        HttpServerJmxWrapper httpServerJmxWrapper = (HttpServerJmxWrapper) applicationContext.getBean("httpServerJmxWrapper");
        httpServerJmxWrapper.setHttpServer(httpServer);

        httpServer.start();
        PlatformLifecycle.toPhase(Phase.RUNNING);

        LOGGER.info("Server is ready.");
    }

    private static RouteMatcher createApiRouteMatcher() throws URISyntaxException {

        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.registerFinder(new PackageNamesScanner(
                new String[]{
                        "org.juitar.web.rest.resources",
                }, true));


        RouteMatcher routeMatcher = new RouteMatcher();

        VertxContainerHandler vertxContainerHandler = new VertxContainerHandler(
                new URI("http://localhost:8080/api/"),
                new ApplicationHandler(resourceConfig));

        // Wrap the handler with a JMX bean for monitoring.
        ApiHandlerJmxWrapper apiHandlerJmxWrapper = (ApiHandlerJmxWrapper) applicationContext.getBean("apiHandlerJmxWrapper");
        apiHandlerJmxWrapper.setDelegate(vertxContainerHandler);

        routeMatcher.all("/api/.*", apiHandlerJmxWrapper);

        return routeMatcher;
    }

}
