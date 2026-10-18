package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Limelight Test")
public class LimelightDataGrabber extends LinearOpMode {

    public volatile static int PIPELINE = 3;

    @Override
    public void runOpMode() {
        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");

        // Send telemetry as often as possible so readings stay fresh
        telemetry.setMsTransmissionInterval(11);

        limelight.pipelineSwitch(PIPELINE);

        telemetry.addData("Status", "Limelight Initialized. Ready to start.");
        telemetry.update();

        waitForStart();

        limelight.start();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                telemetry.addData("Target Detected", "YES");
                telemetry.addData("Horizontal Offset (tx)", "%.2f°", result.getTx()); // -30 to 30 degrees
                telemetry.addData("Vertical Offset (ty)", "%.2f°", result.getTy());   // -25 to 25 degrees
                telemetry.addData("Target Area (ta)", "%.2f%%", result.getTa());      // % of image
            } else {
                telemetry.addData("Target Detected", "NO (Searching...)");
            }

            telemetry.update();
        }

        limelight.stop();
    }
}
