package org.firstinspires.ftc.teamcode.pathfind.geometry;

public final class Circle {
    private final Point center;
    private final double radius;

    public Circle(Point center, double radius) {
        if (center == null || !isFinite(radius) || radius < 0) {
            throw new IllegalArgumentException(
                    "Circle center must not be null and radius must be finite and non-negative");
        }
        this.center = center;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    private static boolean isFinite(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }
}
