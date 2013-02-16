package org.juitar.web.rest.resources;

import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author sha1n
 * Date: 1/30/13
 */
@Path("/dumb")
public class DummyResource {

    private static class Entity implements Serializable {

        // Transient fields
        static String sys = UUID.randomUUID().toString();
        transient double squ = System.currentTimeMillis() % 3;

        // Data fields
        final int id = 1;
        final long lastModified = System.currentTimeMillis();
        final Date created = new Date();
        final String name = "Name";
        final String description = "Description";
        final String[] tags = {"t1", "t2", "t3", "t4", "t5"};
        final List<Entity> subs = new ArrayList<Entity>();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getDummyEntity() {
        Entity entity = new Entity();
        entity.subs.add(new Entity());
        entity.subs.add(new Entity());

        return new Gson().toJson(entity);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/dumber")
    public String getAnotherThing() {
        return "That other thing...";
    }

}
