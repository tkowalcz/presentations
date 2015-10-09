package pl.codewise.hyperion.histogram.hyperion;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class FastThreadSafeCounter implements Counter {

    private static final AtomicLongFieldUpdater<FastThreadSafeCounter> COUNT_UPDATER =
            AtomicLongFieldUpdater.newUpdater(FastThreadSafeCounter.class, "count");

    @SuppressWarnings("unused")
    private volatile long count;

    @Override
    public void increment() {
        COUNT_UPDATER.incrementAndGet(this);
    }

    @Override
    public void decrement() {
        COUNT_UPDATER.decrementAndGet(this);
    }

    @Override
    public void increment(long value) {
        COUNT_UPDATER.addAndGet(this, value);
    }

    @Override
    public void decrement(long value) {
        COUNT_UPDATER.addAndGet(this, -value);
    }

    @Override
    public long getCount() {
        return COUNT_UPDATER.get(this);
    }
}
