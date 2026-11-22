package org.firstinspires.ftc.teamcode.moveatron2k;

import com.pedropathing.drivetrain.DrivePowers;

/**
 * Basic bot-centric manual drive: the requested powers are already in the bot's
 * body frame — +forward = out the nose, +strafe = left, +turn = CCW from above —
 * so there is nothing to rotate; they pass through to the mecanum drivetrain
 * as-is. Exists as the {@link Moveatron2k.MoveType#BotCentric} half of
 * {@code manual(MoveType, DrivePowers)}'s dispatch so both call sites mirror
 * each other.
 */
public final class BotCentricDrive {

    private BotCentricDrive() {}

    /**
     * @return the powers unchanged, already in the body frame
     */
    public static DrivePowers drive(double forward, double strafe, double turn) {
        return new DrivePowers(forward, strafe, turn);
    }
}
