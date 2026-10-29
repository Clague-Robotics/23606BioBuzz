package org.firstinspires.ftc.teamcode.pathfind.geometry;

public final class Intersections {
    private Intersections() {
    }

    public static boolean intersects(Circle first, Circle second) {
        double radiusSum = first.getRadius() + second.getRadius();
        return first.getCenter().distanceSquaredTo(second.getCenter()) <= radiusSum * radiusSum;
    }

    public static boolean intersects(Circle circle, Line line) {
        Point start = line.getStart();
        Point end = line.getEnd();
        Point center = circle.getCenter();
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        double lengthSquared = dx * dx + dy * dy;
        double t = lengthSquared == 0 ? 0
                : ((center.getX() - start.getX()) * dx
                + (center.getY() - start.getY()) * dy) / lengthSquared;
        t = Math.max(0, Math.min(1, t));
        double distanceX = center.getX() - (start.getX() + t * dx);
        double distanceY = center.getY() - (start.getY() + t * dy);
        return distanceX * distanceX + distanceY * distanceY <= circle.getRadius() * circle.getRadius();
    }

    public static boolean intersects(Line line, Circle circle) {
        return intersects(circle, line);
    }

    public static boolean intersects(Circle circle, Box box) {
        Point min = box.getMin();
        Point max = box.getMax();
        Point center = circle.getCenter();
        double closestX = Math.max(min.getX(), Math.min(center.getX(), max.getX()));
        double closestY = Math.max(min.getY(), Math.min(center.getY(), max.getY()));
        double dx = center.getX() - closestX;
        double dy = center.getY() - closestY;
        return dx * dx + dy * dy <= circle.getRadius() * circle.getRadius();
    }

    public static boolean intersects(Box box, Circle circle) {
        return intersects(circle, box);
    }

    public static boolean intersects(Line first, Line second) {
        double firstOrientation = orientation(first.getStart(), first.getEnd(), second.getStart());
        double secondOrientation = orientation(first.getStart(), first.getEnd(), second.getEnd());
        double thirdOrientation = orientation(second.getStart(), second.getEnd(), first.getStart());
        double fourthOrientation = orientation(second.getStart(), second.getEnd(), first.getEnd());

        if (oppositeSigns(firstOrientation, secondOrientation)
                && oppositeSigns(thirdOrientation, fourthOrientation)) {
            return true;
        }
        return (firstOrientation == 0 && onSegment(first, second.getStart()))
                || (secondOrientation == 0 && onSegment(first, second.getEnd()))
                || (thirdOrientation == 0 && onSegment(second, first.getStart()))
                || (fourthOrientation == 0 && onSegment(second, first.getEnd()));
    }

    public static boolean intersects(Line line, Box box) {
        if (contains(box, line.getStart()) || contains(box, line.getEnd())) {
            return true;
        }

        Point min = box.getMin();
        Point max = box.getMax();
        Line top = new Line(min, new Point(max.getX(), min.getY()));
        Line bottom = new Line(new Point(min.getX(), max.getY()), max);
        Line left = new Line(min, new Point(min.getX(), max.getY()));
        Line right = new Line(new Point(max.getX(), min.getY()), max);
        return intersects(line, top) || intersects(line, bottom)
                || intersects(line, left) || intersects(line, right);
    }

    public static boolean intersects(Box box, Line line) {
        return intersects(line, box);
    }

    public static boolean intersects(Box first, Box second) {
        return first.getMin().getX() <= second.getMax().getX()
                && first.getMax().getX() >= second.getMin().getX()
                && first.getMin().getY() <= second.getMax().getY()
                && first.getMax().getY() >= second.getMin().getY();
    }

    private static double orientation(Point a, Point b, Point c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY())
                - (b.getY() - a.getY()) * (c.getX() - a.getX());
    }

    private static boolean oppositeSigns(double first, double second) {
        return (first < 0 && second > 0) || (first > 0 && second < 0);
    }

    private static boolean onSegment(Line line, Point point) {
        return point.getX() >= Math.min(line.getStart().getX(), line.getEnd().getX())
                && point.getX() <= Math.max(line.getStart().getX(), line.getEnd().getX())
                && point.getY() >= Math.min(line.getStart().getY(), line.getEnd().getY())
                && point.getY() <= Math.max(line.getStart().getY(), line.getEnd().getY());
    }

    private static boolean contains(Box box, Point point) {
        return point.getX() >= box.getMin().getX() && point.getX() <= box.getMax().getX()
                && point.getY() >= box.getMin().getY() && point.getY() <= box.getMax().getY();
    }
}
