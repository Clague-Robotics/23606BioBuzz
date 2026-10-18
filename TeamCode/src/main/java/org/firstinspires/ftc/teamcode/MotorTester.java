package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Test MotorFL")

public class MotorTester extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor motor = null;


    private double power = -1;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        motor  = hardwareMap.get(DcMotor.class, "MotorFL");
        waitForStart();
        while (opModeIsActive()) {
            power += gamepad1.a ? 0.001 : 0;
            power -= gamepad1.b ? 0.001 : 0;
            motor.setPower(power);
            telemetry.addData("Power", power);
            telemetry.update();
        }
    }
}
