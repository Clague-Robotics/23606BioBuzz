package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/*
 * Orientation conventions:
 * - North = front of bot. Outtake and Limelight are on the north side; intake is on the south side.
 * - Drive motors (FL, FR, BL, BR): positive power means clockwise rotation when viewed from the
 *   east side of the bot, so powering all four positively drives forward.
 * - Mechanism motors (Throw, In1, In2) and servos (ThrowLeft, ThrowRight): positive power moves
 *   balls from south to north, intake to outtake.
 */
public class Robot {
    public DcMotor motorFL;
    public DcMotor motorBL;
    public DcMotor motorFR;
    public DcMotor motorBR;

    public Robot(HardwareMap hwMap) {
        motorFL = hwMap.get(DcMotor.class, "MotorFL");
        motorBL = hwMap.get(DcMotor.class, "MotorBL");
        motorFR = hwMap.get(DcMotor.class, "MotorFR");
        motorBR = hwMap.get(DcMotor.class, "MotorBR");

        motorFL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFR.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBR.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    /** Sets the same power on all four drive motors. */
    public void setDrivePower(double power) {
        motorFL.setPower(power);
        motorBL.setPower(power);
        motorFR.setPower(power);
        motorBR.setPower(power);
    }

    public void setDriveZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        motorFL.setZeroPowerBehavior(behavior);
        motorBL.setZeroPowerBehavior(behavior);
        motorFR.setZeroPowerBehavior(behavior);
        motorBR.setZeroPowerBehavior(behavior);
    }
}
