package pl.codewise.hyperion.demo.reservoir;

import com.codahale.metrics.Snapshot;
import org.HdrHistogram.Histogram;
import org.HdrHistogram.HistogramIterationValue;

import javax.annotation.Nonnull;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class HistogramSnapshot extends Snapshot {
    private final Histogram histogram;

    public HistogramSnapshot(@Nonnull Histogram histogram) {
        this.histogram = histogram;
    }

    @Override
    public double getValue(double quantile) {
        return histogram.getValueAtPercentile(quantile * 100.0);
    }

    @Override
    public long[] getValues() {
        long[] vals = new long[(int) histogram.getTotalCount()];
        int i = 0;

        for (HistogramIterationValue value : histogram.recordedValues()) {
            long val = value.getValueIteratedTo();

            for (int j = 0; j < value.getCountAddedInThisIterationStep(); j++) {
                vals[i] = val;

                i++;
            }
        }

        if (i != vals.length) {
            throw new IllegalStateException(
                "Total count was " + histogram.getTotalCount() + " but iterating values produced " + vals.length);
        }

        return vals;
    }

    @Override
    public int size() {
        return (int) histogram.getTotalCount();
    }

    @Override
    public long getMax() {
        return histogram.getMaxValue();
    }

    @Override
    public double getMean() {
        return histogram.getMean();
    }

    @Override
    public long getMin() {
        return histogram.getMinValue();
    }

    @Override
    public double getStdDev() {
        return histogram.getStdDeviation();
    }

    @Override
    public void dump(OutputStream output) {
        try (PrintWriter p = new PrintWriter(new OutputStreamWriter(output, UTF_8))) {
            for (HistogramIterationValue value : histogram.recordedValues()) {
                for (int j = 0; j < value.getCountAddedInThisIterationStep(); j++) {
                    p.printf("%d%n", value.getValueIteratedTo());
                }
            }
        }
    }

    public long getEstimatedFootprintInBytes() {
        return histogram.getEstimatedFootprintInBytes();
    }
}
