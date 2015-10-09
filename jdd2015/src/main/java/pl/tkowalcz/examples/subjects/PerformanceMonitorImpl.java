package pl.tkowalcz.examples.subjects;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.util.concurrent.Uninterruptibles;
import com.sun.management.OperatingSystemMXBean;
import rx.Observable;

public class PerformanceMonitorImpl implements PerformanceMonitor {

    private final static OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    private AtomicReference<PerformanceCounter> performanceCounter =
            new AtomicReference<>(new PerformanceCounter());

    public PerformanceMonitorImpl() {
        Thread cpuUsageUpdateThread = new Thread() {

            @Override
            public void run() {
                while (true) {
                    // TODO: Publish metric value
                    Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
                }
            }
        };

        cpuUsageUpdateThread.setUncaughtExceptionHandler(null /* TODO  Forward error to listeners */);
        cpuUsageUpdateThread.setDaemon(true);
        cpuUsageUpdateThread.start();

        Thread counterPublisherThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    PerformanceCounter oldValue = performanceCounter.getAndSet(new PerformanceCounter());
                    // TODO: Publish counter value
                    Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
                }
            }
        };

        counterPublisherThread.setUncaughtExceptionHandler(null /* TODO  Forward error to listeners */);
        counterPublisherThread.setDaemon(true);
        counterPublisherThread.start();
    }

    @Override
    public Observable<Double> cpuUsage() {
        return null; // TODO
    }

    @Override
    public Observable<PerformanceCounter> responseTimes() {
        return null; // TODO
    }

    @Override
    public void updateResponseTime(long time) {
        performanceCounter.get().update(time);
    }
}
