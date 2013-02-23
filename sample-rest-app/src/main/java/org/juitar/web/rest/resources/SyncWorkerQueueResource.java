package org.juitar.web.rest.resources;

import org.juitar.monitoring.api.MethodInvocationProbe;
import org.juitar.spring.SpringContextLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

/**
 * @author sha1n
 * Date: 1/29/13
 */
@Path("/sync")
public class SyncWorkerQueueResource {

    private static final MethodInvocationProbe PUT_PROBE = new MethodInvocationProbe(1000);

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/sql")
    public String submit(String sql) throws SQLException {

        if (PUT_PROBE.hit()) {
            System.out.println("SYNC PUT TPS: " + PUT_PROBE.getLastInvocationCount());
        }

        ApplicationContext applicationContext = SpringContextLoader.getContext();
        JdbcTemplate jdbc = applicationContext.getBean(JdbcTemplate.class);

        try {
            return String.format("Updated %d records", jdbc.update(sql));
        } catch (Exception e) {
            return "ERROR: " + e.getMessage() + " Statement: " + sql;
        }
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/sql")
    public String count() {

        ApplicationContext applicationContext = SpringContextLoader.getContext();
        JdbcTemplate jdbc = applicationContext.getBean(JdbcTemplate.class);

        try {
            return String.format("Count = %d", jdbc.queryForInt("SELECT COUNT(1) FROM TEST"));
        } catch (DataAccessException e) {
            return "ERROR: " + e.getMessage();
        }
    }

}

