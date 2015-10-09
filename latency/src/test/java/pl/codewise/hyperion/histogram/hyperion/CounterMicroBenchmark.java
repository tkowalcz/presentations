package pl.codewise.hyperion.histogram.hyperion;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
public class CounterMicroBenchmark {

    private ScalableThreadSafeCounter scalableCounter = new ScalableThreadSafeCounter();
    private FastThreadSafeCounter fastCounter = new FastThreadSafeCounter();
    private SingleProducerCounter spCounter = new SingleProducerCounter();

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void scalableCounterIncrement() {
        scalableCounter.increment();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void scalableCounterDecrement() {
        scalableCounter.decrement();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void fastCounterIncrement() {
        fastCounter.increment();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void fastCounterDecrement() {
        fastCounter.decrement();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void spCounterIncrement() {
        spCounter.increment();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void spCounterDecrement() {
        spCounter.decrement();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + CounterMicroBenchmark.class.getSimpleName() + ".*")
                .warmupIterations(5)
                .measurementIterations(5)
                .threads(1)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
