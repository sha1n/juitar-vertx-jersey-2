package juitar.vertx.jerseyext;

import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.internal.PropertiesDelegate;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerRequest;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class RestRequestHandler {

    /**
     * The base URI for the REST ws.
     */
    private final URI baseUri;
    private final ApplicationHandler applicationHandler;
    /**
     * The data read from the HTTP request.
     */
    private ByteArrayOutputStream stream = new ByteArrayOutputStream();
    /**
     * The HTTP server request that is currently being handled.
     */
    private HttpServerRequest req;


    public RestRequestHandler(final URI baseUri, final ApplicationHandler applicationHandler) {
        this.baseUri = baseUri;
        this.applicationHandler = applicationHandler;
    }

    public void handle(HttpServerRequest req) {
        this.req = req;
        req.dataHandler(new DataHandler());
        req.endHandler(new EndRequestHandler());
    }

    public class DataHandler implements Handler<Buffer> {

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

    public class EndRequestHandler implements Handler<Void> {

        @Override
        public void handle(Void event) {
            try {
                PropertiesDelegate properties = new MapPropertiesDelegate();

                ContainerRequest containerRequest = new ContainerRequest(
                        baseUri,
                        new URI(req.uri),
                        req.method,
                        new DummySecurityContext(new DummySecurityContext.DummyPrincipal("test")),
                        properties
                );

                containerRequest.setWriter(new RestResponseHandler(req));

                applicationHandler.handle(containerRequest);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }

    }
}
