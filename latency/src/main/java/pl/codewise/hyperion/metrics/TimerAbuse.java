package pl.codewise.hyperion.metrics;

import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TimerAbuse {

    public static void main(String[] args) {
        long max = 0;

        Timer actual = new Timer();
        for (int i = 0; i < 60 * 1000; i++) {
            long duration = (long) (ThreadLocalRandom.current().nextDouble() * 1000);

            if (ThreadLocalRandom.current().nextDouble() > 0.9) {
                duration = (long) (ThreadLocalRandom.current().nextDouble() * 5 * 1000);
            }

            if (ThreadLocalRandom.current().nextDouble() > 0.99) {
                duration = (long) (ThreadLocalRandom.current().nextDouble() * 20 * 1000);
            }

            if (ThreadLocalRandom.current().nextDouble() > 0.999) {
                duration = (long) (ThreadLocalRandom.current().nextDouble() * 120 * 1000);
            }

            actual.update(duration, TimeUnit.MILLISECONDS);
            max = Math.max(max, duration);
        }

        Snapshot actualSnap = actual.getSnapshot();

        System.out.println("max = " + TimeUnit.NANOSECONDS.toMillis(actualSnap.getMax()));
        System.out.println("mymax = " + max);


    }
}
