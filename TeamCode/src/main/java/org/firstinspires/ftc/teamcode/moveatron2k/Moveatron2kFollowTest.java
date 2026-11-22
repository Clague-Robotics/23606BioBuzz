package org.firstinspires.ftc.teamcode.moveatron2k;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Drives the bot to exactly the example target: x = 0 mm, y = 500 mm, heading = 90 deg
 * — all relative to the Moveatron2k frame's (0, 0, 0) = where the bot sat at OpMode init.
 * Watch telemetry while it runs; if it never goes not-busy, the printed
 * distance/heading error says which channel isn't converging.
 */
@Autonomous(name = "Moveatron2k Follow Test")
public class Moveatron2kFollowTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        Moveatron2k moveatron = new Moveatron2k(hardwareMap);

        telemetry.addLine("Moveatron2k initialized (Pinpoint will recalibrate IMU).");
        telemetry.addLine("Target: X = 0.0, Y = 500.0, heading = 90.0");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Units: millimeters, heading in degrees.
            moveatron.goTo(0.0, 500.0, 90.0);

            while (opModeIsActive() && moveatron.isBusy()) {
                moveatron.tick();
                telemetry.addData("Position X (mm)", moveatron.getPosition().xMm);
                telemetry.addData("Position Y (mm)", moveatron.getPosition().yMm);
                telemetry.addData("Distance error (mm)", "%.1f", moveatron.distanceErrorMm());
                telemetry.addData("Heading error (deg)", "%.1f", moveatron.headingErrorDeg());
                telemetry.addData("Busy", moveatron.isBusy());
                telemetry.update();
            }
            moveatron.cancel();
            telemetry.addLine("Done. Heading to the next position.");
            telemetry.update();

            sleep(500);

            moveatron.goTo(500.0, 200.0, 270.0);

            while (opModeIsActive() && moveatron.isBusy()) {
                moveatron.tick();
                telemetry.addData("Position X (mm)", moveatron.getPosition().xMm);
                telemetry.addData("Position Y (mm)", moveatron.getPosition().yMm);
                telemetry.addData("Distance error (mm)", "%.1f", moveatron.distanceErrorMm());
                telemetry.addData("Heading error (deg)", "%.1f", moveatron.headingErrorDeg());
                telemetry.addData("Busy", moveatron.isBusy());
                telemetry.update();
            }
            sleep(1000);

            moveatron.goTo(0.0, 0.0, 90.0);

            while (opModeIsActive() && moveatron.isBusy()) {
                moveatron.tick();
                telemetry.addData("Position X (mm)", moveatron.getPosition().xMm);
                telemetry.addData("Position Y (mm)", moveatron.getPosition().yMm);
                telemetry.addData("Distance error (mm)", "%.1f", moveatron.distanceErrorMm());
                telemetry.addData("Heading error (deg)", "%.1f", moveatron.headingErrorDeg());
                telemetry.addData("Busy", moveatron.isBusy());
                telemetry.update();
            }
            moveatron.cancel();
            telemetry.addLine("Done. Heading to the next position.");
            telemetry.update();

            sleep(500);
        }
    }
}
