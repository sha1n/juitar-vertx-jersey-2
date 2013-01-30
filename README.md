Juitar-Vert.x
=============
This repository plays with REST resources that fulfills requests asynchronously through worker-queues.
The code relies on embedded Vert.x for HTTP server and Jersey 2.0 as a REST framework.


Launcher Code
-------------

    public class Lanucher {

        public static void main(String... args) throws IOException, URISyntaxException {

            // Create ResourceConfig instance and register a package scanner to register individual resources from package.
            ResourceConfig resourceConfig = new ResourceConfig();
            resourceConfig.registerFinder(new PackageNamesScanner(
                    new String[]{
                            "org.juitar.web.rest.resources",
                    }, true));

            // Create a org.juitar.server.http.vertx.VertxHttpServer
            VertxHttpServer httpServer = new VertxHttpServer(
                    8080, // Server port
                    "/api/.*", // URL matcher pattern
                    new VertxContainerHandler( // HTTP request handler for Vert.x
                            new URI("http://localhost:8080/api/"),
                            new ApplicationHandler(resourceConfig)));

            // Start the server
            httpServer.start();
        }
    }
