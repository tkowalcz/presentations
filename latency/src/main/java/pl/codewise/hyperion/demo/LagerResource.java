package pl.codewise.hyperion.demo;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SlidingTimeWindowReservoir;
import com.codahale.metrics.SlidingWindowReservoir;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import com.google.common.util.concurrent.Uninterruptibles;
import org.HdrHistogram.Recorder;
import org.apache.commons.lang3.time.StopWatch;
import org.mpierce.metrics.reservoir.hdrhistogram.HdrHistogramResetOnSnapshotReservoir;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Path("/lager")
@Produces(MediaType.APPLICATION_JSON)
public class LagerResource {

    private final Stopper stopper;

    private final Random random = new Random();

    private final Timer standardTimer;
    private final Timer slidingTimeWindow;
    private final Timer hdrHistogram;
    private final Timer slidingWindow;

    public LagerResource(Stopper stopper, MetricRegistry metricRegistry) {
        this.stopper = stopper;

        standardTimer = metricRegistry.timer("standardTimer");

        slidingTimeWindow = new Timer(new SlidingTimeWindowReservoir(1, TimeUnit.MINUTES));
        metricRegistry.register("slidingWindowTimer", slidingTimeWindow);

        slidingWindow = new Timer(new SlidingWindowReservoir(120 * 10000));
        metricRegistry.register("realTimer", slidingWindow);

        HdrHistogramResetOnSnapshotReservoir reservoir = new HdrHistogramResetOnSnapshotReservoir(new Recorder(4));
        hdrHistogram = new Timer(reservoir);
        metricRegistry.register("hdrHistogramTimer2", hdrHistogram);
    }

    @GET
    @Timed
    public Response lag() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            stopper.pauseIfRequired();
            Uninterruptibles.sleepUninterruptibly(random.nextInt(5), TimeUnit.MILLISECONDS);

            return new Response("lag");
        } finally {
            stopWatch.stop();
            long nanoTime = stopWatch.getNanoTime();

            slidingWindow.update(nanoTime, TimeUnit.NANOSECONDS);
            standardTimer.update(nanoTime, TimeUnit.NANOSECONDS);
            slidingTimeWindow.update(nanoTime, TimeUnit.NANOSECONDS);
            hdrHistogram.update(nanoTime, TimeUnit.NANOSECONDS);
        }
    }
}
