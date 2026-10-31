package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

@TeleOp(name = "Motor Launch Test")
public class LaunchTest extends LinearOpMode {
    public void runOpMode() {
        DcMotor launcher = hardwareMap.get(DcMotor.class, "MotorLauncher");
        DcMotor kicker = hardwareMap.get(DcMotor.class, "MotorKicker");
        DcMotor intake = hardwareMap.get(DcMotor.class, "MotorIntake");
        boolean isLaunching = false;
        boolean isKicking = false;
        boolean isIntaking = false;

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.rightBumperWasReleased()) isLaunching = !isLaunching;
            isKicking = gamepad1.left_bumper;
            isIntaking = gamepad1.left_trigger > 0.5;

            kicker.setPower(isKicking ? -1 : 0.3);
            intake.setPower(isIntaking ? 1 : 0);
            launcher.setPower(isLaunching ? -0.6 : 0);

            telemetry.addData("Is Launching", isLaunching);
            telemetry.addData("Is Kicking", isKicking);
            telemetry.addData("Is Intaking", isIntaking);
            telemetry.update();
        }
    }
}
