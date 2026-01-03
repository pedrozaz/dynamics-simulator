package io.github.pedrozaz.dynamics.domain.service;

import io.github.pedrozaz.dynamics.domain.model.CarSetup;
import io.github.pedrozaz.dynamics.domain.model.PhysicsState;

/**
 * Service responsible for resolving the equation of motion.
 * It takes the current state (t) and returns the next state (t + dt).
 */
public interface PhysicsEngine {

    /**
     * Calculates the next physics step.
     *
     * @param currentState The snapshot of the car at time `t`.
     * @param setup        The car's configuration (DNA).
     * @param dt           Delta time in seconds (e.g., 0.01s for 100Hz)
     * @return A new PhysicsState object representing time `t + dt`.
     */
    PhysicsState nextStep(PhysicsState currentState, CarSetup setup, double dt);
}
