package pl.codewise.hyperion.histogram;

import org.HdrHistogram.Histogram;
import org.HdrHistogram.Recorder;

public class Recording {

    public static final int MILLISECONDS_IN_HOUR = 60 * 60 * 1000;

    public static void main(String[] args) {
        Recorder recorder = new Recorder(1, MILLISECONDS_IN_HOUR, 2);

        HistogramUtils.populateHistogram(value -> recorder.recordValue(value.longValue()));

        Histogram intervalHistogram = recorder.getIntervalHistogram();
        intervalHistogram.outputPercentileDistribution(System.out, 1000.0);

        intervalHistogram = recorder.getIntervalHistogram();
        intervalHistogram.outputPercentileDistribution(System.out, 1000.0);
    }
}
