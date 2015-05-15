package pl.codewise.hyperion.demo.omission;

import org.HdrHistogram.Histogram;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CoordinatedOmissionCorrection {

    public static void main(String[] args) throws FileNotFoundException {
        Histogram histogram = new Histogram(TimeUnit.MINUTES.toMicros(10), 3);
        Histogram correctedHistogram = new Histogram(TimeUnit.MINUTES.toMicros(10), 3);
        for (int i = 0; i < 100000; i++) {
            long duration = (long) (ThreadLocalRandom.current().nextDouble() * 1000);

            if (ThreadLocalRandom.current().nextDouble() > 0.99) {
                duration = (long) (ThreadLocalRandom.current().nextDouble() * 5 * 1000);
            }

            if (ThreadLocalRandom.current().nextDouble() > 0.999) {
                duration = (long) (ThreadLocalRandom.current().nextDouble() * 20 * 1000);
            }

            if (ThreadLocalRandom.current().nextDouble() > 0.9999) {
                duration = (long) (ThreadLocalRandom.current().nextDouble() * 120 * 1000);
            }

            long latency = TimeUnit.MILLISECONDS.toMicros(duration);
            histogram.recordValue(latency);
            correctedHistogram.recordValueWithExpectedInterval(latency, 1000);
        }

        histogram.outputPercentileDistribution(new PrintStream("histogram.hgrm"), 1000.0);
        correctedHistogram.outputPercentileDistribution(new PrintStream("correctedHistogram.hgrm"), 1000.0);
        histogram
                .copyCorrectedForCoordinatedOmission(TimeUnit.MILLISECONDS.toMicros(1))
                .outputPercentileDistribution(new PrintStream("postCorrectedHistogram.hgrm"), 1000.0);
    }
}
