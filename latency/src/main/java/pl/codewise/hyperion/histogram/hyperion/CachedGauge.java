package pl.codewise.hyperion.histogram.hyperion;

public abstract class CachedGauge<T> implements Gauge<T> {

    private long timeoutNanos;
    private long lastReloadNanos;

    private T value;

    public CachedGauge(long timeoutNanos, long lastReloadNanos) {
        this.timeoutNanos = timeoutNanos;
        this.lastReloadNanos = lastReloadNanos;
    }

    private boolean shouldReload(long currentTimeNanos) {
        return currentTimeNanos - lastReloadNanos > timeoutNanos;
    }

    @Override
    public T getVaue() {
        long currentTimeNanos = System.nanoTime();
        if (shouldReload(currentTimeNanos)) {
            value = loadValue();
            lastReloadNanos = currentTimeNanos;
        }

        return value;
    }

    protected abstract T loadValue();
}
