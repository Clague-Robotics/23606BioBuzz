package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
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
        follower.update();
    }
}
