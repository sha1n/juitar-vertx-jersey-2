package juitar.vertx.lanucher;

import juitar.vertx.jerseyext.RestHandler;
import juitar.vertx.rest.TestResource;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.RouteMatcher;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author sha1n
 * Date: 1/29/13
 */
public class Lanucher {

    public static void main(String... args) throws IOException {

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Vertx vertx = Vertx.newVertx();

                RouteMatcher rm = new RouteMatcher();

                final Map<String, Object> props = new HashMap<>();
                props.put(RestHandler.PROPERTY_BASE_URI, "/api");

                ResourceConfig resourceConfig = new ResourceConfig();
                resourceConfig.addProperties(props);
                resourceConfig.registerClasses(TestResource.class);
                try {
                    rm.all(
                            "/api/.*",
                            new RestHandler(
                                    new URI("http://localhost:8080/api/"),
                                    new ApplicationHandler(resourceConfig),
                                    resourceConfig
                            ));
                } catch (URISyntaxException e) {
                    // Exceptions are currently not handled by the project.
                    e.printStackTrace();
                }

                int port = 8080;
                vertx.createHttpServer().requestHandler(rm).listen(port);
                System.out.println("HTTP Server listening on port " + port);
            }
        });


    }
}
