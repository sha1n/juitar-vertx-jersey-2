package juitar.vertx.rest;

import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.api.core.HttpResponseContext;
import juitar.vertx.jerseyext.AsyncWorker;
import juitar.worker.queue.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

/**
 * @author sha1n
 * Date: 1/29/13
 */
@Path("/async")
public class TestResource {

    private static final WorkQueue QUEUE = new WorkQueueImpl();
    private static final WorkerQueueServiceRegistryImpl WORKER_QUEUE_SERVICE_REGISTRY = new WorkerQueueServiceRegistryImpl();

    static {
        WORKER_QUEUE_SERVICE_REGISTRY.registerQueueService(QUEUE, new WorkerFactory() {
            @Override
            public Worker createWorker() {
                return new Worker() {
                    @Override
                    public Result doWork(Work work) {
                        Result result = new Result(work.getId());
                        result.setResultData("Async generated...");
                        return result;
                    }
                };
            }
        });

        WorkerQueueService workerQueueService = WORKER_QUEUE_SERVICE_REGISTRY.getWorkerQueueService(QUEUE);
        workerQueueService.start(4);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @AsyncWorker
    public void get(final HttpRequestContext requestContext, final HttpResponseContext responseContext) {
        Work work = new Work(UUID.randomUUID().toString(), requestContext, new ResultChannel() {
            @Override
            public void onSuccess(Result result) {
                responseContext.setEntity(result.getResultData());
                responseContext.setStatus(200);
            }

            @Override
            public void onFailure(Result result, Exception e) {
                responseContext.setEntity(result);
                responseContext.setStatus(500);
            }
        });

        QUEUE.submit(work);

//        return "Test!!!";
    }

    @GET
    @Produces(MediaType.WILDCARD)
    @Path("/sub")
    public String getSub() {
        return "string";
    }
}
