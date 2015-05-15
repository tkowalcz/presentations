package pl.codewise.hyperion.demo.reservoir;

import com.codahale.metrics.Reservoir;
import com.codahale.metrics.Snapshot;
import org.HdrHistogram.Recorder;
import org.mpierce.metrics.reservoir.hdrhistogram.HdrHistogramReservoir;

import java.util.concurrent.TimeUnit;

enum ReservoirType {

    SINGLE {
        @Override
        public Reservoir create(long maxTrackedTime, int decimalPointsPrecision) {
            return new SingleSamplePeriodReservoir(maxTrackedTime, decimalPointsPrecision);
        }
    }, RUNNING_TOTALS {
        @Override
        public Reservoir create(long maxTrackedTime, int decimalPointsPrecision) {
            return new HdrHistogramReservoir(new Recorder(maxTrackedTime, decimalPointsPrecision));
        }
    };

    abstract Reservoir create(long maxTrackedTime, int decimalPointsPrecision);

    public Reservoir create(TimeUnit minimalUnitTracked, long maxTrackedTimeNanos, int decimalPointsPrecision) {
        long maxTrackedTime = minimalUnitTracked.convert(maxTrackedTimeNanos, TimeUnit.NANOSECONDS);

        Reservoir result = create(maxTrackedTime, decimalPointsPrecision);
        if (minimalUnitTracked != TimeUnit.NANOSECONDS) {
            result = new ToMinimalTrackedUnitAdjustingDecorator(minimalUnitTracked, result);
        }

        return result;
    }

    public static ReservoirType useDefault() {
        return SINGLE;
    }
}

class ForwardingReservoir implements Reservoir {

    private final Reservoir delegate;

    public ForwardingReservoir(Reservoir delegate) {
        this.delegate = delegate;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void update(long value) {
        delegate.update(value);
    }

    @Override
    public Snapshot getSnapshot() {
        return delegate.getSnapshot();
    }
}

class ToMinimalTrackedUnitAdjustingDecorator extends ForwardingReservoir {

    private final TimeUnit minimalUnitTracked;

    public ToMinimalTrackedUnitAdjustingDecorator(TimeUnit minimalUnitTracked, Reservoir delegate) {
        super(delegate);
        this.minimalUnitTracked = minimalUnitTracked;
    }

    @Override
    public void update(long value) {
        super.update(minimalUnitTracked.convert(value, TimeUnit.NANOSECONDS));
    }

    @Override
    public Snapshot getSnapshot() {
        Snapshot result = super.getSnapshot();
        return new FromMinimalTrackedUnitAdjustingDecorator(result);
    }
}