package io.github.pedrozaz.dynamics.domain.model;

public record CarSetup(
        // F = -kx (N/m)
        double frontSpringStiffness,
        double rearSpringStiffness,

        // F = -cv (Ns/m)
        double frontDamping,
        double rearDamping,

        // F = 0.5 * rho * v^2 * Cd * A
        double frontWingAngle,
        double rearWingAngle,

        // High = a++
        double finalGearRatio,

        // 0.0 -> 1.0
        double brakeBalance
) {
    public static CarSetup random() {
        // Temp hardcoded mock
        return new CarSetup(100000, 100000, 5000, 5000, 15, 25, 3.4, 0.55);
    }
}
