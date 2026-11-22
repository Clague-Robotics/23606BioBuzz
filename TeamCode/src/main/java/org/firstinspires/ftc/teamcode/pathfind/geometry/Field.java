package org.firstinspires.ftc.teamcode.pathfind.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A playing field with no-go zones the robot must avoid and stay-in zones it must
 * remain inside. Coordinates and dimensions are in millimetres, origin at the
 * bottom-left of the field, x right, y up.
 *
 * Legality conventions (matching {@link Intersections}):
 *  - A footprint overlapping or merely touching a no-go zone is blocked.
 *  - A footprint fully inside or touching the boundary of a stay-in zone is allowed.
 *  - Footprints must be fully inside at least one stay-in zone; if the field
 *    defines no stay-in zones, only the no-go zones apply.
 *
 * A footprint is a {@link Circle}: the robot's position plus a safety radius that
 * should cover its furthest point from the tracked origin, so "allowed" means the
 * whole robot fits.
 */
public final class Field {

    /** A named area of the field that either must be avoided or must be kept. */
    public interface Zone {
        String getName();

        boolean intersects(Circle footprint);

        boolean contains(Circle footprint);
    }

    public static final class RectZone implements Zone {
        private final String name;
        private final Box area;

        public RectZone(String name, Box area) {
            if (name == null || area == null) {
                throw new IllegalArgumentException("Zone name and area must not be null");
            }
            this.name = name;
            this.area = area;
        }

        public String getName() {
            return name;
        }

        public Box getArea() {
            return area;
        }

        public boolean intersects(Circle footprint) {
            return Intersections.intersects(area, footprint);
        }

        public boolean contains(Circle footprint) {
            return Intersections.contains(area, footprint);
        }
    }

    public static final class CircularZone implements Zone {
        private final String name;
        private final Circle area;

        public CircularZone(String name, Circle area) {
            if (name == null || area == null) {
                throw new IllegalArgumentException("Zone name and area must not be null");
            }
            this.name = name;
            this.area = area;
        }

        public String getName() {
            return name;
        }

        public Circle getArea() {
            return area;
        }

        public boolean intersects(Circle footprint) {
            return Intersections.intersects(area, footprint);
        }

        public boolean contains(Circle footprint) {
            return Intersections.contains(area, footprint);
        }
    }

    private final String name;
    private final List<Zone> noGoZones;
    private final List<Zone> stayInZones;

    public Field(String name, List<Zone> noGoZones, List<Zone> stayInZones) {
        if (name == null || noGoZones == null || stayInZones == null) {
            throw new IllegalArgumentException("Field name and zone lists must not be null");
        }
        this.name = name;
        this.noGoZones = new ArrayList<Zone>(noGoZones);
        this.stayInZones = new ArrayList<Zone>(stayInZones);
    }

    public Field(String name, Zone[] noGoZones, Zone[] stayInZones) {
        this(name, noGoZones == null ? null : Arrays.asList(noGoZones),
                stayInZones == null ? null : Arrays.asList(stayInZones));
    }

    public String getName() {
        return name;
    }

    public List<Zone> getNoGoZones() {
        return noGoZones;
    }

    public List<Zone> getStayInZones() {
        return stayInZones;
    }

    /**
     * True when the circle at the given position with the given radius may be occupied:
     * it overlaps no no-go zone and lies fully inside some stay-in zone.
     */
    public boolean isAllowed(double x, double y, double radius) {
        return isAllowed(new Point(x, y), radius);
    }

    public boolean isAllowed(Point center, double radius) {
        return reasonBlocked(center, radius) == null;
    }

    /**
     * Human-readable name of the first zone making this footprint illegal, or
     * "outside stay-in zones" when it is outside every stay-in zone, or null when
     * the footprint is legal. Useful for robot telemetry.
     */
    public String reasonBlocked(Point center, double radius) {
        Circle footprint = new Circle(center, radius);

        for (Zone stayIn : stayInZones) {
            if (stayIn.contains(footprint)) {
                for (Zone noGo : noGoZones) {
                    if (noGo.intersects(footprint)) {
                        return noGo.getName();
                    }
                }
                return null;
            }
        }

        if (stayInZones.isEmpty()) {
            for (Zone noGo : noGoZones) {
                if (noGo.intersects(footprint)) {
                    return noGo.getName();
                }
            }
            return null;
        }

        return "outside stay-in zones";
    }

    public String reasonBlocked(double x, double y, double radius) {
        return reasonBlocked(new Point(x, y), radius);
    }
}
