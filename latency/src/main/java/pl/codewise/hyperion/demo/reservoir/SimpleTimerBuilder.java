package pl.codewise.hyperion.demo.reservoir;

import com.codahale.metrics.Reservoir;
import com.codahale.metrics.Timer;

import java.util.concurrent.TimeUnit;

public class SimpleTimerBuilder {

    public static final int DEFAULT_DECIMAL_POINTS_PRECISION = 3;
    public static final long DEFAULT_MAX_VALUE_TO_TRACK = TimeUnit.HOURS.toMillis(1);

    private ReservoirType reservoirType = ReservoirType.useDefault();

    private TimeUnit minUnitToTrack = TimeUnit.MILLISECONDS;
    private long maxValueToTrackNanos = DEFAULT_MAX_VALUE_TO_TRACK;

    public SimpleTimerBuilder accumulateSingleInterval() {
        reservoirType = ReservoirType.SINGLE;
        return this;
    }

    public SimpleTimerBuilder accumulateRunningTotals() {
        reservoirType = ReservoirType.RUNNING_TOTALS;
        return this;
    }

    public SimpleTimerBuilder minUnitToTrack(TimeUnit minUnitToTrack) {
        this.minUnitToTrack = minUnitToTrack;
        return this;
    }

    public SimpleTimerBuilder maxValueToTrack(TimeUnit timeUnit, long maxValueToTrack) {
        maxValueToTrackNanos = timeUnit.toNanos(maxValueToTrack);
        return this;
    }

    public Timer build() {
        Reservoir reservoir = reservoirType.create(minUnitToTrack, maxValueToTrackNanos, DEFAULT_DECIMAL_POINTS_PRECISION);
        return new Timer(reservoir);
    }
}

