package org.firstinspires.ftc.teamcode.moveatron2k;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Manual bot-centric teleop on the Moveatron2k mecanum driver.
 *
 * The left stick is the move direction relative to the BOT — stick up always
 * drives out the nose, stick right strafes right, no matter how the bot is
 * rotated. ({@link FieldCentricDrive} is the world-frame twin of this opmode.)
 * Right stick turns (+: CCW from above).
 *
 * {@link Moveatron2k#manual} passes the axes straight through as body-frame
 * powers; it would no-op while busy, but this opmode never sets a goTo target,
 * so {@link Moveatron2k#isBusy} stays false.
 */
@TeleOp(name = "Moveatron2k Bot-Centric")
public class BotCentricDrive extends OpMode {

    private Moveatron2k moveatron;

    @Override
    public void init() {
        moveatron = new Moveatron2k(hardwareMap);
        telemetry.addLine("Bot-centric: left stick = move relative to bot nose, right stick = turn.");
        telemetry.update();
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        moveatron.manual(Moveatron2k.MoveType.BotCentric, forward, strafe, turn);

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
