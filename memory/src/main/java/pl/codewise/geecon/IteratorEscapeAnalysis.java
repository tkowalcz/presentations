package pl.codewise.geecon;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class IteratorEscapeAnalysis {
    @Param({"1000"})
    public int size;

    private Vector<Long> list;

    @Setup(Level.Trial)
    public void buildData() throws Exception {
        list = new Vector<Long>();
        for (long ii = 512; ii < size + 512; ii++) {
            list.add(ii);
        }
    }

    @CompilerControl(CompilerControl.Mode.PRINT)
    @Benchmark
    public long sumEnumerationOverList() {
        long sum = 0;
        Iterator<Long> iterator = new EnumerationAsIterator<>(list.elements());
        for (; iterator.hasNext(); ) {
            sum += iterator.next();
        }
        return sum;
    }

    @Benchmark
    public long sumIteratorOverList() {
        long sum = 0;
        for (Iterator<Long> iter = list.iterator(); iter.hasNext(); ) {
            sum += iter.next();
        }
        return sum;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + IteratorEscapeAnalysis.class.getSimpleName() + ".*")
                .warmupIterations(10)
                .measurementIterations(10)
                .jvmArgs("-Xms64m", "-Xmx64m", "-XX:+DoEscapeAnalysis")
                .addProfiler(GCProfiler.class)
                .threads(1)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}