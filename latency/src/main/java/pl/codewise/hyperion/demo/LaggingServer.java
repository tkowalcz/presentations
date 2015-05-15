package pl.codewise.hyperion.demo;

import com.codahale.metrics.Clock;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import java.util.concurrent.TimeUnit;

public class LaggingServer extends Application<LaggingServerConfiguration> {

    public static void main(String[] args) throws Exception {
        new LaggingServer().run(args);
    }

    @Override
    public String getName() {
        return "lag, lag, lag!";
    }

    @Override
    public void run(LaggingServerConfiguration configuration, Environment environment) {
        StopperConfigurationBuilder stopperConfiguration = StopperConfigurationBuilder.builder()
                .pauseFor(15)
                .onceEvery(3, TimeUnit.SECONDS)
                .pauseFor(100)
                .onceEvery(10, TimeUnit.SECONDS)
                .pauseFor(10000)
                .onceEvery(5, TimeUnit.MINUTES)
                .pauseFor(20000)
                .onceEvery(10, TimeUnit.MINUTES);

        Stopper stopper = new Stopper(stopperConfiguration.build());
        stopper.start();

        MetricRegistry metricRegistry = environment.metrics();

        LagerResource resource = new LagerResource(stopper, metricRegistry);
        environment.jersey().register(resource);

        Graphite graphite = new Graphite("127.0.0.1", 9093);
        GraphiteReporter reporter = GraphiteReporter.forRegistry(metricRegistry)
                .prefixedWith("4developers.2015")
                .withClock(Clock.defaultClock())
                .build(graphite);

        reporter.start(60, TimeUnit.SECONDS);
    }
}