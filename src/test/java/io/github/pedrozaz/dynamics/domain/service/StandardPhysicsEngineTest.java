package io.github.pedrozaz.dynamics.domain.service;

import io.github.pedrozaz.dynamics.domain.model.CarSetup;
import io.github.pedrozaz.dynamics.domain.model.PhysicsState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardPhysicsEngineTest {

    private final StandardPhysicsEngine engine = new StandardPhysicsEngine();

    @Test
    @DisplayName("Should reach terminal velocity when drag equals engine force")
    void testTerminalVelocityConvergence() {
        CarSetup highDragSetup = new CarSetup(
                50000, 50000,
                3000, 3000,
                30.0, 45.0,
                3.4, 0.55
        );

        PhysicsState state = PhysicsState.initial();
        double dt = 0.01;

        for (int i = 0; i < 6000; i++) {
            state = engine.nextStep(state, highDragSetup, dt);
        }

        assertTrue(state.position().x() > 1000, "Car should have moved forward significantly");

        assertEquals(0.0, state.acceleration().x(), 0.1,
                "Acceleration should be near zero at terminal velocity");

        double finalSpeed = state.velocity().x();
        System.out.println("Terminal Velocity Reached: " + finalSpeed + " m/s (" + (finalSpeed * 3.6) + " km/h)");

        assertTrue(finalSpeed > 10.0, "Speed should be positive");
        assertTrue(finalSpeed < 1000.0, "Speed should not be supersonic");
    }
}
