package org.firstinspires.ftc.teamcode.pathfind.geometry;

public final class Box {
    private final Point min;
    private final Point max;

    public Box(Point min, Point max) {
        if (min == null || max == null || min.getX() > max.getX() || min.getY() > max.getY()) {
            throw new IllegalArgumentException("Box bounds must be finite and minimums must not exceed maximums");
        }
        this.min = min;
        this.max = max;
    }

    public Point getMin() {
        return min;
    }

    public Point getMax() {
        return max;
    }
}
