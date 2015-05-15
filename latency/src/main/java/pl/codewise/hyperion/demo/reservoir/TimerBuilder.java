package pl.codewise.hyperion.demo.reservoir;

import com.codahale.metrics.Reservoir;
import com.codahale.metrics.Timer;

import java.util.concurrent.TimeUnit;

public class TimerBuilder {

    private ReservoirType reservoirType = ReservoirType.SINGLE;

    private long maxTrackedTimeInMillis;
    private int decimalPointsPrecision;

    public TimerBuilder maxTrackedRequestTime(long value, TimeUnit timeUnit) {
        this.maxTrackedTimeInMillis = timeUnit.toMillis(value);
        return this;
    }

    public TimerBuilder decimalPointsPrecision(int decimalPointsPrecision) {
        this.decimalPointsPrecision = decimalPointsPrecision;
        return this;
    }

    public TimerBuilder accumulateRunningTotals() {
        reservoirType = ReservoirType.RUNNING_TOTALS;
        return this;
    }

    public Timer build() {
        Reservoir reservoir = reservoirType.create(maxTrackedTimeInMillis, decimalPointsPrecision);
        return new Timer(reservoir);
    }
}
