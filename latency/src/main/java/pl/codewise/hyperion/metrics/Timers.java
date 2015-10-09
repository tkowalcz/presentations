package pl.codewise.hyperion.metrics;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Timer;

public class Timers {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.update(1, TimeUnit.MILLISECONDS);

        for (int i = 0; i < 1000 * 60; i++) {
            ThreadLocalRandom current = ThreadLocalRandom.current();
            double latency = current.nextDouble() * 1000;

            if(current.nextDouble() > 0.99) {
                latency = current.nextDouble() * 20 * 1000;
            }

            timer.update((long) latency, TimeUnit.MILLISECONDS);

        }

        long max = TimeUnit.NANOSECONDS.toMillis(timer.getSnapshot().getMax());
        System.out.println("timer = " + max);
    }
}
