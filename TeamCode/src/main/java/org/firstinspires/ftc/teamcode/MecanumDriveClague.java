/*package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Robot;

@TeleOp(name = "2026-2027 TeleOp Pee Pee Poo Poo")
public class MecanumDriveClague extends LinearOpMode {

    public volatile static double TURN_SPRINT_SPEED = 0.8;
    public volatile static double TURN_SLOW_SPEED = 0.5;
    public volatile static double MOVE_SPRINT_SPEED = 1;
    public volatile static double MOVE_SLOW_SPEED = 0.6;
    public volatile static double INTAKE_MOTOR_SPEED = -+1;
    public volatile static double THROW_MOTOR_SPEED_IDLE = 0.5;
    public volatile static double THROW_MOTOR_SPEED_LAUNCH = 0.8;
    public volatile static long THROW_MOTOR_WARMUP_TIME_MS = 200;
    public volatile static double MIDSTATE_MOTOR_IDLE = 0;
    public volatile static double MIDSTATE_MOTOR_LAUNCH = 1;
    public volatile static double SLIDE_MODIFIER_TURN_SIDE = 0.5;
    public volatile static double SLIDE_MODIFIER_OTHER_SIDE = -0.2;
    public volatile static double CLEAR_MOTOR_SPEED = -1;

    private Robot robot;


    private boolean isThrowing;
    private long throwDate;

    @Override
    public void runOpMode() {
        robot = new Robot(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            tick();
            telemetry.update();
        }
    }

    private void tick() {
        // Input
        boolean spitInput = gamepad2.x | gamepad2.y;
        boolean sprintInput = gamepad1.a;
        double inputX = gamepad1.left_stick_x * -1;
        double inputY = gamepad1.left_stick_y;
        double rot = gamepad1.right_stick_x * -1;
        boolean slide = gamepad1.left_bumper || gamepad1.right_bumper;
        boolean breaking = gamepad1.b;
        boolean throwInput = gamepad2.left_trigger > 0.5;
        double intakeInput = gamepad2.right_trigger * INTAKE_MOTOR_SPEED;



        // Moving
        double moveSpeed = sprintInput ? MOVE_SPRINT_SPEED : MOVE_SLOW_SPEED;
        double turnSpeed = sprintInput ? TURN_SPRINT_SPEED : TURN_SLOW_SPEED;

        // Allow the bot to 'slide' a little, helping it go around turns
        if (inputX == 0 && slide) {
            slide = false;
        }
        if (slide) {
            gamepad1.rumble(50);
        }
        double slideModifierL = inputX > 0 && slide ? SLIDE_MODIFIER_TURN_SIDE : SLIDE_MODIFIER_OTHER_SIDE;
        double slideModifierR = inputX < 0 && slide ? SLIDE_MODIFIER_OTHER_SIDE : SLIDE_MODIFIER_TURN_SIDE;

        if (!slide) {
            slideModifierL = 0;
            slideModifierR = 0;
        }

        // Apply movement
        double powerFL = ((inputY + inputX) * moveSpeed) + (rot * turnSpeed) + slideModifierL;
        double powerBL = ((inputY - inputX) * moveSpeed) + (rot * turnSpeed) + slideModifierL;
        double powerBR = ((inputY - inputX) * moveSpeed) - (rot * turnSpeed) + slideModifierR;
        double powerFR = ((inputY + inputX) * moveSpeed) - (rot * turnSpeed) + slideModifierR;

        robot.motorFR.setPower(powerFR);
        robot.motorFL.setPower(powerFL);
        robot.motorBR.setPower(powerBR);
        robot.motorBL.setPower(powerBL);

        // Breaking
        robot.motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.motorThrow.setPower(throwInput ? THROW_MOTOR_SPEED_LAUNCH : THROW_MOTOR_SPEED_IDLE);

        // Intake
        robot.motorIn1.setPower(intakeInput);

        // Midway
        robot.motorIn2.setPower(intakeInput < -0.5 ? MIDSTATE_MOTOR_LAUNCH : MIDSTATE_MOTOR_IDLE);

        // Spit out
        if (spitInput) {
            robot.motorIn1.setPower(CLEAR_MOTOR_SPEED);
            robot.motorIn2.setPower(CLEAR_MOTOR_SPEED);
            robot.motorThrow.setPower(CLEAR_MOTOR_SPEED);
        }
        // telemetry
        telemetry.addData("Motor BR Power", robot.motorBR.getPower());
        telemetry.addData("Motor BL Power", robot.motorBL.getPower());
        telemetry.addData("Motor FL Power", robot.motorFL.getPower());
        telemetry.addData("Motor FR Power", robot.motorFR.getPower());
        telemetry.addData("Slide Left", slideModifierL);
        telemetry.addData("Slide Right", slideModifierR);
    }
}*/
