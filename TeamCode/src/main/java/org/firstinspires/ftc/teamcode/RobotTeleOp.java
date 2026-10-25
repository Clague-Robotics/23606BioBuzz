package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.ProgrammingBotConstants;

// Ascii Field Depiction For refrence
/////////////////////////////////////////////////////////
////                                                 ////
////                                                 ////
////                                                 ////
////              |-----------------|                ////
////              |-----------------|                ////
////              |     Red Hive    |                ////
////              |-----------------|                ////
////   North                              South      ////
////              |-----------------|                ////
////              |     Blue Hive   |                ////
////              |-----------------|                ////
////                                                 ////
////  (Right in the corner)                          ////
////  (o, o)                                         ////
////XX                                               ////
/////////////////////////////////////////////////////////
enum DriveType {
    FIELD_ORIENTED,
    BOT_ORIENTED
}

enum FieldLocation {
    ZERO_ZERO,
    BLUE_HIVE_NORTH,
    BLUE_HIVE_SOUTH,
    RED_HIVE_NORTH,
    RED_HIVE_SOUTH
}
@TeleOp(name = "23606 BioBuzz TeleOp")
public class RobotTeleOp extends OpMode {
    private Follower follower;
    private DriveType driveType;
    private volatile Pose startingPos = new Pose(0, 0);

    void goTo(Pose pose) {
        try {
            Path path = line(
                    follower.pose(),
                    pose
            ).tangent();

            follower.follow(path);
        } catch (Exception ignored) { }
    }
    void goTo(FieldLocation location) {
        switch (location) {
            case ZERO_ZERO:
                goTo(new Pose(0, 0));
                break;
            case BLUE_HIVE_NORTH:
                goTo(new Pose(50, -5));
                break;
            case RED_HIVE_NORTH:
                goTo(new Pose(70, -5));
                break;
            case BLUE_HIVE_SOUTH:
                goTo(new Pose(50, -115));
                break;
            case RED_HIVE_SOUTH:
                new Pose(70, -115);
                break;
            default:
                telemetry.addData("Error", "Don't know that location");
                break;
        }
    }
    @Override
    public void init() {
        follower = ProgrammingBotConstants.create(hardwareMap);
        driveType = DriveType.FIELD_ORIENTED;
        follower.setPose(startingPos);
    }

    @Override
    public void loop() {
        // A try catch statement so that any error made won't end the OpMode.
        try {
            // Switch bot movement mode
            if (gamepad1.optionsWasReleased()) {
                if (follower.isBusy()) {
                    follower.stop();
                } else {
                    switch (driveType) {
                        case FIELD_ORIENTED:
                            driveType = DriveType.BOT_ORIENTED;
                            break;
                        case BOT_ORIENTED:
                            driveType = DriveType.FIELD_ORIENTED;
                            break;
                    }
                    gamepad1.rumble(200);
                }
            }

            // Basic logic to go autonomously back to a given position.
            if (gamepad1.share) {
                goTo(FieldLocation.ZERO_ZERO);
            }
            if (gamepad1.touchpad_finger_1 && gamepad1.touchpad) {
                double fingerX = gamepad1.touchpad_finger_1_x;
                double fingerY = gamepad1.touchpad_finger_1_y;
                telemetry.addData("Finger 1 X", fingerX);
                telemetry.addData("Finger 1 Y", fingerY);
                if (fingerX > 0) { // BLUE
                    if (fingerY > 0) { // NORTH
                        goTo(FieldLocation.BLUE_HIVE_NORTH);
                    } else { // SOUTH
                        goTo(FieldLocation.BLUE_HIVE_SOUTH);
                    }
                } else { // RED
                    if (fingerY > 0) { // NORTH
                        goTo(FieldLocation.RED_HIVE_NORTH);
                    } else { // SOUTH
                        goTo(FieldLocation.RED_HIVE_SOUTH);
                    }
                }
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

                switch (driveType) {
                    case FIELD_ORIENTED:
                        DrivePowers powers = ManualDrive.fieldCentric(
                                forward,
                                lateral,
                                turn,
                                follower.pose().heading()
                        );
                        follower.manual(powers);
                        telemetry.addData("Drive Mode", "Field-Centric Drive");
                        break;
                    case BOT_ORIENTED:
                        follower.manual(forward, lateral, turn);
                        telemetry.addData("Drive Mode", "Bot-Centric Drive");
                        break;
                    default:
                        telemetry.addData("Drive Mode", "Unknown");
                        break;
                }
            } else {
                telemetry.addData("Drive Mode", "Autonomous");
            }
            telemetry.addData("Bot Position X", follower.pose().x());
            telemetry.addData("Bot Position Y", follower.pose().y());
            follower.update();
        } catch (Exception ex) {
            telemetry.addData("Error", ex.getMessage());
            telemetry.addData("Error Info", "Realistically this doesn't matter if it shows up and your bot keeps running.");
        }
    }
}
