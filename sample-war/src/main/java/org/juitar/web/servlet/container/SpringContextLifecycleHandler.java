package org.juitar.web.servlet.container;

import org.juitar.spring.SpringContextLoader;
import org.springframework.context.support.GenericApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

/**
 * @author sha1n
 * Date: 2/24/13
 */
public class SpringContextLifecycleHandler implements ServletContextListener {

    private static GenericApplicationContext genericApplicationContext;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            genericApplicationContext = new SpringContextLoader().load();
            genericApplicationContext.refresh();
            genericApplicationContext.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        genericApplicationContext.stop();
        genericApplicationContext.destroy();
        genericApplicationContext.close();

    }
}
