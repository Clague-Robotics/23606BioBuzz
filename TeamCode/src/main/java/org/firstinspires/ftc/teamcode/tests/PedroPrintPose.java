package org.firstinspires.ftc.teamcode.tests;


import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp(name = "Print Position Pedro Pathing")
public class PedroPrintPose extends LinearOpMode {
    public void runOpMode() {
        Follower follower = Constants.create(hardwareMap);
        follower.setPose(new Pose(0, 0, 0));
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("Follower X", follower.pose().x());
            telemetry.addData("Follower Y", follower.pose().y());
            telemetry.addData("Follower heading", follower.pose().heading());
            telemetry.update();
            follower.update();
        }
    }
}
