package org.firstinspires.ftc.teamcode.moveatron2k;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.math.Pose;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.pedropathing.utils.Angle;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * Simple point-to-point driver for the mecanum bot with a Pinpoint.
 * Alternative to Pedro's Foresight when all you need is "drive to a world point,
 * end at a heading" straight-line movement. No paths, no braking model: a piecewise
 * proportional controller on position error plus a proportional controller on
 * heading error, with clamped outputs; inside the slowdown bands both channels
 * fall off quadratically (or |error|^slowdownExponent) so the bot decelerates
 * into the target instead of overshooting/hovering on momentum. The turn
 * command is additionally capped (never above {@code maxHeadingPower}) and
 * decays quadratically from {@code fullPowerHeadingDeg} toward a small creep
 * floor, so the spin slows down the closer the target gets and never reaches
 * the ±180° wrap seam with enough momentum to sway across it.
 * The {@code Mecanum} mixer normalizes the final wheel powers, so the robot
 * slows down naturally as the error shrinks.
 *
 * All positions are in MILLIMETERS in the {@link Position Moveatron2k frame}: (0, 0,
 * heading 0) is where the bot sat at OpMode init, so targets are always relative
 * to where you started. Headings are in DEGREES, CCW-positive, 0 = the direction
 * the bot faced at init. {@link #setPosition} redefines the frame (e.g. to the
 * field's coordinates for an autonomous start pose); {@link #getPosition} reads
 * the bot's current spot in that frame.
 *
 * Typical use:
 * <pre>
 *     Moveatron2k moveatron = new Moveatron2k(hardwareMap);               // init()
 *     moveatron.goTo(0.0, 500.0, 90.0);                     // target: x, y [mm], heading [deg]
 *     while (opModeIsActive() &amp;&amp; moveatron.isBusy()) {
 *         moveatron.tick();
 *         telemetry.update();
 *     }
 * </pre>
 */
public class Moveatron2k {

    /** Which frame the manual drive axes are expressed in. */
    public enum MoveType {
        /** Axes are the bot's body frame: +forward = the nose, +strafe = left. */
        BotCentric,
        /** Axes are the Moveatron2k frame (world): +x = wherever the bot faced
         *  at init (or after {@link #setPosition}), +y = that direction's left.
         *  Rotated into the body frame against the current heading each call. */
        FieldCentric
    }

    /** Controller settings. **/
    public static class Config {
        /** Power per mm of forward error when far (|error| >= breakpointMm). */
        public double kpForwardPrimary = Constants.PRIMARY_TRANSLATIONAL_FORWARD_KP / 25.4;
        /** Power per mm of forward error when close (|error| &lt; breakpointMm). */
        public double kpForwardSecondary = Constants.SECONDARY_TRANSLATIONAL_FORWARD_KP / 25.4;
        /** Power per mm of lateral (left-positive) error when far. */
        public double kpStrafePrimary = Constants.PRIMARY_TRANSLATIONAL_LATERAL_KP / 25.4;
        /** Power per mm of lateral error when close. */
        public double kpStrafeSecondary = Constants.SECONDARY_TRANSLATIONAL_LATERAL_KP / 25.4;
        /** Distance where the controller switches between the two gains (mm). */
        public double breakpointMm = Constants.TRANSLATIONAL_BREAKPOINT_IN * 25.4;
        /** Power per radian of heading error. */
        public double kpHeading = Constants.HEADING_KP;
        /** Cap on forward/strafe channel output (0-1). */
        public double maxPower = 1.0;
        /** Cap on turn channel output (0-1). Default 0.5: a full-power turn
         *  reaches ±180° with enough momentum to pin the seam oscillation;
         *  a capped one settles, crosses the 10° tolerance, and stops. */
        public double maxHeadingPower = 0.5;
        /** Target counts as reached within this distance (mm). */
        public double positionToleranceMm = 10.0;
        /** Target counts as reached within this heading error (deg). */
        public double headingToleranceDeg = 10.0;
        /** Approach-shaping exponent inside the slowdown bands: the command
         *  falls off as |error|^slowdownExponent (scaled to equal the linear
         *  gain at the band edge), so the bot decelerates into the target
         *  instead of barreling across it. 1 = the old linear behavior;
         *  2 = quadratic (recommended: kills end-of-move overshoot). */
        public double slowdownExponent = 2.0;
        /** Heading error at which the turn command reaches its cap: beyond it
         *  the turn never gets more power, inside it the command decays
         *  quadratically toward the creep floor, so the spin slows down the
         *  closer the target gets (no momentum to overshoot the ±180° seam). */
        public double fullPowerHeadingDeg = 60.0;
        /** Minimum turn power (0-1) whenever the heading error is outside the
         *  tolerance: guarantees the last stretch still moves instead of
         *  stalling in static friction just outside the band. Below ~40° of
         *  error the creep floor IS the turn command (the quadratic term is
         *  negligible there), so this sets the final-approach spin speed:
         *  0.08 crawled at ~2-4°/s (~20 s for the 40-60° after touchdown);
         *  0.2 is ~2.5x that. If the bot now oscillates around the target,
         *  split the difference (~0.15). */
        public double headingCreepFloor = 0.2;

        /** Inverts the turn channel. Set true when the bot physically rotates
         *  away from its target (turn axis mirrored relative to the Pinpoint's
         *  CCW-positive yaw reading): the loop then converges to the anti-target
         *  180° away and hovers with heading error pinned at the ±180° seam. */
        public boolean reverseRotation = true;

        /** True = the drive motors' zero-power behavior is BRAKE: when the
         *  commanded power hits zero the motors are actively shorted and the
         *  wheels stop dead instead of coasting on momentum (no float glide).
         *  False = FLOAT, Pedro's Mecanum constructor default: free coasting.
         *  Applies whenever this Moveatron2k instance commands the drivetrain,
         *  including the end of a {@link #goTo} move (the bot then halts inside
         *  tolerance instead of drifting past it on the brake). */
        public boolean manualDriveBrake = true;

        public Config() {}

        /** Convenience builder for one-off overrides. */
        public Config with(java.util.function.Consumer<Config> tweak) {
            tweak.accept(this);
            return this;
        }
    }

    /** A pose in the Moveatron2k frame: position in MM, heading in DEGREES CCW
     *  (0 = the frame's x-axis). Immutable. */
    public static class Position {
        public final double xMm;
        public final double yMm;
        public final double headingDeg;

        public Position(double xMm, double yMm, double headingDeg) {
            this.xMm = xMm;
            this.yMm = yMm;
            this.headingDeg = headingDeg;
        }

        @Override
        public String toString() {
            return String.format("(%.1f mm, %.1f mm, %.1f°)", xMm, yMm, headingDeg);
        }
    }

    private final PinpointLocalizer localizer;
    private final Mecanum drivetrain;
    private final Config config;

    // Target in mm/radians; only meaningful while hasTarget is set.
    private double targetX;
    private double targetY;
    private double targetHeading;
    private boolean hasTarget = false;
    private boolean busy = false;

    // Last tick's errors, for telemetry/debug.
    private double forwardError, strafeError, headingError, distanceError;

    // Moveatron2k frame definition: the raw Pinpoint pose (mm/radians) that maps to
    // (0, 0, heading 0). Origin = the spot the bot sat at OpMode init, unless
    // setPosition() redefined it. originCos/Sin cache R(-originHeading).
    private double originX, originY, originHeading, originCos, originSin;

    public Moveatron2k(HardwareMap hardwareMap) {
        this(hardwareMap, new Config());
    }

    public Moveatron2k(HardwareMap hardwareMap, Config config) {
        // Reuse the exact localizer + drivetrain setups from the Pedro configuration.
        this.localizer = new PinpointLocalizer(hardwareMap, Constants.localizerConfig);
        this.drivetrain = new Mecanum(hardwareMap, Constants.drivetrainConfig);
        // Pedro's Mecanum constructor force-sets FLOAT; honor the user toggle.
        this.drivetrain.setZeroPowerBehavior(config.manualDriveBrake
                ? DcMotor.ZeroPowerBehavior.BRAKE
                : DcMotor.ZeroPowerBehavior.FLOAT);
        this.config = config;
        // (0, 0, 0) of the Moveatron2k frame is where the bot sits right now (OpMode init),
        // NOT whatever stale origin the Pinpoint has accumulated since power-on.
        setPosition(0.0, 0.0, 0.0);
    }

    /** Target position (mm) and final heading (deg, CCW-positive, 0 = the Moveatron2k
     *  frame's x-axis: the direction faced at init, or after setPosition). */
    public void goTo(double xMm, double yMm, double headingDeg) {
        targetX = xMm;
        targetY = yMm;
        targetHeading = Math.toRadians(headingDeg);
        hasTarget = true;
        busy = true;
    }

    /** Same as {@link #goTo(double, double, double)}. */
    public void goTo(Position target) {
        goTo(target.xMm, target.yMm, target.headingDeg);
    }

    /**
     * Redefines the Moveatron2k frame so the robot's CURRENT spot reads as the given
     * pose. Called with (0, 0, 0) at construction; call again to anchor the frame
     * to known field coordinates (e.g. {@code setPosition(1200, 600, 180)} at an
     * autonomous start pose). All targets/errors then use the new frame.
     */
    public void setPosition(double xMm, double yMm, double headingDeg) {
        localizer.update();
        Pose raw = localizer.pose(); // inches, per the Pinpoint's globalDistanceUnit
        originHeading = raw.heading() - Math.toRadians(headingDeg);
        originCos = Math.cos(originHeading);
        originSin = Math.sin(originHeading);
        // origin = currentRaw - R(originHeading) * (xMm, yMm)   [2D rigid transform,
        // inverse of currentPose()'s rotate-then-translate].
        originX = raw.x() * 25.4 - (originCos * xMm - originSin * yMm);
        originY = raw.y() * 25.4 - (originSin * xMm + originCos * yMm);
    }

    /** Same as {@link #setPosition(double, double, double)}. */
    public void setPosition(Position pose) {
        setPosition(pose.xMm, pose.yMm, pose.headingDeg);
    }

    /** The robot's current spot in the Moveatron2k frame (fresh Pinpoint read). */
    public Position getPosition() {
        return currentPose();
    }

    private Position currentPose() {
        localizer.update();
        Pose raw = localizer.pose(); // inches, per the Pinpoint's globalDistanceUnit
        double rawX = raw.x() * 25.4;   // mm
        double rawY = raw.y() * 25.4;
        double dx = rawX - originX;
        double dy = rawY - originY;
        return new Position(
                originCos * dx + originSin * dy,
                -originSin * dx + originCos * dy,
                Math.toDegrees(Angle.normalizeSigned(raw.heading() - originHeading)));
    }

    /** Clears the target and stops. Safe to call redundantly. */
    public void cancel() {
        hasTarget = false;
        busy = false;
        drivetrain.applyDrive(DrivePowers.zero());
    }

    /**
     * Manual drive path: applies the given axes straight to the drivetrain —
     * no point-to-point controller, no target set, {@link #tick()} still no-ops.
     * For {@link MoveType#FieldCentric} the axes are in the Moveatron2k frame
     * (world: +forward = the init/setPosition x-axis, +strafe = its left, +turn
     * = CCW) and get rotated into the body frame against the bot's CURRENT
     * heading (fresh Pinpoint read) each call, so pushing forward always drives
     * toward the frame's x-axis no matter which way the bot has since rotated.
     * For {@link MoveType#BotCentric} the axes already are the bot's body frame
     * (+forward = nose, +strafe = left) and pass through.
     *
     * Does NOTHING while the bot is busy with a {@link #goTo} move (i.e.
     * {@link #isBusy()} is true): the closed-loop move owns the wheels until it
     * finishes inside tolerance (its own tick) or {@link #cancel()} clears it.
     *
     * @param moveType which frame the axes are expressed in
     * @param forward  forward power
     * @param strafe   left power
     * @param turn     + = CCW as seen from above (physical convention — the
     *                 closed loop's {@link Config#reverseRotation} compensation
     *                 is goTo's business, not the driver's)
     */
    public void manual(MoveType moveType, double forward, double strafe, double turn) {
        if (busy) {
            return;
        }

        switch (moveType) {
            case FieldCentric: {
                double headingRad = Math.toRadians(currentPose().headingDeg);
                double cos = Math.cos(headingRad);
                double sin = Math.sin(headingRad);
                double bodyForward = forward * cos + strafe * sin;
                double bodyStrafe = -forward * sin + strafe * cos;
                drivetrain.applyDrive(new DrivePowers(bodyForward, bodyStrafe, turn));
                break;
            }
            case BotCentric:
            default:
                drivetrain.applyDrive(new DrivePowers(forward, strafe, turn));
                break;
        }
    }

    /**
     * Read the Pinpoint, compute corrections, and command the drivetrain.
     * Call once per opmode loop. No-op until the first {@link #goTo}.
     * Ends inside tolerance by commanding zero power.
     */
    public void tick() {
        if (!hasTarget) {
            return;
        }

        Position pose = currentPose(); // Moveatron2k frame: 0,0 = OpMode init spot (or last setPosition)
        double x = pose.xMm;
        double y = pose.yMm;
        double heading = Math.toRadians(pose.headingDeg);

        // World-frame error toward the target.
        double worldErrX = targetX - x;
        double worldErrY = targetY - y;
        distanceError = Math.sqrt(worldErrX * worldErrX + worldErrY * worldErrY);

        // Rotate into the robot's body frame (same rotation as Pedro's
        // Vector2D.toBodyFrame): x = forward, y = left, so strafe + = left.
        double cos = Math.cos(heading);
        double sin = Math.sin(heading);
        forwardError = worldErrX * cos + worldErrY * sin;
        strafeError = -worldErrX * sin + worldErrY * cos;
        headingError = Angle.normalizeSigned(targetHeading - heading);

        if (distanceError <= config.positionToleranceMm
                && Math.abs(headingError) <= Math.toRadians(config.headingToleranceDeg)) {
            busy = false;
            drivetrain.applyDrive(DrivePowers.zero());
            return;
        }

        // Same structure as Pedro's Controller.piecewise(small).put(breakpoint, large)
        // far from the target; quadratic approach shaping inside the bands.
        double forward = shaped(forwardError, config.breakpointMm,
                config.kpForwardSecondary, config.kpForwardPrimary, config.maxPower);
        double strafe = shaped(strafeError, config.breakpointMm,
                config.kpStrafeSecondary, config.kpStrafePrimary, config.maxPower);
        // Heading: capped monotone curve. Full power at fullPowerHeadingDeg,
        // decaying quadratically to the creep floor near the target — the spin
        // gets slower the closer it is, so it can't build the momentum that
        // carried it across the ±180° wrap seam and pinned the ±170° sway.
        // Turn reversals near the seam stay equally weak.
        double headingFullRad = Math.toRadians(config.fullPowerHeadingDeg);
        double turnScale = Math.min(1.0, Math.abs(headingError) / headingFullRad);
        double turn = Math.signum(headingError) * (config.headingCreepFloor
                + (config.maxHeadingPower - config.headingCreepFloor) * turnScale * turnScale);
        if (config.reverseRotation) {
            turn = -turn;
        }

        drivetrain.applyDrive(new DrivePowers(forward, strafe, turn));
    }

    /** False once the last tick ended within tolerance (or before the first goTo). */
    public boolean isBusy() {
        return busy;
    }

    /** Distance remaining to the target, mm (last tick). */
    public double distanceErrorMm() {
        return distanceError;
    }

    /** Heading remaining to the target, deg CCW-positive (last tick). */
    public double headingErrorDeg() {
        return Math.toDegrees(headingError);
    }

    /**
     * The tuned piecewise-proportional command when |error| ≥ band; inside the
     * band the command falls off as |error|^slowdownExponent, scaled to match
     * the small-error linear gain at the band edge (so power → 0 quadratically
     * as error → 0: the bot creeps the last stretch and doesn't overshoot
     * across the heading on momentum).
     */
    private double shaped(double error, double band, double kpSmall, double kpLarge, double maxPower) {
        double magnitude = Math.abs(error);
        if (magnitude >= band) {
            return piecewise(error, kpSmall, kpLarge, maxPower);
        }
        double edgePower = clamp(kpSmall * band, maxPower);
        return Math.signum(error) * edgePower * Math.pow(magnitude / band, config.slowdownExponent);
    }

    private double piecewise(double errorMm, double kpSmall, double kpLarge, double maxPower) {
        // Same structure as Pedro's Controller.piecewise(small).put(breakpoint, large):
        // the small-error gain inside the breakpoint, the large-error gain beyond it.
        double kp = Math.abs(errorMm) < config.breakpointMm ? kpSmall : kpLarge;
        return clamp(kp * errorMm, maxPower);
    }

    private static double clamp(double power, double maxPower) {
        return Math.max(-maxPower, Math.min(maxPower, power));
    }
}
