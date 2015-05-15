package pl.codewise.hyperion.demo;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StopperConfigurationBuilder {

    private final List<PauseSpecification> pauses = Lists.newArrayList();
    private PauseSpecification currentPause;

    public StopperConfigurationBuilder pauseFor(long pauseTime) {
        if (currentPause != null) {
            pauses.add(currentPause);
        }

        currentPause = new PauseSpecification();
        currentPause.setPauseTime(pauseTime);
        return this;
    }

    public StopperConfigurationBuilder onceEvery(int amount, TimeUnit timeUnit) {
        currentPause.setProbability(amount, timeUnit);
        return this;
    }

    public List<PauseSpecification> build() {
        if (currentPause != null) {
            pauses.add(currentPause);
        }

        return pauses;
    }

    public static StopperConfigurationBuilder builder() {
        return new StopperConfigurationBuilder();
    }
}
