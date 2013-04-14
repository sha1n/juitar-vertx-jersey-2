package org.juitar.vertx.jersey;

import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.ContainerResponse;
import org.glassfish.jersey.server.spi.ContainerResponseWriter;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;

import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author sha1n
 * Date: 1/30/13
 */
class VertxContainerResponseWriter implements ContainerResponseWriter {

    private HttpServerRequest req;
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    /**
     * This is to make sure the response is not ended more than once.
     * Jersey 2.0 seems to call {@link #commit()}  twice at least when
     * an unmapped request URL is coming in (404).
     */
    private boolean ended = false;

    VertxContainerResponseWriter(HttpServerRequest req) {
        this.req = req;
    }

    @Override
    public OutputStream writeResponseStatusAndHeaders(long contentLength, ContainerResponse responseContext) throws ContainerException {
        req.response.statusCode = responseContext.getStatusInfo().getStatusCode();
        req.response.statusMessage = responseContext.getStatusInfo().getReasonPhrase();

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
        return true;  // TODO what needs to be done here?
    }

    @Override
    public void setSuspendTimeout(long timeOut, TimeUnit timeUnit) throws IllegalStateException {
        // TODO what needs to be done here?
    }

    @Override
    public void commit() {
        if (!ended) {
            req.response.end(new Buffer(out.toByteArray()));
        }
        ended = true;
    }

    @Override
    public void failure(Throwable error) {
        if (!ended) {
            if (req.response.statusCode == HttpResponseStatus.OK.getCode()) {
                req.response.statusCode = HttpResponseStatus.INTERNAL_SERVER_ERROR.getCode();
                req.response.statusMessage = HttpResponseStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
            }

            req.response.end();
        }
        ended = true;
    }

    @Override
    public boolean enableResponseBuffering() {
        return true;
    }
}
