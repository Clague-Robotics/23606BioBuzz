package org.firstinspires.ftc.teamcode.pathfind;

import org.firstinspires.ftc.teamcode.pathfind.geometry.Box;
import org.firstinspires.ftc.teamcode.pathfind.geometry.Circle;
import org.firstinspires.ftc.teamcode.pathfind.geometry.Field;
import org.firstinspires.ftc.teamcode.pathfind.geometry.Field.Zone;
import org.firstinspires.ftc.teamcode.pathfind.geometry.Point;

import java.util.Arrays;
import java.util.List;

/**
 * The FTC BioBuzz playing field in millimetres, origin at bottom-left, 12ft (3657.6mm) square.
 */
public final class BioBuzzZones {
    private BioBuzzZones() {}

    // Rectangle corners: bottom-left (x1Mm, y1Mm) and top-right (x2Mm, y2Mm).
    private static Zone rect(String name, double x1Mm, double y1Mm, double x2Mm, double y2Mm) {
        return new Field.RectZone(name, new Box(new Point(x1Mm, y1Mm), new Point(x2Mm, y2Mm)));
    }

    // The circle's position is its center.
    private static Zone circleZone(String name, double xMm, double yMm, double radiusMm) {
        return new Field.CircularZone(name, new Circle(new Point(xMm, yMm), radiusMm));
    }

    public static final List<Zone> NO_GO_ZONES = Arrays.asList(
        rect("Red Hive", 1219.2, 1320.8, 1752.6, 2463.8),
        rect("Blue Hive", 1905.0, 1219.2, 2463.8, 2387.6),
        rect("Central Bridge", 1498.6, 1752.6, 2133.6, 1854.2),

        circleZone("Flower left", 50.8, 1219.2, 50.8),
        circleZone("Flower right", 3606.8, 2438.4, 50.8),
        circleZone("Flower top", 1219.2, 3606.8, 50.8),
        circleZone("Flower bottom", 2438.4, 50.8, 50.8)
    );

    public static final List<Zone> STAY_IN_ZONES = Arrays.asList(
        rect("Playing Field", 0, 0, 3657.6, 3657.6)
    );

    public static final Field FIELD = new Field("BioBuzz", NO_GO_ZONES, STAY_IN_ZONES);
}
