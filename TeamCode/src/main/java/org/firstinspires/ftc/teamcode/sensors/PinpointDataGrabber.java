
package org.firstinspires.ftc.teamcode.sensors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

@TeleOp(name = "Pinpoint Telemetry Test", group = "Linear OpMode")
public class PinpointDataGrabber extends LinearOpMode {

    private GoBildaPinpointDriver pinpoint;

    @Override
    public void runOpMode() {

        // 1. Map Pinpoint hardware
        pinpoint = hardwareMap.get(
                GoBildaPinpointDriver.class, "pinpoint"
        );

        // 2. Configure encoder resolution
        pinpoint.setEncoderResolution(
                GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD
        );

        // 3. Configure pod offsets (millimeters)
        pinpoint.setOffsets(-60.0, 120.0, DistanceUnit.MM);

        // 4. Configure encoder directions
        pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        );

        // 5. Reset position and calibrate IMU
        pinpoint.resetPosAndIMU();

        telemetry.addLine("Pinpoint Initialized");
        telemetry.addLine("Waiting for start...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // Update Pinpoint data
            pinpoint.update();

            // Get current position
            Pose2D pos = pinpoint.getPosition();

            // Get velocity
            double velX = pinpoint.getVelX(DistanceUnit.MM);
            double velY = pinpoint.getVelY(DistanceUnit.MM);

            // Get heading directly in degrees
            double heading = pos.getHeading(AngleUnit.DEGREES);

            // Get heading velocity in degrees per second
            double headingVelocity = Math.toDegrees(
                    pinpoint.getHeadingVelocity(
                            UnnormalizedAngleUnit.RADIANS
                    )
            );

            // Position telemetry
            telemetry.addData("X Position (in)",
                    pos.getX(DistanceUnit.INCH));

            telemetry.addData("Y Position (in)",
                    pos.getY(DistanceUnit.INCH));

            // Heading telemetry
            telemetry.addData("Heading (degrees)", heading);

            telemetry.addData("Heading Velocity (deg/s)",
                    headingVelocity);

            // Linear velocity telemetry
            telemetry.addData("X Velocity (in/s)",
                    velX / 25.4);

            telemetry.addData("Y Velocity (in/s)",
                    velY / 25.4);

            telemetry.update();
        }
    }
}
