package pl.codewise.hyperion.metrics;

import com.codahale.metrics.SlidingWindowReservoir;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import org.HdrHistogram.Recorder;
import org.mpierce.metrics.reservoir.hdrhistogram.HdrHistogramReservoir;
import pl.codewise.hyperion.demo.reservoir.HistogramSnapshot;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TimerAbuse {

    public static void main(String[] args) {
        long max = 0;

        Timer actual = new Timer(new SlidingWindowReservoir(120 * 1000));
        HdrHistogramReservoir reservoir = new HdrHistogramReservoir(new Recorder(TimeUnit.HOURS.toNanos(1), 5));
        Timer timer = new Timer();
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

            timer.update(duration, TimeUnit.MILLISECONDS);
            reservoir.update(TimeUnit.MILLISECONDS.toNanos(duration));
            actual.update(duration, TimeUnit.MILLISECONDS);
            max = Math.max(max, duration);
        }

        Snapshot snapshot = timer.getSnapshot();
        Snapshot hdrSnapshot = reservoir.getSnapshot();
        Snapshot actualSnap = actual.getSnapshot();

        long timerMax = TimeUnit.NANOSECONDS.toSeconds(snapshot.getMax());
        long myMax = TimeUnit.MILLISECONDS.toSeconds(max);
        long hdrMax = TimeUnit.NANOSECONDS.toSeconds(hdrSnapshot.getMax());

        long timerP99 = TimeUnit.NANOSECONDS.toSeconds((long) snapshot.get999thPercentile());
        long hdrP99 = TimeUnit.NANOSECONDS.toSeconds((long) hdrSnapshot.get999thPercentile());
        long actualP99 = TimeUnit.NANOSECONDS.toSeconds((long) actualSnap.get999thPercentile());

        System.out.println("Timer max: " + timerMax + ", my max: " + myMax + ", hdr max: " + hdrMax);
        System.out.println("Timer p99: " + timerP99 + ", actual p99: " + actualP99 + ", hdr p99: " + hdrP99);
        if (snapshot instanceof HistogramSnapshot) {
            System.out.println("Snapshot size = " + ((HistogramSnapshot) snapshot).getEstimatedFootprintInBytes());
        } else {
            System.out.println("Snapshot size = " + snapshot.size());
        }
    }
}
