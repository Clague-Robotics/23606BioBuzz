package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.ProgrammingBotConstants;

import static com.pedropathing.api.Paths.curve;

@Autonomous(name = "Basic Pedro Pathing Test")
public class CurveTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        // 1. SET UP: build the Follower (the part of Pedro that drives the robot)
        Follower follower = ProgrammingBotConstants.create(hardwareMap);

        // 2. Tell Pedro where the robot starts (inches). Pose = x, y.
        Pose start = new Pose(0, 0);
        follower.setPose(start);

        // 3. Make a curve: start point, one "pull" point, end point.
        //    The curve bends toward the middle point but doesn't touch it.
        Path curvePath = curve(
                start,
                new Pose(48, 0),    // control point (pulls the curve)
                new Pose(48, 48)    // end point
        ).tangent();                // robot faces the way it is travelling

        waitForStart();

        // 4. Start following the path
        follower.follow(curvePath);

        // 5. Pedro only moves if you call update() over and over
        while (opModeIsActive() && !follower.atParametricEnd()) {
            follower.update();
            telemetry.addData("Pose", follower.pose());
            telemetry.update();
        }
    }
}