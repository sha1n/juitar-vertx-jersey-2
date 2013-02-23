package org.juitar.web.rest.resources;

import org.juitar.monitoring.api.MethodInvocationProbe;
import org.juitar.spring.SpringContextLoader;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
            System.out.println("PUT TPS: " + PUT_PROBE.getLastInvocationCount());
        }

        ApplicationContext applicationContext = SpringContextLoader.getContext();

        DataSource dataSource = applicationContext.getBean(DataSource.class);

        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {

            connection.setAutoCommit(false);
            int count = statement.executeUpdate(sql);
            connection.commit();
            return String.format("Updated %d records", count);
        } catch (Exception e) {
            return "ERROR: " + e.getMessage() + " Statement: " + sql;
        }
    }


}

