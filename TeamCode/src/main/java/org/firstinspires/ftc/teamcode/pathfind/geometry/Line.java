package org.firstinspires.ftc.teamcode.pathfind.geometry;

public final class Line {
    private final Point start;
    private final Point end;

    public Line(Point start, Point end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Line points must not be null");
        }
        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
