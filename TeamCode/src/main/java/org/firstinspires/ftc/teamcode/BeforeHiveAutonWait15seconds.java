package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "Before Hive Auton (15 second pre-wait)", preselectTeleOp = "23606 BioBuzz TeleOp")
public class BeforeHiveAutonWait15seconds extends LinearOpMode {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(86.75, 133.75, 270);
    private final Pose path1 = poseFactory.of(-31.75, 45.75, 180);
    private final Pose path1Control1 = poseFactory.of(110.75, 127.75, 0);

    public Path path1() {
        return Paths.curve(start, path1Control1, path1).linear(start, path1);
    }

    public void runOpMode() {
        Follower follower = Constants.create(hardwareMap);
        follower.setPose(start);
        LaunchController.LaunchConfig config = new LaunchController.LaunchConfig();
        config.targetLaunchRPM = 2_400;
        LaunchController controller = new LaunchController(hardwareMap, config);

        waitForStart();

        sleep(15000);

        // Warm up launch motors
        controller.updateValues(false, false, true);
        controller.tick();

        telemetry.addData("Status", "Warming Up Launch Motors");
        telemetry.update();

        sleep(5000);

        // Launch
        controller.updateValues(true, true, true);
        controller.tick();

        telemetry.addData("Status", "Launching... ");
        telemetry.update();

        sleep(5000);


        telemetry.addData("Status", "Finished!");
        telemetry.update();
        sleep(200);
    }
}
