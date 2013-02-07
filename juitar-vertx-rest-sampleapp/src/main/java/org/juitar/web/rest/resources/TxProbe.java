package org.juitar.web.rest.resources;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author sha1n
 * Date: 2/7/13
 */
public class TxProbe {

    private volatile FrameProbe currentProbe = new FrameProbe();
    private volatile FrameProbe previousProbe = new FrameProbe();

    public boolean hit() {
        boolean update = false;
        long hitStamp = System.currentTimeMillis();
        Long head = currentProbe.hit(hitStamp);
        if (head <= (hitStamp - 1000)) {
            FrameProbe temp = currentProbe;
            currentProbe = new FrameProbe();
            temp.calculateMetrics(hitStamp);
            previousProbe = temp;
            update = true;
        }

        return update;
    }

    public double getCurrentTPS() {
        return previousProbe.getAverageTPS();
    }

    public class FrameProbe {

        private double txPerSecondAverage = 0D;
        private ConcurrentLinkedQueue<Long> log = new ConcurrentLinkedQueue<>();

        public Long hit(long hitStamp) {
            log.add(hitStamp);
            return log.peek();
        }

        public double getAverageTPS() {
            return txPerSecondAverage;
        }

        private void calculateMetrics(final long lastHitStamp) {
            long frame = lastHitStamp - 1000;
            long currentHead;
            int rangeCount = log.size();
            do {
                currentHead = log.poll();
            } while (currentHead >= frame && !log.isEmpty());

            this.txPerSecondAverage = rangeCount;
        }

    }

}
