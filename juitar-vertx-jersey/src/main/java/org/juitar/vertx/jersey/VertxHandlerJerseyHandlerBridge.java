package org.juitar.vertx.jersey;

import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.internal.PropertiesDelegate;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerRequest;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class acts as a bridge between a Vert.x {@link Handler} and a Jersey {@link ApplicationHandler}.
 *
 * @author sha1n
 * Date: 1/30/13
 */
class VertxHandlerJerseyHandlerBridge {

    private final URI baseUri;
    private final ApplicationHandler applicationHandler;
    private ByteArrayOutputStream stream = new ByteArrayOutputStream();
    private HttpServerRequest httpServerRequest;

    VertxHandlerJerseyHandlerBridge(final URI baseUri, final ApplicationHandler applicationHandler) {
        this.baseUri = baseUri;
        this.applicationHandler = applicationHandler;
    }

    void handle(HttpServerRequest request) {
        this.httpServerRequest = request;
        this.httpServerRequest.dataHandler(new DataHandler());
        this.httpServerRequest.endHandler(new EndRequestHandler());
    }

    private class DataHandler implements Handler<Buffer> {

        @Override
        public void handle(Buffer buf) {
            byte[] data = buf.getBytes();
            try {
                stream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class EndRequestHandler implements Handler<Void> {

        @Override
        public void handle(Void event) {
            try {
                PropertiesDelegate properties = new MapPropertiesDelegate();

                ContainerRequest containerRequest = new ContainerRequest(
                        baseUri,
                        new URI(httpServerRequest.uri),
                        httpServerRequest.method,
                        new DummySecurityContext(new DummySecurityContext.DummyPrincipal("test")), // TODO figure this out
                        properties
                );

                containerRequest.setEntityStream(new ByteArrayInputStream(stream.toByteArray()));
                containerRequest.setWriter(new VertxContainerResponseWriter(httpServerRequest));

                applicationHandler.handle(containerRequest);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }

    }
}
