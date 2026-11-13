package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

import org.firstinspires.ftc.teamcode.*;
@TeleOp(name = "Motor Launch Test")
public class LaunchTest extends LinearOpMode {
    public void runOpMode() {
        LaunchController controller = new LaunchController(hardwareMap); // ./gradlew doesn't fail on this, ide display error.
        /*DcMotor launcher = hardwareMap.get(DcMotor.class, "MotorLauncher");
        DcMotor kicker = hardwareMap.get(DcMotor.class, "MotorKicker");
        DcMotor intake = hardwareMap.get(DcMotor.class, "MotorIntake");
        boolean isLaunching = false;
        boolean isKicking = false;
        boolean isIntaking = false;*/

        waitForStart();
        while (opModeIsActive()) {
            /*if (gamepad1.xWasReleased()) isLaunching = !isLaunching;
            isKicking = gamepad1.right_trigger > 0.5;
            isIntaking = gamepad1.left_trigger > 0.5;

            kicker.setPower(isKicking ? -1 : 0.3);
            intake.setPower(isIntaking ? 1 : 0);
            launcher.setPower(isLaunching ? 0.6 : 0);

            telemetry.addData("Is Launching", isLaunching);
            telemetry.addData("Is Kicking", isKicking);
            telemetry.addData("Is Intaking", isIntaking);

            telemetry.addData("Motor Info","");
            telemetry.addData("Intake Motor Speed", intake.getPower()); */

            controller.updateInput(gamepad1.right_trigger > 0.5, gamepad1.left_trigger > 0.5, gamepad1.xWasReleased(), gamepad1.dpadUpWasReleased(), gamepad1.dpadDownWasPressed());
            controller.debugPrint(telemetry);
            controller.tick();
            telemetry.update();
        }
    }
}
