package org.firstinspires.ftc.teamcode.moveatron2k;

import com.pedropathing.drivetrain.DrivePowers;

/**
 * Basic field-centric manual drive: the requested motion axes are in the
 * {@link Moveatron2k} world frame — +x ("forward") = the direction the bot
 * faced at init (or wherever {@link Moveatron2k#setPosition} redefined the
 * frame's x-axis), +y ("strafe") = 90° CCW off that, +turn = CCW — and get
 * rotated into the bot's body frame against its current heading before the
 * mecanum mixer can use them.
 *
 * Same rotation as Moveatron2k's world→body error mapping in reverse:
 * body = R(-heading) · world, strafe positive left.
 */
public final class FieldCentricDrive {

    private FieldCentricDrive() {}

    /**
     * @param forwardPow world-frame x-axis power (+ = the frame's 0° direction)
     * @param strafePow  world-frame left power
     * @param turn       turn power, frame-independent (+ = CCW), passed through
     * @param headingRad the bot's current heading in the Moveatron2k frame
     *                   (CCW-positive, as {@link Moveatron2k.Position#headingDeg})
     * @return body-frame powers for the mecanum drivetrain
     */
    public static DrivePowers drive(double forward, double strafe, double turn, double headingRad) {
        double cos = Math.cos(headingRad);
        double sin = Math.sin(headingRad);
        double bodyForward = forward * cos + strafe * sin;
        double bodyStrafe = -forward * sin + strafe * cos;
        return new DrivePowers(bodyForward, bodyStrafe, turn);
    }
}
