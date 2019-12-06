package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.hardware.bosch.BNO055IMU;
// import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 * <p>
 * This class can be used to define the specific hardware for the new_chassis.
 * <p>
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 * <p>
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Left  drive motor:        "left_lift"
 * Motor channel:  Right drive motor:        "right_lift"
 * Servo:          Marker placing servo:     "marker_servo"
 * Servo:          Lift lock servo:          "lift_lock_servo"
 * Touch:          Lift touch sensor:        "touch_sensor"
 * Color:          Color/Range sensor:       "sensorColor"
 * 09-14-2019: Added servo to test grabbing stone
 * Servo:          Grab stone server:        "test_servo"
 */

public class ServoTestHWMain {

    /* Public OpMode members. */
    public Servo servoTest;
    public BNO055IMU imu;

    public static final double servoTestInitPosition = 0.8;

    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public ServoTestHWMain() {

    }

    /* Initialize standard Hardware interfaces */

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Gyro onboard Rev Hub
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Define and initialize ALL installed servos.
        servoTest = hwMap.servo.get("test_servo");
        servoTest.setPosition(servoTestInitPosition);
    }
}