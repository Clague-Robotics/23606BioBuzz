package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LaunchController {
    private DcMotorEx intake = null;
    private DcMotorEx kicker = null;
    private DcMotorEx launch = null;

    private boolean isIntaking = false;
    private boolean isKicking = false;
    private boolean isLaunching = false;
    private boolean lastLaunchButtonState = false;

    private double aimTPS = 0;

    public static class LaunchConfig {
        // Motor & Target Specs (goBILDA 5203 6000 RPM)
        public double ticksPerRevolution = 28.0;
        public double targetLaunchRPM = 2_500.0;

        // PIDF Gains for Velocity Control
        public double kP = 13.0;
        public double kI = 0.5;
        public double kD = 0.0;
        public double kF = 12;

        // Motor Power Settings
        public float kickingSpeedOn = 1.0f;
        public float intakingSpeedOn = 1.0f;

        public float kickingSpeedOff = 0.0f;
        public float intakingSpeedOff = 0.0f;

        // Motor Directions
        public DcMotorSimple.Direction launchingDirection = DcMotorSimple.Direction.FORWARD;
        public DcMotorSimple.Direction kickingDirection = DcMotorSimple.Direction.REVERSE;
        public DcMotorSimple.Direction intakingDirection = DcMotorSimple.Direction.FORWARD;

        // Hardware Map Names
        public String launchMotorName = "MotorLauncher";
        public String kickerMotorName = "MotorKicker";
        public String intakeMotorName = "MotorIntake";

        public LaunchConfig() {}
    }

    LaunchConfig launchConfig;

    public LaunchController(HardwareMap hardwareMap, LaunchConfig launchConfig) {
        this.launchConfig = launchConfig;

        // Convert RPM to Ticks Per Second (TPS)
        this.aimTPS = (launchConfig.targetLaunchRPM * launchConfig.ticksPerRevolution) / 60.0;

        // Initialize hardware from HardwareMap
        this.launch = hardwareMap.get(DcMotorEx.class, launchConfig.launchMotorName);
        this.kicker = hardwareMap.get(DcMotorEx.class, launchConfig.kickerMotorName);
        this.intake = hardwareMap.get(DcMotorEx.class, launchConfig.intakeMotorName);

        // Apply motor directions
        this.launch.setDirection(launchConfig.launchingDirection);
        this.kicker.setDirection(launchConfig.kickingDirection);
        this.intake.setDirection(launchConfig.intakingDirection);

        // Set zero power behavior (FLOAT lets flywheel coast smoothly without braking wear)
        this.launch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.kicker.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Reset encoder and enable built-in velocity PIDF control
        this.launch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.launch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Apply custom PIDF coefficients
        this.launch.setVelocityPIDFCoefficients(
                launchConfig.kP,
                launchConfig.kI,
                launchConfig.kD,
                launchConfig.kF
        );
    }

    public LaunchController(HardwareMap hardwareMap) {
        this(hardwareMap, new LaunchConfig());
    }

    public void updateValues(boolean isIntaking, boolean isKicking, boolean isLaunching) {
        this.isIntaking = isIntaking;
        this.isKicking = isKicking;
        this.isLaunching = isLaunching;
    }

    public void updateInput(boolean intakeHeld, boolean kickingHeld, boolean launchToggleButton, boolean addToSpeed, boolean removeToSpeed) {
        // Only toggle isLaunching on the exact frame the button is pressed down
        if (launchToggleButton && !lastLaunchButtonState) {
            isLaunching = !isLaunching;
        }
        lastLaunchButtonState = launchToggleButton;

        this.isIntaking = intakeHeld;
        this.isKicking = kickingHeld;

        double add = 0;
        if (addToSpeed) add += launchConfig.ticksPerRevolution * 5;
        if (removeToSpeed) add -= launchConfig.ticksPerRevolution * 5;

        this.aimTPS += add;
    }

    public boolean isAtTargetSpeed(double toleranceTPS) {
        if (launch == null || !isLaunching) return false;
        return Math.abs(launch.getVelocity() - aimTPS) <= toleranceTPS;
    }

    public void debugPrint(Telemetry telemetry) {
        double currentTPS = launch.getVelocity();
        double currentRPM = (currentTPS * 60.0) / launchConfig.ticksPerRevolution;

        telemetry.addData("Is Launching", isLaunching);
        telemetry.addData("Is Kicking", isKicking);
        telemetry.addData("Is Intaking", isIntaking);

        telemetry.addData("Launch Current RPM", "%.0f / %.0f", currentRPM, launchConfig.targetLaunchRPM);
        telemetry.addData("Launch Current TPS", "%.1f / %.1f", currentTPS, aimTPS);

        telemetry.addData("Kicker Motor Power", kicker.getPower());
        telemetry.addData("Intake Motor Power", intake.getPower());
    }

    public void tick() {
        if (intake == null || kicker == null || launch == null) return;

        // Set Intake power
        float intakePower = isIntaking ? launchConfig.intakingSpeedOn : launchConfig.intakingSpeedOff;
        intake.setPower(intakePower);

        // Set Kicker power
        float kickerPower = isKicking ? launchConfig.kickingSpeedOn : launchConfig.kickingSpeedOff;
        kicker.setPower(kickerPower);

        // Set Launch velocity using internal PIDF
        if (isLaunching) {
            launch.setVelocity(aimTPS);
        } else {
            launch.setVelocity(0);
        }
    }
}
