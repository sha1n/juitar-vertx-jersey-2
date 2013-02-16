package org.juitar.tomcat.lanucher;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.Http11NioProtocol;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.scanning.PackageNamesScanner;
import org.glassfish.jersey.servlet.ServletContainer;
import org.juitar.spring.SpringContextLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author sha1n
 * Date: 1/29/13
 */
public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static ApplicationContext applicationContext;

    public static void main(String... args) throws IOException, LifecycleException, URISyntaxException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        GenericApplicationContext genericApplicationContext;

        LOGGER.info("Loading Spring context...");
        genericApplicationContext = new SpringContextLoader().load();
        genericApplicationContext.refresh();
        genericApplicationContext.start();
        applicationContext = genericApplicationContext;


        Connector connector = new NioConnector();
        connector.setPort(8080);

        Tomcat tomcat = new Tomcat();
        tomcat.getService().addConnector(connector);
        tomcat.getConnector().setProtocolHandlerClassName(Http11NioProtocol.class.getName());
        tomcat.getConnector().setPort(9090);

        File base = new File(System.getProperty("java.io.tmpdir"));
        Context rootCtx = tomcat.addContext("", base.getAbsolutePath());
        configureJersey(rootCtx);

        tomcat.start();

        LOGGER.info("Server is ready.");
    }

    private static void configureJersey(Context rootCtx) throws URISyntaxException {

        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.registerFinder(new PackageNamesScanner(
                new String[]{
                        "org.juitar.web.rest.resources",
                }, true));

        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        Tomcat.addServlet(rootCtx, "jersey", servletContainer);
        rootCtx.addServletMapping("/api/*", "jersey");
    }

    private static class NioConnector extends Connector {

        public NioConnector() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            super();
            protocolHandlerClassName = Http11NioProtocol.class.getName();
            protocolHandler = (ProtocolHandler) Class.forName(protocolHandlerClassName).newInstance();
        }
    }
}
