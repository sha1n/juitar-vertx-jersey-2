package juitar.vertx.jerseyext;

import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.server.impl.inject.InjectableValuesProvider;
import com.sun.jersey.server.impl.model.method.dispatch.AbstractResourceMethodDispatchProvider;
import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;
import com.sun.jersey.spi.dispatch.RequestDispatcher;

/**
 * @author sha1n
 * Date: 1/29/13
 */
public class QueueResourceMethodDispatchProvider extends AbstractResourceMethodDispatchProvider implements ResourceMethodDispatchProvider {
    @Override
    public RequestDispatcher create(AbstractResourceMethod abstractResourceMethod) {
        if (abstractResourceMethod.getMethod().isAnnotationPresent(QueueOperation.class)) {
            return new QueueRequestDispatcher(abstractResourceMethod);
        } else {
            return super.create(abstractResourceMethod);
        }
    }

    @Override
    protected InjectableValuesProvider getInjectableValuesProvider(AbstractResourceMethod abstractResourceMethod) {
        return null;
    }
}
