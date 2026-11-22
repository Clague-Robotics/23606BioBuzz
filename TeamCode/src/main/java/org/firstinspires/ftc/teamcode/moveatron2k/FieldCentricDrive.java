package org.firstinspires.ftc.teamcode.moveatron2k;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Manual field-centric teleop on the Moveatron2k mecanum driver.
 *
 * The left stick is the move direction in the Moveatron2k frame — straight up
 * ALWAYS drives toward the direction the bot faced at OpMode init (or wherever
 * {@link Moveatron2k#setPosition} redefined the frame), regardless of the bot's
 * current heading. {@link Moveatron2k#manual} reads the Pinpoint and rotates the
 * axes into the body frame every loop. Right stick turns (+: CCW from above).
 *
 * Stick mapping (+strafe = left, +turn = CCW from above, matching
 * {@link Moveatron2k#manual}): left stick up = forward, left stick right =
 * strafe right (i.e. -left), right stick right = turn clockwise.
 */
@TeleOp(name = "Moveatron2k Field-Centric")
public class FieldCentricDrive extends OpMode {

    private Moveatron2k moveatron;

    @Override
    public void init() {
        moveatron = new Moveatron2k(hardwareMap);
        telemetry.addLine("Field-centric: left stick = move toward frame axes, right stick = turn.");
        telemetry.update();
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        moveatron.manual(Moveatron2k.MoveType.FieldCentric, forward, strafe, turn);

        Moveatron2k.Position pose = moveatron.getPosition();
        telemetry.addData("Position", pose);
        telemetry.addData("Busy", moveatron.isBusy());
        telemetry.update();
    }

    @Override
    public void stop() {
        moveatron.cancel();
    }
}
