package pl.tkowalcz.examples.subjects;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.util.concurrent.Uninterruptibles;
import com.sun.management.OperatingSystemMXBean;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class PerformanceMonitorImpl implements PerformanceMonitor {

    private final static OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    private final Subject<Double, Double> cpuUsagePublisher = PublishSubject.create();
    private final Subject<PerformanceCounter, PerformanceCounter> counterPublisher = BehaviorSubject.create();

    private AtomicReference<PerformanceCounter> performanceCounter =
            new AtomicReference<>(new PerformanceCounter());

    public PerformanceMonitorImpl() {
        Thread cpuUsageUpdateThread = new Thread() {

            @Override
            public void run() {
                while (true) {
                    cpuUsagePublisher.onNext(bean.getSystemCpuLoad());
                    Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
                }
            }
        };

        cpuUsageUpdateThread.setUncaughtExceptionHandler((thread, throwable) -> cpuUsagePublisher.onError(throwable));
        cpuUsageUpdateThread.setDaemon(true);
        cpuUsageUpdateThread.start();

        Thread counterPublisherThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    PerformanceCounter oldValue = performanceCounter.getAndSet(new PerformanceCounter());
                    counterPublisher.onNext(oldValue);
                    Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
                }
            }
        };

        counterPublisherThread.setUncaughtExceptionHandler((thread, throwable) -> counterPublisher.onError(throwable));
        counterPublisherThread.setDaemon(true);
        counterPublisherThread.start();
    }

    @Override
    public Observable<Double> cpuUsage() {
        return cpuUsagePublisher.asObservable();
    }

    @Override
    public Observable<PerformanceCounter> responseTimes() {
        return counterPublisher.asObservable();
    }

    @Override
    public void updateResponseTime(long time) {
        performanceCounter.get().update(time);
    }
}
