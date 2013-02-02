package org.juitar.jmx;

import org.springframework.jmx.export.annotation.ManagedMetric;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.support.MetricType;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sha1n
 * Date: 2/2/13
 */
@ManagedResource(objectName = "org.juitar.vertx.http.server:name=RestApiServerRequestMonitor",
        description = "REST API Request Monitor")
public class ApiHandlerJmxWrapper implements Handler<HttpServerRequest> {

    private final AtomicLong requestCount = new AtomicLong(0);
    private final AtomicLong successfulRequests = new AtomicLong(0);
    private final AtomicLong lastRequestTime = new AtomicLong(0);

    private Handler<HttpServerRequest> delegate;

    public final void setDelegate(Handler<HttpServerRequest> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handle(HttpServerRequest event) {
        this.lastRequestTime.set(System.currentTimeMillis());
        this.requestCount.incrementAndGet();
        try {
            this.delegate.handle(event);
            this.successfulRequests.incrementAndGet();
        } finally {
            lastRequestTime.set(System.currentTimeMillis());
        }
    }

    @ManagedMetric(metricType = MetricType.COUNTER)
    public long getTotalRequests() {
        return this.requestCount.get();
    }

    @ManagedMetric(metricType = MetricType.GAUGE)
    public float getRequestSuccessRaio() {
        return this.successfulRequests.get() / this.requestCount.get();
    }

    @ManagedMetric(metricType = MetricType.COUNTER)
    public Date getLastRequestTime() {
        return new Date(this.lastRequestTime.get());
    }
}
