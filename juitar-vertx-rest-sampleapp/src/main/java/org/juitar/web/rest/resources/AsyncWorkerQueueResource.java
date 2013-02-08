package org.juitar.web.rest.resources;

import org.juitar.monitoring.api.MethodInvocationProbe;
import org.juitar.vertx.lanucher.Launcher;
import org.juitar.workerq.*;
import org.springframework.context.ApplicationContext;

import javax.ws.rs.*;
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

    private static final MethodInvocationProbe GET_PROBE = new MethodInvocationProbe(1000);
    private static final MethodInvocationProbe PUT_PROBE = new MethodInvocationProbe(1000);
    private static final WorkQueue QUEUE = new WorkQueueImpl();
    private static final WorkerQueueServiceRegistryImpl WORKER_QUEUE_SERVICE_REGISTRY = new WorkerQueueServiceRegistryImpl();

    static {
        WORKER_QUEUE_SERVICE_REGISTRY.registerQueueService(QUEUE, new WorkerFactory() {
            @Override
            public Worker createWorker() {
                return new Worker() {
                    @Override
                    public void doWork(Work work) {
                        Result result = new Result(work.getId());
                        result.setResultData("Async generated...");
                        work.getCompletionCallback().onSuccess(result);
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
        if (GET_PROBE.hit()) {
            System.out.println("GET TPS: " + GET_PROBE.getLastInvocationCount());
        }

        Work work = new Work(UUID.randomUUID().toString(), "work-payload", new CompletionCallback() {
            @Override
            public void onSuccess(Result result) {
                asyncResponse.resume(result.toString());
            }

            @Override
            public void onFailure(Result result, Exception e, CompletionStatus sttaus) {
                asyncResponse.resume(e);
            }
        });

        QUEUE.submit(work);
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/sql")
    public void submit(String sql, @Suspended final AsyncResponse response) {

        if (PUT_PROBE.hit()) {
            System.out.println("PUT TPS: " + PUT_PROBE.getLastInvocationCount());
        }

        final long time = System.currentTimeMillis();
        ApplicationContext applicationContext = Launcher.applicationContext;
        WorkQueue jdbcBatchQueue = (WorkQueue) applicationContext.getBean("jdbcBatchQueue");

        Work work = new Work(UUID.randomUUID().toString(),
                new String[]{sql},
                new CompletionCallback() {
                    @Override
                    public void onSuccess(Result result) {
                        response.resume("Result received in "
                                + (System.currentTimeMillis() - time)
                                + "ms: " + result.toString());
                    }

                    @Override
                    public void onFailure(Result result, Exception e, CompletionStatus status) {
                        e.printStackTrace();
                        response.resume(e);
                    }
                });

        jdbcBatchQueue.submit(work);

    }


}

