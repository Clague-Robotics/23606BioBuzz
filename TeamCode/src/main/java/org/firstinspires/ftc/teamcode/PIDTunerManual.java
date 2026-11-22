package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "PIDF Manual Tuner")
public class PIDTunerManual extends LinearOpMode {
    @Override
    public void runOpMode() {
        LaunchController launchController = new LaunchController(hardwareMap);

        telemetry.addLine("A/B/X/Y: adjust kP/kI/kD/kF with right stick");
        telemetry.addLine("Left trigger: intake; right trigger: kicker");
        telemetry.addLine("Left bumper: reverse intake; right bumper: reverse kicker");
        telemetry.addLine("X release: toggle launcher; D-pad up/down: adjust speed");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            launchController.updateInput(
                    gamepad1.left_trigger > 0.5,  // Intake
                    gamepad1.right_trigger > 0.5, // Kicker
                    gamepad1.xWasReleased(),     // Launcher toggle
                    gamepad1.left_bumper,        // Reverse intake
                    gamepad1.right_bumper,       // Reverse kicker
                    gamepad1.dpadUpWasReleased(),   // Increase speed
                    gamepad1.dpadDownWasReleased()  // Decrease speed
            );

            double multiplier = 0.01000;

            if (gamepad1.rightBumperWasReleased()) multiplier *= 10;
            if (gamepad1.leftBumperWasReleased()) multiplier *= 0.1;


            double gainChange = gamepad1.right_stick_y * multiplier;
            boolean gainsChanged = false;

            if (gamepad1.a) {
                launchController.launchConfig.kP += gainChange;
                gainsChanged = true;
            }
            if (gamepad1.b) {
                launchController.launchConfig.kI += gainChange;
                gainsChanged = true;
            }
            if (gamepad1.x) {
                launchController.launchConfig.kD += gainChange;
                gainsChanged = true;
            }
            if (gamepad1.y) {
                launchController.launchConfig.kF += gainChange;
                gainsChanged = true;
            }

            launchController.launchConfig.kF = Math.max(0, launchController.launchConfig.kF);
            launchController.launchConfig.kP = Math.max(0, launchController.launchConfig.kP);
            launchController.launchConfig.kI = Math.max(0, launchController.launchConfig.kI);
            launchController.launchConfig.kD = Math.max(0, launchController.launchConfig.kD);

            // Make updated gains take effect on the motor.
            if (gainsChanged) {
                launchController.applyVelocityPIDF();
            }

            telemetry.addData("Gain adjustment per loop", "%.4f", gainChange);
            launchController.debugPrint(telemetry);
            launchController.tick();
            telemetry.update();
        }
    }
}
