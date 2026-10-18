package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Limelight Test")
public class LimelightDataGrabber extends LinearOpMode {
    @Override
    public void runOpMode() {
        Limelight3A limelight;
        // 1. Map the hardware using your exact configuration name
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        // 2. Clear telemetry queue immediately for maximum performance
        telemetry.setMsTransmissionInterval(11);

        // 3. Select your tuned pipeline (e.g., 0 for Color, 1 for AprilTags)
        limelight.pipelineSwitch(3);

        telemetry.addData("Status", "Limelight Initialized. Ready to start.");
        telemetry.update();

        waitForStart();

        // 4. Start the camera stream
        limelight.start();

        while (opModeIsActive()) {
            // 5. Query the sensor for the freshest frame packet
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                // Access core targeting metrics

                double tx = result.getTx();       // Horizontal offset from crosshair (-30 to 30 degrees)
                double ty = result.getTy();       // Vertical offset from crosshair (-25 to 25 degrees)
                double ta = result.getTa();       // Target area as a % of the total image size
                // String targetId = String.valueOf(result.getClassifierClass()); // Neural network / classification info (if used)

                // 6. Push data to the Driver Station
                telemetry.addData("Target Detected", "YES");
                telemetry.addData("Horizontal Offset (tx)", "%.2f°", tx);
                telemetry.addData("Vertical Offset (ty)", "%.2f°", ty);
                telemetry.addData("Target Area (ta)", "%.2f%%", ta);
                
                // If using an AprilTag pipeline, you can also query individual tag data:
                //if (!result.getAprilTagResults().isEmpty()) {
                    // telemetry.addData("Primary AprilTag ID", result.getAprilTagResults().get(0).getId());
                //}

            } else {
                telemetry.addData("Target Detected", "NO (Searching...)");
            }

            telemetry.update();
        }
    }
}
