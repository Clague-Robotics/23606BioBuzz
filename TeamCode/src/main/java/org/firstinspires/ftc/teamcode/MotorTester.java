package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/** Spins MotorFL; hold A to increase power and B to decrease it. */
@TeleOp(name = "Test MotorFL")
public class MotorTester extends LinearOpMode {

    private double power = -1;

    @Override
    public void runOpMode() {
        DcMotor motor = hardwareMap.get(DcMotor.class, "MotorFL");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            power += gamepad1.a ? 0.001 : 0;
            power -= gamepad1.b ? 0.001 : 0;
            power = Range.clip(power, -1, 1);
            motor.setPower(power);
            telemetry.addData("Power", power);
            telemetry.update();
        }
    }
}
