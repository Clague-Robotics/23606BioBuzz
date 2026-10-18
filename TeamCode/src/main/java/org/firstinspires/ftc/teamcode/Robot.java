package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Robot {
    public DcMotor motorFL;
    public DcMotor motorBL;
    public DcMotor motorFR;
    public DcMotor motorBR;
    public Robot(HardwareMap hwMap) {
        // Initialize the motor using the hardware map
        motorBR = hwMap.get(DcMotor.class, "MotorBR");
        motorBL = hwMap.get(DcMotor.class, "MotorBL");
        motorFR = hwMap.get(DcMotor.class, "MotorFR");
        motorFL = hwMap.get(DcMotor.class, "MotorFL");


        motorBR.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFR.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBL.setDirection(DcMotorSimple.Direction.FORWARD);

        // North = front of bot
        // For motors BR FR BL and FL, positive power means clockwise rotation when view on the east side of the bot.
        // That means it will move forward. if all 4 motors are powered positively
        // Intake is on the southern side. Outtake is on the northern side.
        // Limelight is mounted at the NORTH.
        // For motors Throw, In1, and In2, and servos ThrowLeft and Throw Right positive power means balls being launched
        // From south to north, intake to outtake.
        // Intake is on the southern side. Outtake is on the northern side.
    }
}
