package pl.codewise.hyperion.demo.reservoir;

import com.codahale.metrics.Reservoir;
import com.codahale.metrics.Snapshot;
import org.HdrHistogram.Histogram;
import org.HdrHistogram.Recorder;

public class SingleSamplePeriodReservoir implements Reservoir {

    private final Recorder recorder;

    public SingleSamplePeriodReservoir(long maxTrackedTimeInMillis, int decimalPointsPrecision) {
        recorder = new Recorder(maxTrackedTimeInMillis, decimalPointsPrecision);
    }

    @Override
    public int size() {
        return getSnapshot().size();
    }

    @Override
    public void update(long value) {
        recorder.recordValue(value);
    }

    @Override
    public Snapshot getSnapshot() {
        Histogram intervalHistogram = recorder.getIntervalHistogram();
        return new HistogramSnapshot(intervalHistogram);
    }
}
