package org.firstinspires.ftc.teamcode.pathfind.geometry;

public final class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        if (!isFinite(x) || !isFinite(y)) {
            throw new IllegalArgumentException("Point coordinates must be finite");
        }
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceSquaredTo(Point other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return dx * dx + dy * dy;
    }

    private static boolean isFinite(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }
}
