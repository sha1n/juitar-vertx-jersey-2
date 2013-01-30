package org.juitar.vertx.jersey;

import org.glassfish.jersey.server.ApplicationHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

import java.net.URI;

/**
 * This class is the main glue between Vert.x and Jersey. It implements the Vert.x {@link Handler} interface, meaning
 * that all HTTP requests coming into the Vert.x listener will be dispatched to handlers of it's kind.
 *
 * @author sha1n
 * Date: 1/30/13
 */
public class VertxContainerHandler implements Handler<HttpServerRequest> {

    private final ApplicationHandler applicationHandler;
    private final URI baseUri;

    public VertxContainerHandler(URI baseURI, final ApplicationHandler applicationHandler) {
        this.baseUri = baseURI;
        this.applicationHandler = applicationHandler;
    }

    @Override
    public void handle(HttpServerRequest req) {
        new VertxHandlerJerseyHandlerBridge(baseUri, applicationHandler).handle(req);
    }

}
