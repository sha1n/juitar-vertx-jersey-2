package org.juitar.web.rest.resources;

import juitar.worker.queue.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

/**
 * @author sha1n
 * Date: 1/29/13
 */
@Path("/async")
public class AsyncWorkerQueueResource {

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


    /**
     * This method uses JAX-RS 2 {@link AsyncResponse} response capability.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void /* Yes it returns void */ asyncQueue(@Suspended final AsyncResponse asyncResponse) {

        Work work = new Work(UUID.randomUUID().toString(), "work-payload", new ResultChannel() {
            @Override
            public void onSuccess(Result result) {
                asyncResponse.resume(result.toString());
            }

            @Override
            public void onFailure(Result result, Exception e) {
                asyncResponse.resume(e);
            }
        });

        QUEUE.submit(work);
    }

}
