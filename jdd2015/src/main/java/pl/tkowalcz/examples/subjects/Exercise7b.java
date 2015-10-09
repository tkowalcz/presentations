package pl.tkowalcz.examples.subjects;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;
import rx.Observable;

public class Exercise7b {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        PerformanceMonitor performanceMonitor = new PerformanceMonitorImpl();

        Observable.timer(0, 1, TimeUnit.MILLISECONDS)
                .map(ignore -> Math.abs(ThreadLocalRandom.current().nextLong()) % 1000)
                .subscribe(performanceMonitor::updateResponseTime);

        Observable<Double> cpuUsage = performanceMonitor.cpuUsage();
        Observable<PerformanceCounter> performanceCounter = performanceMonitor.responseTimes();

        Observable.combineLatest(performanceCounter, cpuUsage, Pair::of)
                .subscribe(System.out::println);

        System.in.read();
    }
}
