package juitar.vertx.jerseyext;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.server.impl.model.method.dispatch.ResourceJavaMethodDispatcher;
import com.sun.jersey.spi.container.JavaMethodInvokerFactory;
import com.sun.jersey.spi.dispatch.RequestDispatcher;

import java.lang.reflect.InvocationTargetException;

/**
 * @author sha1n
 * Date: 1/29/13
 */
public class QueueRequestDispatcher extends ResourceJavaMethodDispatcher implements RequestDispatcher {

    private AbstractResourceMethod abstractResourceMethod;

    public QueueRequestDispatcher(AbstractResourceMethod abstractResourceMethod) {
        super(abstractResourceMethod, JavaMethodInvokerFactory.getDefault());
        this.abstractResourceMethod = abstractResourceMethod;
    }

    @Override
    protected void _dispatch(Object resource, HttpContext context) throws InvocationTargetException, IllegalAccessException {

    }
}
