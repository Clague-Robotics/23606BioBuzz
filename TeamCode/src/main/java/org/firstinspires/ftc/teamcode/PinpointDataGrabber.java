package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@TeleOp(name = "Pinpoint Telemetry Test", group = "Linear OpMode")
public class PinpointDataGrabber extends LinearOpMode {

    @Override
    public void runOpMode() {
        GoBildaPinpointDriver pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        // goBILDA_4_BAR_POD or goBILDA_SWINGARM_POD
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        // Pod offsets from the robot's center of rotation: setOffsets(xOffset, yOffset, unit)
        pinpoint.setOffsets(-60.0, 120.0, DistanceUnit.MM);

        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        // Clear previous data and calibrate the internal IMU
        pinpoint.resetPosAndIMU();

        telemetry.addData("Status", "Initialized. Waiting for start...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Must be called every loop to read fresh data from the Pinpoint
            pinpoint.update();

            Pose2D pos = pinpoint.getPosition();
            telemetry.addData("X Position (inches)", pos.getX(DistanceUnit.INCH));
            telemetry.addData("Y Position (inches)", pos.getY(DistanceUnit.INCH));
            telemetry.addData("Heading (degrees)", pos.getHeading(AngleUnit.DEGREES));

            telemetry.addData("X Velocity (in/s)", pinpoint.getVelX(DistanceUnit.INCH));
            telemetry.addData("Y Velocity (in/s)", pinpoint.getVelY(DistanceUnit.INCH));
            telemetry.update();
        }
    }
}
