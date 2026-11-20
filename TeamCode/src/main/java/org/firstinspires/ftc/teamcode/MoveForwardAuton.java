package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "Move Forward Auton", preselectTeleOp = "23606 BioBuzz TeleOp")
public class MoveForwardAuton extends LinearOpMode {


    public void runOpMode() {
        Follower follower = Constants.create(hardwareMap);
        waitForStart();

        while (true) {
            follower.manual(0.2, 0, 0);
            follower.update();
        }
    }
}
