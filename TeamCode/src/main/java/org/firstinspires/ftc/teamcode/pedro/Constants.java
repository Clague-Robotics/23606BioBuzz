package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return null;
    }

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.5340512830237598);
                Controller secondaryTranslationalForward = Controller.proportional(0.19731767896418043);
                Controller primaryTranslationalLateral = Controller.proportional(0.31926220877976974);
                Controller secondaryTranslationalLateral = Controller.proportional(0.11795885530079156);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.018365502141717897));
                c.brake.set(Controller.proportionalFeedforward(0.015610676820460212));

                c.headingFeedback.set(Controller.proportional(3.614652922997446));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04646737073530826, 0.015690140409382655));

                c.linearBrakeCoefficients.set(Matrix.diag(0.008534370429969525, 0.10140056799968229));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.005299240333419522, 0.0013727675804053427));

                c.maxAchievableForwardVelocity.set(54.84128038360168);
                c.maxAchievableStrafeVelocity.set(44.89242111987923);
                c.naturalForwardDeceleration.set(32.27110804127517);
                c.naturalStrafeDeceleration.set(42.512710816052994);
            }
    );
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(3.189373166542354);
        c.yPodOffset.set(-6.80692657710999);
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
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });
}
