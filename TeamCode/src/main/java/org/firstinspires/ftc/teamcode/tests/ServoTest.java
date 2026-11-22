package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Test")
public class ServoTest extends LinearOpMode {
    public static volatile double LOW = 0.25;
    public static volatile int HIGH = 1;
    public void runOpMode() {
        Servo servo = hardwareMap.get(Servo.class, "ServoFlower");
        servo.scaleRange(LOW, HIGH);
        waitForStart();

        double position = HIGH;
        servo.setPosition(position);
        while (opModeIsActive()) {
            position = servo.getPosition();
            telemetry.addData("Servo Position", servo.getPosition());
            if (gamepad1.dpad_up) position = HIGH;
            if (gamepad1.dpad_down) position = LOW;
            servo.setPosition(position);
            telemetry.update();
        }
    }
}
