package pl.tkowalcz.examples.subjects;

import rx.Observable;

public interface PerformanceMonitor {

    Observable<Double> cpuUsage();

    Observable<PerformanceCounter> responseTimes();

    void updateResponseTime(long time);
}
