package pl.codewise.hyperion.demo.reservoir;

import com.codahale.metrics.Snapshot;

import java.io.OutputStream;

public class FromMinimalTrackedUnitAdjustingDecorator extends Snapshot {

    private Snapshot delegate;

    public FromMinimalTrackedUnitAdjustingDecorator(Snapshot delegate) {
        this.delegate = delegate;
    }

    @Override
    public double getValue(double quantile) {
        return delegate.getValue(quantile) * 1000000;
    }

    @Override
    public long[] getValues() {
        long[] result = delegate.getValues();

        for (int i = 0; i < result.length; i++) {
            result[i] *= 1000000;
        }

        return result;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public long getMax() {
        return delegate.getMax() * 1000000;
    }

    @Override
    public double getMean() {
        return delegate.getMean() * 1000000;
    }

    @Override
    public long getMin() {
        return delegate.getMin() * 1000000;
    }

    @Override
    public double getStdDev() {
        return delegate.getStdDev() * 1000000;
    }

    @Override
    public void dump(OutputStream output) {
        delegate.dump(output);
    }
}
