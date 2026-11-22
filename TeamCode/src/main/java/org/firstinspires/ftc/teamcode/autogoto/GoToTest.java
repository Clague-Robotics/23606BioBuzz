package org.firstinspires.ftc.teamcode.autogoto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Drives the bot to exactly the example target: x = 0 mm, y = 500 mm, heading = 90 deg
 * — all relative to the GoTo frame's (0, 0, 0) = where the bot sat at OpMode init.
 * Watch telemetry while it runs; if it never goes not-busy, the printed
 * distance/heading error says which channel isn't converging.
 */
@Autonomous(name = "GoTo Point Test")
public class GoToTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        GoTo goTo = new GoTo(hardwareMap);

        telemetry.addLine("GoTo initialized (Pinpoint will recalibrate IMU).");
        telemetry.addLine("Target: X = 0.0, Y = 500.0, heading = 90.0");
        telemetry.update();

        waitForStart();

        // Units: millimeters, heading in degrees.
        goTo.goTo(0.0, 500.0, 90.0);

        while (opModeIsActive() && goTo.isBusy()) {
            goTo.tick();
            telemetry.addData("Position X (mm)", goTo.getPosition().xMm);
            telemetry.addData("Position Y (mm)", goTo.getPosition().yMm);
            telemetry.addData("Distance error (mm)", "%.1f", goTo.distanceErrorMm());
            telemetry.addData("Heading error (deg)", "%.1f", goTo.headingErrorDeg());
            telemetry.addData("Busy", goTo.isBusy());
            telemetry.update();
        }
        goTo.cancel();
        telemetry.addLine("Done.");
        telemetry.update();
        sleep(1000);
    }
}
