package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

@TeleOp(name = "Motor Test")
public class TestMotorList extends LinearOpMode {
    public void runOpMode() {
        ArrayList<DcMotor> motors = new ArrayList<DcMotor>();
        ArrayList<Double> powers = new ArrayList<Double>();
        motors.add(hardwareMap.get(DcMotor.class, "MotorLauncher"));
        powers.add(1.0);
        motors.add(hardwareMap.get(DcMotor.class, "MotorKicker"));
        powers.add(1.0);
        motors.add(hardwareMap.get(DcMotor.class, "MotorIntake"));
        powers.add(1.0);
        int activeElement = 0;

        waitForStart();
        while (opModeIsActive()) {
            motors.get(activeElement).setPower(powers.get(activeElement));
            if (gamepad1.aWasReleased()) {
                activeElement++;
                activeElement = activeElement % motors.size();
            }
            if (gamepad1.dpadUpWasReleased()) {
                powers.set(activeElement, powers.get(activeElement) + 0.1);
            }
            if (gamepad1.dpadDownWasReleased()) {
                powers.set(activeElement, powers.get(activeElement) - 0.1);
            }
            if (gamepad1.dpadRightWasReleased() || gamepad1.dpadLeftWasReleased()) {
                powers.set(activeElement, powers.get(activeElement) * -1);
            }

            telemetry.addData("Motors", motors);
            telemetry.addData("Powers", powers);
            telemetry.update();
        }
    }
}
