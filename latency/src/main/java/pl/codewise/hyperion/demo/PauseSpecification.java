package pl.codewise.hyperion.demo;

import java.util.concurrent.TimeUnit;

public class PauseSpecification {

    private long pauseTime;

    private int amount;
    private TimeUnit timeUnit;

    public long getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }

    public double getScaledProbability(int requiredAmount, TimeUnit requiredTimeUnit) {
        double requiredFrequency = requiredTimeUnit.toMillis(requiredAmount);
        double setFrequency = timeUnit.toMillis(amount);

        return requiredFrequency / setFrequency;
    }

    public void setProbability(int amount, TimeUnit timeUnit) {
        this.amount = amount;
        this.timeUnit = timeUnit;
    }
}
