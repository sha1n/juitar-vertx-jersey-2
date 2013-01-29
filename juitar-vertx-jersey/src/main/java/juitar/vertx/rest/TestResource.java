package juitar.vertx.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author sha1n
 * Date: 1/29/13
 */
@Path("/async")
public class TestResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {
        return "Test!!!";
    }

    @GET
//    @Produces(MediaType.WILDCARD)
    @Path("/sub")
    public void getSub() {

    }
}
