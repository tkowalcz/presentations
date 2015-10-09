package pl.tkowalcz.examples.subjects;

import java.util.concurrent.TimeUnit;

import org.HdrHistogram.AtomicHistogram;
import org.HdrHistogram.Histogram;

public class PerformanceCounter {

    private Histogram histogram = new AtomicHistogram(TimeUnit.HOURS.toMillis(1), 3);

    public void update(long value) {
        histogram.recordValue(value);
    }

    @Override
    public String toString() {
        return "PerformanceCounter{ p0.95=" +
                histogram.getValueAtPercentile(95) +
                " }";
    }
}
