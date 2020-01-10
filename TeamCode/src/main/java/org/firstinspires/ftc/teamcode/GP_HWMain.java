package org.firstinspires.ftc.teamcode;

// Based on TeamCode/src/main/java/org/firstinspires/ftc/teamcode/HardwarePushbot_BucketBrigade.java

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

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

public class GP_HWMain {

    /* Public OpMode members. */
    // Drive Motors
    public DcMotor  lf = null;     // Left Front drive motor
    public DcMotor  lr = null;     // Left Rear drive motor
    public DcMotor  rf = null;     // Right Front drive motor
    public DcMotor  rr = null;     // Right Rear drive motor

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public GP_HWMain(){

    }
    /** The colorSensor field will contain a reference to our color sensor hardware object */
    public NormalizedColorSensor colorSensor;

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        lf = hwMap.get(DcMotor.class, "left_front");
        lr = hwMap.get(DcMotor.class, "left_rear");
        rf = hwMap.get(DcMotor.class, "right_front");
        rr = hwMap.get(DcMotor.class, "right_rear");

        colorSensor = hwMap.get(NormalizedColorSensor.class, "color_test");

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

    }
}
