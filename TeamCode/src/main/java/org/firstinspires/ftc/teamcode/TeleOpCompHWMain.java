package org.firstinspires.ftc.teamcode;

// Based on TeamCode/src/main/java/org/firstinspires/ftc/teamcode/HardwarePushbot_BucketBrigade.java

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import java.util.Set;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

public class TeleOpCompHWMain {

    /* Public OpMode members. */
    // Drive Motors
    public DcMotor  lf = null;     // Left Front drive motor
    public DcMotor  lr = null;     // Left Rear drive motor
    public DcMotor  rf = null;     // Right Front drive motor
    public DcMotor  rr = null;     // Right Rear drive motor

    //Other Motors
    public DcMotor intakeLeft = null;
    public DcMotor intakeRight = null;
    public DcMotor liftMotor = null;

    /* Servos */
    public Servo servoFoundation;
    public Servo servoStoneArm;
    public Servo capstoneServo;
    public Servo pivotServo;
    public Servo handServo;

    /* Sensors */
    DigitalChannel magLimit;  // Magnetic limit switch on lift
    public NormalizedColorSensor colorSensorPrimary;    // Color Sensor 1
    public NormalizedColorSensor colorSensorSecondary;  // Color Sensor 2
    public DistanceSensor distanceSensorPrimary;        // Distance sensor on color sensor

    /* local OpMode members. */
    HardwareMap hwMap           = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public TeleOpCompHWMain(){
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
        intakeLeft  = hwMap.get(DcMotor.class, "intake_left");
        intakeRight = hwMap.get(DcMotor.class, "intake_right");
        liftMotor = hwMap.get (DcMotor.class, "lift_motor");


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        lf.setDirection(DcMotor.Direction.FORWARD);// Set to REVERSE if using AndyMark motors
        lr.setDirection(DcMotor.Direction.FORWARD);// Set to REVERSE if using AndyMark motors
        rf.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        rr.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        intakeLeft.setDirection(DcMotor.Direction.REVERSE);
        intakeRight.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        lf.setPower(0);
        lr.setPower(0);
        rf.setPower(0);
        rr.setPower(0);
        intakeLeft.setPower(0);
        intakeRight.setPower(0);
        liftMotor.setPower(0);

        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set all drive motors to run with encoders.
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Define and initialize ALL installed servos.
        //
        // Servo to control foundation latch - init to raised position
        servoFoundation = hwMap.servo.get("foundation_servo");
        servoFoundation.setPosition(0.75);

        // Servo to control stone capture arm - init to raised position
        // Lower position numbers raise arm farther
        servoStoneArm = hwMap.servo.get("stone_arm_servo");
        servoStoneArm.setPosition(0.0);

        //Servo to place capstone on foundation
        capstoneServo = hwMap.servo.get("capstone_servo");
        //capstoneServo.setPosition(0.2);

        //Smart Servo for rack and pinion arm
        pivotServo = hwMap.servo.get("pivot_servo");
        pivotServo.setPosition(1.0);
        handServo = hwMap.servo.get("hand_servo");
        handServo.setPosition(1.0);

        // Color sensors
        colorSensorPrimary = hwMap.get(NormalizedColorSensor.class, "color_sensor1");
        colorSensorSecondary = hwMap.get(NormalizedColorSensor.class, "color_sensor2");

        // Distance sensors
        distanceSensorPrimary = hwMap.get(DistanceSensor.class, "color_sensor1");

        // Magnetic limit sensor
        magLimit = hwMap.get(DigitalChannel.class, "mag_limit");

        // set the digital channel to input.
        magLimit.setMode(DigitalChannel.Mode.INPUT);
    }
}
