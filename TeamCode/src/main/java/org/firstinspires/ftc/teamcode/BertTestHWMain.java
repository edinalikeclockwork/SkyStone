package org.firstinspires.ftc.teamcode;

// Based on TeamCode/src/main/java/org/firstinspires/ftc/teamcode/HardwarePushbot_BucketBrigade.java

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Set;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left Front  drive motor:    "left_front"
 * Motor channel:  Left Rear   drive motor:    "left_rear"
 * Motor channel:  Right Front drive motor:    "right_front"
 * Motor channel:  Right Rear  drive motor:    "right_rear"
 *
 * Servo:    ServoFoundation   Foundation Latch: "foundation_servo"
 * Servo:    ServoStoneArm     Stone Arm:        "stone_arm_servo"
 */

public class BertTestHWMain {

    /* Public OpMode members. */
    // Drive Motors
    public DcMotor lf = null;     // Left Front drive motor
    public DcMotor lr = null;     // Left Rear drive motor
    public DcMotor rf = null;     // Right Front drive motor
    public DcMotor rr = null;     // Right Rear drive motor

    //Other Motors
    public DcMotor flapper = null;  //Stone, ramp, flapper

    /* Servos */
    public Servo servoFoundation;
    public Servo servoStoneArm;
    public Servo intakeRampRight;
    public Servo intakeRampLeft;
    public Servo lowerIntakeRight;
    public Servo lowerIntakeLeft;
    public Servo upperIntakeRight;
    public Servo upperIntakeLeft;
    public Servo dumpRamp;
    public Servo capstoneServo;

    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public BertTestHWMain() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        lf = hwMap.get(DcMotor.class, "left_front");
        lr = hwMap.get(DcMotor.class, "left_rear");
        rf = hwMap.get(DcMotor.class, "right_front");
        rr = hwMap.get(DcMotor.class, "right_rear");
        flapper = hwMap.get(DcMotor.class, "flapper");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        lf.setDirection(DcMotor.Direction.FORWARD);// Set to REVERSE if using AndyMark motors
        lr.setDirection(DcMotor.Direction.FORWARD);// Set to REVERSE if using AndyMark motors
        rf.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        rr.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Set all motors to zero power
        lf.setPower(0);
        lr.setPower(0);
        rf.setPower(0);
        rr.setPower(0);

        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set all drive motors to run with encoders.
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // set flapper motor to use encoder
        flapper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flapper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Define and initialize ALL installed servos.
        //
        // Servo to control foundation latch - init to raised position
        servoFoundation = hwMap.servo.get("foundation_servo");
        servoFoundation.setPosition(0.75);

        // Servo to control stone capture arm - init to raised position
        // Lower position numbers raise arm farther
        servoStoneArm = hwMap.servo.get("stone_arm_servo");
        servoStoneArm.setPosition(0.47);

        // Servo to control dump ramp - init to lowered position
        // Small numbers raise the ramp
        dumpRamp = hwMap.servo.get("dump_ramp");
        dumpRamp.setPosition(0.43);

        // Servos to control intake ramp - init to raised position
        intakeRampRight = hwMap.servo.get("intake_ramp_right");
        intakeRampLeft = hwMap.servo.get("intake_ramp_left");
        //intakeRampRight.setPosition(0.5);
        //intakeRampLeft.setPosition(0.5);
        // Ramp in up position
        intakeRampRight.setPosition(1.0);
        intakeRampLeft.setPosition(0.08);

        // Continuous rotation servos on ramp wheels
        //   - Set to 0.5 (or close) so they init as stopped
        lowerIntakeRight = hwMap.servo.get("lower_intake_right");
        lowerIntakeLeft = hwMap.servo.get("lower_intake_left");
        lowerIntakeRight.setPosition(0.49);
        lowerIntakeLeft.setPosition(0.50);
        upperIntakeRight = hwMap.servo.get("upper_intake_right");
        upperIntakeLeft = hwMap.servo.get("upper_intake_left");
        upperIntakeRight.setPosition(0.50);
        upperIntakeLeft.setPosition(0.49);

        //Servo to place capstone on foundation
        capstoneServo = hwMap.servo.get("capstone_servo");
        capstoneServo.setPosition(0.2);
    }
}
