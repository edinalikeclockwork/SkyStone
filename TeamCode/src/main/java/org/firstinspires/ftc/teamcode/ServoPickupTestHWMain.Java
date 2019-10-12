package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define the specific hardware for the new_chassis.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Left  drive motor:        "left_lift"
 * Motor channel:  Right drive motor:        "right_lift"
 * Servo:          Marker placing servo:     "marker_servo"
 * Servo:          Lift lock servo:          "lift_lock_servo"
 * Touch:          Lift touch sensor:        "touch_sensor"
 * Color:          Color/Range sensor:       "sensorColor"
09-14-2019: Added servo to test grabbing stone
 * Servo:          Grab stone server:     "stone_servo"
 */

public class ServoPickupTestHWMain {

    /* Public OpMode members. */
    public Servo            servoLeft;
    public Servo            servoRight;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HWMainSimple(){

    }

    /* Initialize standard Hardware interfaces */

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and initialize ALL installed servos.
        servoLeft = hwMap.servo.get("left_servo");
        servoRight = hwMap.servo.get("right_servo");
    }
}
