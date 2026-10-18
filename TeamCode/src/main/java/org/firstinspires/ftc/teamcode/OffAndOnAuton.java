package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "2026-2027 Auton On and Off")
public class OffAndOnAuton extends LinearOpMode {

    public volatile static double FORWARD_STR = 1;
    public volatile static double BACKWARD_STR = -1;
    public volatile static int FORWARD_TIME_MS = 500;
    public volatile static int BACKWARD_TIME_MS = 750;
    public volatile static boolean BRAKE = true;

    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        // Set before moving so the final stop actually brakes instead of coasting.
        robot.setDriveZeroPowerBehavior(BRAKE ? DcMotor.ZeroPowerBehavior.BRAKE : DcMotor.ZeroPowerBehavior.FLOAT);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        robot.setDrivePower(FORWARD_STR);
        sleep(FORWARD_TIME_MS);

        robot.setDrivePower(BACKWARD_STR);
        sleep(BACKWARD_TIME_MS);

        robot.setDrivePower(0);
    }
}
