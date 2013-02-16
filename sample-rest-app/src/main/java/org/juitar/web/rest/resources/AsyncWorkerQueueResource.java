package org.juitar.web.rest.resources;

import org.juitar.monitoring.api.MethodInvocationProbe;
import org.juitar.spring.SpringContextLoader;
import org.juitar.workerq.*;
import org.springframework.context.ApplicationContext;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 * @author sha1n
 * Date: 1/29/13
 */
@Path("/async")
public class AsyncWorkerQueueResource {

    private static final MethodInvocationProbe GET_PROBE = new MethodInvocationProbe(1000);
    private static final MethodInvocationProbe PUT_PROBE = new MethodInvocationProbe(1000);
    private static final WorkQueue QUEUE = new BlockingWorkQueue(new LinkedTransferQueue<Work>(), new TransferQueueAdapter());
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

        QUEUE.add(work);
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/sql")
    public void submit(String sql, @Suspended final AsyncResponse response) {

        if (PUT_PROBE.hit()) {
            System.out.println("PUT TPS: " + PUT_PROBE.getLastInvocationCount());
        }

        final long time = System.currentTimeMillis();
        ApplicationContext applicationContext = SpringContextLoader.getContext();
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

        jdbcBatchQueue.add(work);

    }


    private static class TransferQueueAdapter implements BlockingQueueAdapter {
        private TransferQueue<Work> workBlockingQueue;

        @Override
        public void setDelegate(BlockingQueue<Work> blockingQueue) {
            this.workBlockingQueue = (TransferQueue<Work>) blockingQueue;
        }

        @Override
        public boolean add(Work work) {
            boolean added = false;
            if (workBlockingQueue.hasWaitingConsumer()) {
                added = workBlockingQueue.add(work);
            } else {
                try {
                    workBlockingQueue.transfer(work);
                    added = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return added;
        }

        @Override
        public Work take() throws InterruptedException {
            return workBlockingQueue.take();
        }
    }
}

