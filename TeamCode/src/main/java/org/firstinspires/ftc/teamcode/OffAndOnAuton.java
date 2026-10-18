package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Robot;


@Autonomous(name = "2026-2027 Auton On and Off")
public class OffAndOnAuton extends LinearOpMode {

    public volatile static double FORWARD_STR = 1;
    public volatile static double BACKWARD_STR = -1;
    public volatile static int FORWARD_TIME_MS = 500;
    public volatile static int BACKWARD_TIME_MS = 750;
    public volatile static boolean BREAK = true;

    private Robot robot;


    private boolean isThrowing;
    private long throwDate;

    @Override
    public void runOpMode() {
        robot = new Robot(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        robot.motorBL.setPower(FORWARD_STR);
        robot.motorBR.setPower(FORWARD_STR);
        robot.motorFL.setPower(FORWARD_STR);
        robot.motorFR.setPower(FORWARD_STR);

        sleep(FORWARD_TIME_MS);

        robot.motorBL.setPower(BACKWARD_STR);
        robot.motorBR.setPower(BACKWARD_STR);
        robot.motorFL.setPower(BACKWARD_STR);
        robot.motorFR.setPower(BACKWARD_STR);

        sleep(BACKWARD_TIME_MS);

        robot.motorBL.setPower(0);
        robot.motorBR.setPower(0);
        robot.motorFL.setPower(0);
        robot.motorFR.setPower(0);
        if (BREAK) {
            robot.motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }
}
