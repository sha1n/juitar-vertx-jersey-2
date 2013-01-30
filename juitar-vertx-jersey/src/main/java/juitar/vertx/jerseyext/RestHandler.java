package juitar.vertx.jerseyext;

import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

import java.net.URI;

public class RestHandler implements Handler<HttpServerRequest>, Container {
    public static final String PROPERTY_BASE_URI = "juitar.vertx.server.rest.baseUri";

    private final ApplicationHandler applicationHandler;
    private final ResourceConfig resourceConfig;
    private final URI baseUri;

    public RestHandler(URI baseURI, final ApplicationHandler applicationHandler, ResourceConfig resourceConfig) {
        this.baseUri = baseURI;
        this.resourceConfig = resourceConfig;
        this.applicationHandler = applicationHandler;
    }

    @Override
    public void handle(HttpServerRequest req) {
        new RestRequestHandler(baseUri, applicationHandler).handle(req);
    }

    @Override
    public ResourceConfig getConfiguration() {
        return resourceConfig;
    }

    @Override
    public void reload() {
        // TODO feature out what's that callback for
    }

    @Override
    public void reload(ResourceConfig configuration) {
        // TODO feature out what's that callback for
    }
}
