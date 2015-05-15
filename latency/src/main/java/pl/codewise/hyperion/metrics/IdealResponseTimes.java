package pl.codewise.hyperion.metrics;

import com.codahale.metrics.Timer;

import java.util.concurrent.TimeUnit;

public class IdealResponseTimes {

    public static final int CONSTANT_LATENCY = 42;

    public static final int TPS = 1000;
    public static final int SECONDS_BETWEEN_MEASUREMENTS = 60;

    public static void main(String[] args) {
        Timer timer = new Timer();
        for (int i = 0; i < SECONDS_BETWEEN_MEASUREMENTS * TPS; i++) {
            timer.update(CONSTANT_LATENCY, TimeUnit.MILLISECONDS);
        }

        long timerMax = TimeUnit.NANOSECONDS.toMillis(timer.getSnapshot().getMax());
        System.out.println("Timer max: " + timerMax);
    }
}
