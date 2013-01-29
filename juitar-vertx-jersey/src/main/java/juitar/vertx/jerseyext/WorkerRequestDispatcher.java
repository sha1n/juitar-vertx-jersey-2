package juitar.vertx.jerseyext;

import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.container.MappableContainerException;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.spi.container.JavaMethodInvoker;
import com.sun.jersey.spi.container.JavaMethodInvokerFactory;
import com.sun.jersey.spi.dispatch.RequestDispatcher;

import java.lang.reflect.InvocationTargetException;

/**
 * @author sha1n
 * Date: 1/29/13
 */
public class WorkerRequestDispatcher implements RequestDispatcher {

    private final JavaMethodInvoker javaMethodInvoker = JavaMethodInvokerFactory.getDefault();
    private AbstractResourceMethod abstractResourceMethod;

    public WorkerRequestDispatcher(AbstractResourceMethod abstractResourceMethod) {
        this.abstractResourceMethod = abstractResourceMethod;
    }

    @Override
    public void dispatch(Object resource, HttpContext context) {
        // Invoke the method on the resource
        try {

            javaMethodInvoker.invoke(abstractResourceMethod.getMethod(), resource, context.getRequest(), context.getResponse());

            // The response is not set here. It is later set by the worker callback called from the response.

        } catch (InvocationTargetException e) {
            // Propagate the target exception so it may be mapped to a response
            throw new MappableContainerException(e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new ContainerException(e);
        }
    }
}
