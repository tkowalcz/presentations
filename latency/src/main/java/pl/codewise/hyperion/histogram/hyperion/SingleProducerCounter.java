package pl.codewise.hyperion.histogram.hyperion;

public class SingleProducerCounter implements Counter {

    static {
        try {
            COUNT_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(
                    SingleProducerCounter.class.getDeclaredField("count"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final long COUNT_OFFSET;

    private long count;

    @Override
    public void increment() {
        UnsafeAccess.UNSAFE.putOrderedLong(this, COUNT_OFFSET, count + 1);
    }

    @Override
    public void decrement() {
        UnsafeAccess.UNSAFE.putOrderedLong(this, COUNT_OFFSET, count - 1);
    }

    @Override
    public void increment(long value) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, COUNT_OFFSET, count + value);
    }

    @Override
    public void decrement(long value) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, COUNT_OFFSET, count - value);
    }

    @Override
    public long getCount() {
        return count;
    }
}
