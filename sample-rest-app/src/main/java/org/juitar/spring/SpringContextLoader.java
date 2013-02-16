package org.juitar.spring;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * @author sha1n
 * Date: 1/24/13
 */
public class SpringContextLoader {

    private static GenericApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    public GenericApplicationContext load() throws IOException {
        PathMatchingResourcePatternResolver pmrl = new PathMatchingResourcePatternResolver(getClass().getClassLoader());
        Resource[] resources = pmrl.getResources("classpath*:META-INF/*-context.xml");

        context = new GenericApplicationContext();
        for (Resource r : resources) {
            XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
            reader.loadBeanDefinitions(r);
        }

        return context;
    }
}
