package pl.codewise.hyperion.demo;

import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Stopper extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(Stopper.class);

    private List<PauseSpecification> pauseSpecifications;

    private volatile long endTime;

    public Stopper(List<PauseSpecification> pauseSpecifications) {
        this.pauseSpecifications = pauseSpecifications;
    }

    public void pauseIfRequired() {
        while (System.currentTimeMillis() < endTime) {
            LOGGER.info("Pausing...");
            Uninterruptibles.sleepUninterruptibly(endTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void run() {
        Random random = new Random();
        //noinspection InfiniteLoopStatement
        while (true) {
            for (PauseSpecification spec : pauseSpecifications) {
                double probability = spec.getScaledProbability(100, TimeUnit.MILLISECONDS);
                if (random.nextDouble() < probability) {
                    endTime = System.currentTimeMillis() + spec.getPauseTime();

                    LOGGER.info("Pausing for " + spec.getPauseTime() + "ms");
                    pauseIfRequired();
                }
            }

            Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
        }
    }
}
