package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.ProgrammingBotConstants;

@TeleOp(name = "23606 BioBuzz TeleOp")
public class RobotTeleOp extends OpMode {
    private Follower follower;
    @Override
    public void init() {
        follower = ProgrammingBotConstants.create(hardwareMap);
    }

    @Override
    public void loop() {
        // Basic logic to go autonomously back to starting position.
        Path curvePath = line(
                follower.pose(),
                new Pose(0, 0)
        ).tangent();

        if (gamepad1.right_bumper) {
            follower.follow(curvePath);
        }


        if (!follower.isBusy()) { // Basic manual controls
            double forward = -gamepad1.left_stick_y;
            double lateral = -gamepad1.left_stick_x;
            double turn = -gamepad1.right_stick_x;
            boolean slow = gamepad1.b;
            if (slow) {
                forward *= 0.5;
                lateral *= 0.5;
                turn *= 0.5;
            }
             follower.manual(forward, lateral, turn);
        }

        telemetry.addData("Bot Position X", follower.pose().x());
        telemetry.addData("Bot Position Y", follower.pose().y());
        follower.update();
    }
}
