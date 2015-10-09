package pl.codewise.hyperion.histogram.hyperion;

import java.util.concurrent.atomic.LongAdder;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class ScalableThreadSafeCounter implements Counter {

    private LongAdder count = new LongAdder();

    @Override
    public void increment() {
        count.increment();
    }

    @Override
    public void decrement() {
        count.decrement();
    }

    @Override
    public void increment(long value) {
        count.add(value);
    }

    @Override
    public void decrement(long value) {
        count.add(-value);
    }

    @Override
    public long getCount() {
        return count.longValue();
    }
}
