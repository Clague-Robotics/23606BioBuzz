package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    // Foresight-tuned controller gains, kept as named constants so other simple
    // controllers can auto-populate from the values ForesightTuner produced.
    // Translational gains are power per inch of error; the heading gain is power per radian.
    public static final double PRIMARY_TRANSLATIONAL_FORWARD_KP = 0.4786413209086717;
    public static final double SECONDARY_TRANSLATIONAL_FORWARD_KP = 0.17684517854411133;
    public static final double PRIMARY_TRANSLATIONAL_LATERAL_KP = 0.5844632115717816;
    public static final double SECONDARY_TRANSLATIONAL_LATERAL_KP = 0.21594353953113501;
    public static final double HEADING_KP = 5.0555743617793585;
    public static final double TRANSLATIONAL_BREAKPOINT_IN = 2.5;

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig));
    }

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(PRIMARY_TRANSLATIONAL_FORWARD_KP);
                Controller secondaryTranslationalForward = Controller.proportional(SECONDARY_TRANSLATIONAL_FORWARD_KP);
                Controller primaryTranslationalLateral = Controller.proportional(PRIMARY_TRANSLATIONAL_LATERAL_KP);
                Controller secondaryTranslationalLateral = Controller.proportional(SECONDARY_TRANSLATIONAL_LATERAL_KP);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(TRANSLATIONAL_BREAKPOINT_IN, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(TRANSLATIONAL_BREAKPOINT_IN, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01842031457007383));
                c.brake.set(Controller.proportionalFeedforward(0.015657267384562752));

                c.headingFeedback.set(Controller.proportional(HEADING_KP));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.051366369975103396, 0.009675248886029239));

                c.linearBrakeCoefficients.set(Matrix.diag(0.09593291226461254, 0.08929236878691171));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0017808629494124291, 0.002164070959464674));

                c.maxAchievableForwardVelocity.set(50.19823934826016);
                c.maxAchievableStrafeVelocity.set(42.99234811329732);
                c.naturalForwardDeceleration.set(24.413019630539036);
                c.naturalStrafeDeceleration.set(26.78932212900624);
            }
    );
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-4.724);
        c.yPodOffset.set(2.756);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("MotorFL");
        c.frontRightName.set("MotorFR");
        c.backLeftName.set("MotorBL");
        c.backRightName.set("MotorBR");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });
}
