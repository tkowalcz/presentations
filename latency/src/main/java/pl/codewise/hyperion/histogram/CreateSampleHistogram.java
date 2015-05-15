package pl.codewise.hyperion.histogram;

import org.HdrHistogram.Histogram;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class CreateSampleHistogram {

    public static final int MAX_VALUE = (int) TimeUnit.HOURS.toMillis(1);

    public static void main(String[] args) throws FileNotFoundException {
        Histogram histogram = new Histogram(MAX_VALUE, 2);
        Random random = new Random();

        IntStream.range(0, 1000)
                .map(ignore -> 1)
                .forEach(histogram::recordValue);

        IntStream.range(0, 100)
                .map(ignore -> 10)
                .forEach(histogram::recordValue);

        IntStream.range(0, 10)
                .map(ignore -> 100)
                .forEach(histogram::recordValue);

        histogram.recordValue(1);

//        IntStream.range(0, 10000)
//                .map(ignore -> random.nextInt(MAX_VALUE))
//                .forEach(histogram::recordValue);

        histogram.outputPercentileDistribution(new PrintStream("out.hgrm"), 100.0);
    }
}
