package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LaunchController {
    private DcMotorEx intake;
    private DcMotorEx kicker;
    private DcMotorEx launch;

    private boolean isIntaking = false;
    private boolean isKicking = false;
    private boolean intakeReversing = false;
    private boolean kickerReversing = false;
    private boolean isLaunching = false;
    private boolean lastLaunchButtonState = false;
    private boolean smartLaunch = false;

    private double aimTPS = 0;
    final LaunchConfig launchConfig;

    public static class LaunchConfig {
        public int changeRpmValue = 50;
        // Motor and target specs (goBILDA 5203 6000 RPM)
        public double ticksPerRevolution = 28.0;
        public double targetLaunchRPM = 2_400.0;

        // PIDF gains for velocity control
        public double kP = 13.0;
        public double kI = 0.5;
        public double kD = 0.0;
        public double kF = 12.0;

        // Motor power settings
        public float kickingSpeedOn = 1.0f;
        public float intakingSpeedOn = 0.5f;

        public float kickingSpeedOff = 0.0f;
        public float intakingSpeedOff = 0.0f;
        public float intakeSpeedReverse = -1.0f;
        public float kickingSpeedReverse = -1.0f;

        // Motor directions
        public DcMotorSimple.Direction launchingDirection =
                DcMotorSimple.Direction.FORWARD;
        public DcMotorSimple.Direction kickingDirection =
                DcMotorSimple.Direction.REVERSE;
        public DcMotorSimple.Direction intakingDirection =
                DcMotorSimple.Direction.FORWARD;

        // Hardware map names
        public String launchMotorName = "MotorLauncher";
        public String kickerMotorName = "MotorKicker";
        public String intakeMotorName = "MotorIntake";
    }

    public LaunchController(HardwareMap hardwareMap, LaunchConfig launchConfig) {
        this.launchConfig = launchConfig;
        this.aimTPS = rpmToTicksPerSecond(launchConfig.targetLaunchRPM);

        launch = hardwareMap.get(DcMotorEx.class, launchConfig.launchMotorName);
        kicker = hardwareMap.get(DcMotorEx.class, launchConfig.kickerMotorName);
        intake = hardwareMap.get(DcMotorEx.class, launchConfig.intakeMotorName);

        launch.setDirection(launchConfig.launchingDirection);
        kicker.setDirection(launchConfig.kickingDirection);
        intake.setDirection(launchConfig.intakingDirection);

        launch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        kicker.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        launch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        applyVelocityPIDF();
    }

    public LaunchController(HardwareMap hardwareMap) {
        this(hardwareMap, new LaunchConfig());
    }

    private double rpmToTicksPerSecond(double rpm) {
        return rpm * launchConfig.ticksPerRevolution / 60.0;
    }

    private double ticksPerSecondToRPM(double ticksPerSecond) {
        return ticksPerSecond * 60.0 / launchConfig.ticksPerRevolution;
    }

    /** Applies the current PIDF values to the launcher motor. */
    public void applyVelocityPIDF() {
        launch.setVelocityPIDFCoefficients(
                launchConfig.kP,
                launchConfig.kI,
                launchConfig.kD,
                launchConfig.kF
        );
    }

    public void updateValues(
            boolean isIntaking,
            boolean isKicking,
            boolean isLaunching
    ) {
        this.isIntaking = isIntaking;
        this.isKicking = isKicking;
        this.isLaunching = isLaunching;
    }

    public void updateInput(
            boolean intakeHeld,
            boolean kickingHeld,
            boolean launchToggleButton,
            boolean intakeReverse,
            boolean kickingReverse,
            boolean addToSpeed,
            boolean removeToSpeed,
            boolean smartLaunch
    ) {
        // Toggle launcher only on the button's press edge.
        if (launchToggleButton && !lastLaunchButtonState) {
            isLaunching = !isLaunching;
        }
        lastLaunchButtonState = launchToggleButton;

        isIntaking = intakeHeld;
        isKicking = kickingHeld;
        intakeReversing = intakeReverse;
        kickerReversing = kickingReverse;

        double speedChangeTPS = rpmToTicksPerSecond(launchConfig.changeRpmValue);
        if (addToSpeed) {
            aimTPS += speedChangeTPS;
        }
        if (removeToSpeed) {
            aimTPS -= speedChangeTPS;
        }

        this.smartLaunch = smartLaunch;
    }

    public boolean isAtTargetSpeed(double toleranceTPS) {
        return launch != null
                && isLaunching
                && Math.abs(launch.getVelocity() - aimTPS) <= toleranceTPS;
    }

    public void debugPrint(Telemetry telemetry) {
        double currentTPS = launch.getVelocity();
        double currentRPM = ticksPerSecondToRPM(currentTPS);
        double targetRPM = ticksPerSecondToRPM(aimTPS);

        telemetry.addData("Is Launching", isLaunching);
        telemetry.addData("Is Kicking", isKicking);
        telemetry.addData("Is Intaking", isIntaking);
        telemetry.addData("Launch RPM (current / target)",
                "%.0f / %.0f", currentRPM, targetRPM);

        telemetry.addData("Kicker Motor Power", kicker.getPower());
        telemetry.addData("Intake Motor Power", intake.getPower());

        telemetry.addData("kP", launchConfig.kP);
        telemetry.addData("kI", launchConfig.kI);
        telemetry.addData("kD", launchConfig.kD);
        telemetry.addData("kF", launchConfig.kF);
    }

    public void tick() {
        if (intake == null || kicker == null || launch == null) {
            return;
        }

        float intakePower = isIntaking
                ? launchConfig.intakingSpeedOn
                : launchConfig.intakingSpeedOff;
        if (intakeReversing) {
            intakePower = launchConfig.intakeSpeedReverse;
        }
        intake.setPower(intakePower);

        if (smartLaunch) {
            if (isAtTargetSpeed(50)) {
                kicker.setPower(launchConfig.kickingSpeedOn);
            } else {
                kicker.setPower(launchConfig.kickingSpeedOff);
            }
        } else {
            float kickerPower = isKicking
                    ? launchConfig.kickingSpeedOn
                    : launchConfig.kickingSpeedOff;
            if (kickerReversing) {
                kickerPower = launchConfig.kickingSpeedReverse;
            }
            kicker.setPower(kickerPower);
        }

        if (isLaunching) {
            launch.setVelocity(aimTPS);
        } else {
            launch.setVelocity(0);
        }
    }
}
