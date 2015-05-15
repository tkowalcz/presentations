package pl.codewise.hyperion.histogram;

import org.HdrHistogram.AbstractHistogram;
import rx.Observable;

import java.util.Random;
import java.util.function.Consumer;

public class HistogramUtils {

    public static final int DEFAULT_RANDOM_SEED = 3141592;

    public static void populateHistogram(AbstractHistogram histogram) {
        measurementStream(DEFAULT_RANDOM_SEED)
                .subscribe(modifier -> histogram.recordValue(modifier.longValue()));
    }

    public static void populateHistogram(Consumer<Number> histogram) {
        measurementStream(DEFAULT_RANDOM_SEED)
                .subscribe(modifier -> histogram.accept(modifier.longValue()));
    }

    public static void populateHistogram(int randomSeed, Consumer<Number> histogram) {
        measurementStream(randomSeed)
                .subscribe(modifier -> histogram.accept(modifier.longValue()));
    }

    public static Observable<? extends Number> measurementStream(int randomSeed) {
        Random random = new Random(randomSeed);

        return Observable.range(0, 100)
                .map(ignore -> random.nextDouble())
                .map(Math::abs)
                .mergeWith(
                        Observable.range(0, 90)
                                .map(ignore -> random.nextDouble() * 5)
                                .map(Math::abs))
                .mergeWith(
                        Observable.range(0, 100)
                                .map(ignore -> random.nextDouble() * 10)
                                .map(Math::abs))
                .mergeWith(
                        Observable.range(0, 100)
                                .map(ignore -> random.nextDouble() * 100)
                                .map(Math::abs))
                .mergeWith(
                        Observable.range(0, 100)
                                .map(ignore -> random.nextDouble() * 10000)
                                .map(Math::abs))
                .mergeWith(
                        Observable.range(0, 100)
                                .map(ignore -> random.nextDouble() * 100000)
                                .map(Math::abs))
                .mergeWith(
                        Observable.range(0, 100)
                                .map(ignore -> random.nextDouble() * 100000)
                                .map(Math::abs));
    }

    public static void describeHistogram(AbstractHistogram histogram) {
        System.out.println("histogram.getLowestDiscernibleValue() = " + histogram.getLowestDiscernibleValue());
        System.out.println("histogram.getHighestTrackableValue() = " + histogram.getHighestTrackableValue());

        System.out.println("histogram.highestEquivalentValue(72700) = " + histogram.highestEquivalentValue(72700));
        System.out.println("histogram.getNeededByteBufferCapacity() = " + histogram.getNeededByteBufferCapacity());
        System.out.println("histogram.getCountBetweenValues() = " + histogram.getCountBetweenValues(1, 84));
        System.out.println("histogram.getValueAtPercentile(0.75) = " + histogram.getValueAtPercentile(75.0));
    }
}
