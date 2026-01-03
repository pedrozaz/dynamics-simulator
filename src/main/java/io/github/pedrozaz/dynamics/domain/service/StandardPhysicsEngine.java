package io.github.pedrozaz.dynamics.domain.service;

import io.github.pedrozaz.dynamics.domain.model.CarSetup;
import io.github.pedrozaz.dynamics.domain.model.PhysicsState;
import io.github.pedrozaz.dynamics.domain.model.Vector3;
import org.springframework.stereotype.Service;

@Service
public class StandardPhysicsEngine implements PhysicsEngine {

    // kg/m^3
    private static final double AIR_DENSITY = 1.225;
    // approximated F1 weight (car + driver mass)
    private static final double CAR_MASS = 798.0;

    @Override
    public PhysicsState nextStep(PhysicsState current, CarSetup setup, double dt) {
        Vector3 netForce = calculateNetForce(current, setup);
        Vector3 acceleration = netForce.multiply(1.0 / CAR_MASS);
        Vector3 nextVelocity = current.velocity().add(acceleration.multiply(dt));
        Vector3 nextPosition = current.position().add(nextVelocity.multiply(dt));

        double nextTime = current.time() + dt;

        // TODO: Implement rotation/angular physics in the future
        return new PhysicsState(
                nextTime,
                nextPosition,
                nextVelocity,
                acceleration,
                current.yaw(), current.pitch(), current.roll(),
                current.yawRate(), current.pitchRate(), current.rollRate()
        );
    }

    private Vector3 calculateNetForce(PhysicsState c, CarSetup s) {
        Vector3 engineForce = new Vector3(5000, 0, 0); // placeholder 5000N forward

        // F_drag = -0.5 * rho * rho * v^2 * Cd * A
        double dragCoefficient = (s.frontWingAngle() + s.rearWingAngle()) * 0.05;

        double speed = Math.sqrt(
                c.velocity().x() * c.velocity().x() +
                        c.velocity().y() * c.velocity().y()
        );

        // Mag = 0.5 * rho * v^2 * Cd
        double dragMagnitude = 0.5 * AIR_DENSITY * (speed * speed) * dragCoefficient;

        Vector3 dragForce = Vector3.ZERO;
        if (speed > 0.001) {
            // Normalized velocity vector
            Vector3 velocityDir = c.velocity().multiply(1.0 / speed);

            // Drag vector
            dragForce = velocityDir.multiply(-dragMagnitude);
        }
        return engineForce.add(dragForce);
    }
}
