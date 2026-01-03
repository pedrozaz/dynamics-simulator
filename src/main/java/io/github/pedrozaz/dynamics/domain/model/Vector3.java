package io.github.pedrozaz.dynamics.domain.model;

/**
 * Vector R3
 * @param x Component i
 * @param y Component j
 * @param z Component k
 */
public record Vector3(double x, double y, double z) {
    public static final Vector3 ZERO = new Vector3(0, 0, 0);

    // v + u = (x1+x2, y1+y2, z1+z3)
    public Vector3 add(Vector3 o) {
        return new Vector3(this.x + o.x, this.y + o.y, this.z + o.z);
    }

    // c * v
    public Vector3 multiply(double s) {
        return new Vector3(this.x * s, this.y * s, this.z * s);
    }
}
