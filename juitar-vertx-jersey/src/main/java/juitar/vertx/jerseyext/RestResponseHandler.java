package juitar.vertx.jerseyext;

import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.ContainerResponse;
import org.glassfish.jersey.server.spi.ContainerResponseWriter;
import org.vertx.java.core.http.HttpServerRequest;

import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class RestResponseHandler implements ContainerResponseWriter {

    /**
     * The request that will be responded.
     */
    private HttpServerRequest req;
    /**
     * The body of the response.
     */
    private ByteArrayOutputStream out = new ByteArrayOutputStream();

    RestResponseHandler(HttpServerRequest req) {
        this.req = req;
    }

    @Override
    public OutputStream writeResponseStatusAndHeaders(long contentLength, ContainerResponse responseContext) throws ContainerException {
        // Set status
        req.response.statusCode = responseContext.getStatusInfo().getStatusCode();
        req.response.statusMessage = responseContext.getStatusInfo().getReasonPhrase();

        // Set headers
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        for (String key : headers.keySet()) {
            for (Object value : headers.get(key)) {
                req.response.putHeader(key, value);
            }
        }
        req.response.putHeader("Connection", "keep-alive");

        return out;
    }

    @Override
    public boolean suspend(long timeOut, TimeUnit timeUnit, TimeoutHandler timeoutHandler) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSuspendTimeout(long timeOut, TimeUnit timeUnit) throws IllegalStateException {
        // TODO figure this out
    }

    @Override
    public void commit() {
        // TODO figure out what's wrong with this line. Response seems to be written twice with this uncommented.
//        req.response.end(new Buffer(out.toByteArray()));
    }

    @Override
    public void failure(Throwable error) {
        req.response.end();
    }
}
