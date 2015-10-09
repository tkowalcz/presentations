package pl.codewise.hyperion.histogram.hyperion;

public interface Counter {

    void increment();

    void decrement();

    void increment(long value);

    void decrement(long value);

    long getCount();
}
