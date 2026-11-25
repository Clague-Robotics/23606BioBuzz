package org.firstinspires.ftc.teamcode.moveatron2k;

import com.pedropathing.utils.Angle;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Moveatron2k Face Point")
public class Moveatron2kFaceTest extends OpMode {

    private static final double FOCUS_STEP_MM = 100.0;

    private Moveatron2k moveatron;
    private Moveatron2k.Position focus = new Moveatron2k.Position(0, 0, 0);

    @Override
    public void init() {
        moveatron = new Moveatron2k(hardwareMap);
        telemetry.addLine("Face-point drive: left stick = move (frame axes), bot auto-faces the focus point.");
        telemetry.addLine("A/B = focus ±100 mm on x, X/Y = ±100 mm on y, Start release = re-anchor + refocus here.");
        telemetry.update();
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;

        if (gamepad1.aWasPressed()) {
            focus = new Moveatron2k.Position(focus.xMm + FOCUS_STEP_MM, focus.yMm, 0);
        } else if (gamepad1.bWasPressed()) {
            focus = new Moveatron2k.Position(focus.xMm - FOCUS_STEP_MM, focus.yMm, 0);
        } else if (gamepad1.xWasPressed()) {
            focus = new Moveatron2k.Position(focus.xMm, focus.yMm + FOCUS_STEP_MM, 0);
        } else if (gamepad1.yWasPressed()) {
            focus = new Moveatron2k.Position(focus.xMm, focus.yMm - FOCUS_STEP_MM, 0);
        }
        if (gamepad1.startWasReleased()) {
            moveatron.setPosition(0.0, 0.0, 0.0);
            focus = new Moveatron2k.Position(0, 0, 0);
        }

        moveatron.manual(Moveatron2k.MoveType.FieldCentric, forward, strafe, focus, telemetry);

        Moveatron2k.Position pose = moveatron.getPosition();
        double aimErrorDeg = Math.toDegrees(Angle.normalizeSigned(
                Math.atan2(focus.yMm - pose.yMm, focus.xMm - pose.xMm)
                        - Math.toRadians(pose.headingDeg)));
        telemetry.addData("Aim error (deg, + = CCW)", "%.1f", aimErrorDeg);
        telemetry.addData("Focus (mm)", "x %.0f, y %.0f", focus.xMm, focus.yMm);
        telemetry.addData("Position", pose);
        telemetry.update();
    }

    @Override
    public void stop() {
        moveatron.cancel();
    }
}
