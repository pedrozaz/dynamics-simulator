package io.github.pedrozaz.dynamics.domain.model;

/**
 * Represents instantly position of vehicle at instant `t`.
 * Receives State(t) and returns State(t+1)
 *
 * @param time seconds
 * @param position x, y, z
 * @param velocity First Derivative (m/s)
 * @param acceleration Second Derivative (telemetry/G-Force)
 * @param yaw
 * @param pitch
 * @param roll
 * @param yawRate
 * @param pitchRate
 * @param rollRate
 */
public record PhysicsState(
        double time,

        Vector3 position,
        Vector3 velocity,
        Vector3 acceleration,

        // Euler Angles
        double yaw,
        double pitch,
        double roll,

        // rad/s
        double yawRate,
        double pitchRate,
        double rollRate
) {
    public static PhysicsState initial() {
        return new PhysicsState(
                0.0,
                Vector3.ZERO, Vector3.ZERO, Vector3.ZERO,
                0.0, 0.0, 0.0,
                0.0, 0.0, 0.0
        );
    }
}
