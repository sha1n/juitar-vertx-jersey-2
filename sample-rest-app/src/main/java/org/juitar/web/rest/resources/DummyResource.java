package org.juitar.web.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author sha1n
 * Date: 1/30/13
 */
@Path("/dumb")
public class DummyResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getSomething() {
        return "That thing...";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/dumber")
    public String getAnotherThing() {
        return "That other thing...";
    }

}
