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
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig));
    }

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.4786413209086717);
                Controller secondaryTranslationalForward = Controller.proportional(0.17684517854411133);
                Controller primaryTranslationalLateral = Controller.proportional(0.5844632115717816);
                Controller secondaryTranslationalLateral = Controller.proportional(0.21594353953113501);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01842031457007383));
                c.brake.set(Controller.proportionalFeedforward(0.015657267384562752));

                c.headingFeedback.set(Controller.proportional(5.0555743617793585));
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
        c.xPodOffset.set(-1.4450288006639858);
        c.yPodOffset.set(0.012187296011316495);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("MotorFL");
        c.frontRightName.set("MotorFR");
        c.backLeftName.set("MotorBL");
        c.backRightName.set("MotorBR");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
    });
}
