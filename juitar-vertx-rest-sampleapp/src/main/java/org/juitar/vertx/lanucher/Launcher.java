package org.juitar.vertx.lanucher;

import junitar.server.netty.SpringContextLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * @author sha1n
 * Date: 1/29/13
 */
public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static ApplicationContext applicationContext;

    public static void main(String... args) throws IOException, URISyntaxException, ExecutionException, InterruptedException {

        GenericApplicationContext genericApplicationContext;

        LOGGER.info("Loading Spring context...");
        genericApplicationContext = new SpringContextLoader().load();
        genericApplicationContext.refresh();
        genericApplicationContext.start();
        applicationContext = genericApplicationContext;

//        ResourceConfig resourceConfig = new ResourceConfig();
//        resourceConfig.registerFinder(new PackageNamesScanner(
//                new String[]{
//                        "org.juitar.web.rest.resources",
//                }, true));
//
//
//        RouteMatcher routeMatcher = new RouteMatcher();
//        routeMatcher.all(
//                "/api/.*",
//                new VertxContainerHandler(
//                        new URI("http://localhost:8080/api/"),
//                        new ApplicationHandler(resourceConfig)));
//
//
//        VertxHttpServer httpServer = new VertxHttpServer(8080, routeMatcher, new AppShutdownHook());
//        httpServer.start();
        HttpServerJmxWrapper httpServerJmxWrapper = applicationContext.getBean(HttpServerJmxWrapper.class);
        httpServerJmxWrapper.start();
        LOGGER.info("Server is ready.");
    }
}
