package juitar.vertx.lanucher;

import juitar.vertx.jerseyext.RestHandler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.RouteMatcher;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

                try {
                    rm.all(
                            "/api/.*",
                            new RestHandler(new URI("http://localhost:8080/api/"),
                                    "juitar.vertx.rest"));
                } catch (URISyntaxException e) {
                    // Exceptions are currently not handled by the project.
                    e.printStackTrace();
                }

                vertx.createHttpServer().requestHandler(rm).listen(8080);
            }
        });


    }
}
