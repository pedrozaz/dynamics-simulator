package io.github.pedrozaz.dynamics.domain.model;

public record RaceResult(
        CarSetup setup,
        double finishTime,
        double distanceRun,
        boolean crashed
) {
    public double calculateFitness() {
        if (crashed) return distanceRun * 0.001;

        return 10000.0 / finishTime;
    }
}
