package org.juitar.web.rest.resources;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author sha1n
 * Date: 2/5/13
 */
@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        Response.StatusType status;
        if (exception instanceof ClientErrorException) {
            status = ((ClientErrorException) exception).getResponse().getStatusInfo();
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }

        return Response.status(status).entity(status.getStatusCode() + " : " + status.getReasonPhrase()).build();
    }
}
