package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class BeforeHiveAuton extends LinearOpMode {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(86.75, 133.75, 270);
    private final Pose path1 = poseFactory.of(131.75, 45.75, 180);
    private final Pose path1Control1 = poseFactory.of(110.75, 127.75, 0);

    public Path path1() {
        return Paths.curve(start, path1Control1, path1).linear(start, path1);
    }

    public void runOpMode() {
        Follower follower = Constants.create(hardwareMap);
        LaunchController controller = new LaunchController(hardwareMap);

        waitForStart();

        // Warm up launch motors
        controller.updateValues(false, false, true);
        controller.tick();
        sleep(5000);

        // Launch
        controller.updateValues(true, true, true);
        controller.tick();
        sleep(5000);
    }
}
