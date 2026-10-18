    package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
// Import the official goBILDA driver package
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

    @TeleOp(name = "Pinpoint Telemetry Test", group = "Linear OpMode")
public class PinpointDataGrabber extends LinearOpMode {

    // Declare the Pinpoint device
    private GoBildaPinpointDriver pinpoint;

    @Override
    public void runOpMode() {
        // 1. Map the hardware using the name configured on your Driver Hub
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        /*
         * 2. Configure your specific hardware profile.
         * If using standard goBILDA pods, choose the matching built-in preset.
         * Options: goBILDA_4Bar_Pod or goBILDA_SwingArm_Pod
         */
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        /*
         * 3. Define the physical positions (offsets) of your pods relative
         * to the center of your robot's rotation in millimeters.
         * Format: setOffsets(xOffset, yOffset);
         */
        pinpoint.setOffsets(-60.0, 120.0, DistanceUnit.MM);

        /*
         * 4. Set the encoder directions based on how you have mounted them.
         * Options: REVERSED or FORWARD
         */
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        // 5. Clear previous data and calibrate the internal IMU sensor
        pinpoint.resetPosAndIMU();

        telemetry.addData("Status", "Initialized. Waiting for start...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            /*
             * CRITICAL STEP: You must call update() frequently in your loop.
             * This tells the hardware map to grab the latest burst of telemetry
             * data processed by the Pinpoint coprocessor.
             */
            pinpoint.update();

            // Fetch the calculated coordinates relative to your starting position
            Pose2D pos = pinpoint.getPosition();
            // Fetch live velocities (measured in units per second)
            Pose2D vel = new Pose2D(
                    DistanceUnit.MM,
                    pinpoint.getVelX(DistanceUnit.MM),
                    pinpoint.getVelY(DistanceUnit.MM),
                    AngleUnit.RADIANS,
                    pinpoint.getHeadingVelocity(UnnormalizedAngleUnit.RADIANS)
            );
            // Display your current field coordinates via Telemetry
            telemetry.addData("X Position (inches)", pos.getX(DistanceUnit.INCH));
            telemetry.addData("Y Position (inches)", pos.getY(DistanceUnit.INCH));
            telemetry.addData("Heading (degrees)", Math.toDegrees(pos.getHeading(AngleUnit.DEGREES)));

            telemetry.addData("X Velocity", vel.getX(DistanceUnit.INCH));
            telemetry.addData("Y Velocity", vel.getY(DistanceUnit.INCH));
            telemetry.update();
        }
    }
}
