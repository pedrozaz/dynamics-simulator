package io.github.pedrozaz.dynamics.domain.service;

import io.github.pedrozaz.dynamics.domain.model.CarSetup;
import io.github.pedrozaz.dynamics.domain.model.PhysicsState;
import io.github.pedrozaz.dynamics.domain.model.RaceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class SimulationOrchestrator {

    private final PhysicsEngine physicsEngine;

    // Mock parameters
    private static final double TRACK_LENGTH_METERS = 1000.0;
    private static final double TIME_STEP = 0.01;
    private static final double MAX_TIME_SECONDS = 120.0;

    /**
     * Runs simulation for all population at the same time
     */
    public List<RaceResult> runGeneration(List<CarSetup> population) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<RaceResult>> futures = population.stream()
                    .map(o -> executor.submit(() -> simulateRace(o)))
                    .toList();

            return futures.stream()
                    .map(f -> {
                        try {
                            return f.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException("Simulation failed", e);
                        }
                    })
                    .toList();
        }
    }

    /**
     * Simulates a single run for each vehicle
     * This method runs isolate in their own Virtual Thread
     */
    private RaceResult simulateRace(CarSetup setup) {
        PhysicsState state = PhysicsState.initial();

        while (state.position().x() < TRACK_LENGTH_METERS & state.time() < MAX_TIME_SECONDS) {
            state = physicsEngine.nextStep(state, setup, TIME_STEP);
        }

        boolean finished = state.position().x() >= TRACK_LENGTH_METERS;

        return new RaceResult(
                setup,
                state.time(),
                state.position().x(),
                !finished
        );
    }
}
