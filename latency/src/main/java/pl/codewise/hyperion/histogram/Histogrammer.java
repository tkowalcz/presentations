package pl.codewise.hyperion.histogram;

import org.HdrHistogram.AbstractHistogram;
import org.HdrHistogram.Histogram;
import org.HdrHistogram.IntCountsHistogram;
import org.HdrHistogram.ShortCountsHistogram;

import java.io.IOException;

public class Histogrammer {

    public static final int MILLISECONDS_IN_HOUR = 60 * 60 * 1000;

    public static void main(String[] args) throws IOException {
        Histogram basicHistogram = new Histogram(1, 60 * 60 * 1000, 2);
        HistogramUtils.populateHistogram(basicHistogram);

        ShortCountsHistogram shortCountsHistogram = new ShortCountsHistogram(1, MILLISECONDS_IN_HOUR, 2);
        HistogramUtils.populateHistogram(shortCountsHistogram);

        IntCountsHistogram intCountsHistogram = new IntCountsHistogram(1, MILLISECONDS_IN_HOUR, 2);
        HistogramUtils.populateHistogram(intCountsHistogram);

        System.in.read();

        System.out.println("basicHistogram.getEstimatedFootprintInBytes() = " + basicHistogram.getEstimatedFootprintInBytes());
        System.out.println("shortCountsHistogram.getEstimatedFootprintInBytes() = " + shortCountsHistogram.getEstimatedFootprintInBytes());
        System.out.println("intCountsHistogram.getEstimatedFootprintInBytes() = " + intCountsHistogram.getEstimatedFootprintInBytes());

        System.in.read();

        HistogramUtils.describeHistogram(basicHistogram);
        HistogramUtils.describeHistogram(shortCountsHistogram);
        HistogramUtils.describeHistogram(intCountsHistogram);

        System.in.read();

        basicHistogram.outputPercentileDistribution(System.out, 1.0);
        shortCountsHistogram.outputPercentileDistribution(System.out, 1.0);
        intCountsHistogram.outputPercentileDistribution(System.out, 1.0);

        System.in.read();
//        Observable.range(1, Short.MAX_VALUE + 1).subscribe(ignore -> basicHistogram.recordValue(123445));
//        Observable.range(1, Short.MAX_VALUE + 1).subscribe(ignore -> shortCountsHistogram.recordValue(123445));
//        Observable.range(1, Short.MAX_VALUE + 1).subscribe(ignore -> intCountsHistogram.recordValue(123445));

        AbstractHistogram.Percentiles percentiles = basicHistogram.percentiles(1);
        percentiles.forEach(percentile -> {
            System.out.println("percentile.getPercentile() = " + percentile.getPercentile());
            System.out.println("percentile.getPercentile() = " + percentile.getTotalCountToThisValue());
        });
    }
}
