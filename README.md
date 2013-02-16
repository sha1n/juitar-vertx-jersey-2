Juitar-Vert.x-Jersey-2
======================
This repository plays with REST resources that fulfills requests asynchronously through worker-queues.
The code relies on an embedded Vert.x core components for HTTP server with async IO capabilities and Jersey 2.0 for REST framework.

Module: juitar-vertx-http-server
--------------------------------
Provides a convenient Vert.x based HTTP server class with basic lifecycle methods and shutdown hook callback
interface option.


Module: juitar-vertx-jersey
--------------------------------
Provides a very basic implementation to enable Jersey 2.0 to work on top of a Vert.x HTTP server.


Module: juitar-vertx-rest-sampleapp
-----------------------------------
This module takes the HTTP server and Jersey adapters implemented by the other two modules and implements a REST resource
 which makes use of the [worker-queue](https://github.com/sha1n/juitar-playground/tree/master/core/worker-queue) and
 [jdbc-worker](https://github.com/sha1n/juitar-playground/tree/master/core/jdbc-worker) modules implemented in repository
 [juitar-playground](https://github.com/sha1n/juitar-playground).

### REST Async Response PUT Method Using the JDBC Worker Queue

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/sql")
    public void submit(String sql, @Suspended final AsyncResponse response) {

        ApplicationContext applicationContext = Launcher.applicationContext;
        WorkQueue jdbcBatchQueue = (WorkQueue) applicationContext.getBean("jdbcBatchQueue");

        Work work = new Work(UUID.randomUUID().toString(),
                new String[]{sql},
                new ResultChannel() {
                    @Override
                    public void onSuccess(Result result) {
                        response.resume("Result received: " + result.toString());
                    }

                    @Override
                    public void onFailure(Result result, Exception e) {
                        e.printStackTrace();
                        response.resume(e);
                    }
                });

        jdbcBatchQueue.submit(work);

    }


### Launcher Code

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
