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

public class HardwareMain {

    /* Public OpMode members. */
    public DcMotor          leftDrive   = null;
    public DcMotor          rightDrive  = null;
    public DcMotor          leftLift   = null;
    public DcMotor          rightLift  = null;
    public Servo            markerServo;
    public Servo            lockServo;
    public Servo            stoneServo;
    public BNO055IMU        imu;
    public DigitalChannel   digitalTouch;


    public static final double MID_SERVO         = 0.5;
    public static final double servoInitPosition = 0.0;
    public static final double stoneServoInitPosition = 0.8;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareMain(){

    }

    /* Initialize standard Hardware interfaces */

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        // Drive Motors
        leftDrive  = hwMap.get(DcMotor.class, "left_drive");
        rightDrive = hwMap.get(DcMotor.class, "right_drive");
        leftDrive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE
        rightDrive.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD
        // Lift Motors
        leftLift = hwMap.get(DcMotor.class, "left_lift");
        rightLift = hwMap.get(DcMotor.class, "right_lift");

        // Gyro onboard Rev Hub
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Touch Sensor on lift
        digitalTouch = hwMap.digitalChannel.get("touch_sensor");
        // set the digital channel to input.
        digitalTouch.setMode(DigitalChannel.Mode.INPUT);
        
        // Drive motors use encoders, lift motors do not
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set drive and lift motors to zero power
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        leftLift.setPower(0);
        rightLift.setPower(0);

        // Define and initialize ALL installed servos.
        stoneServo = hwMap.servo.get("stone_servo");
        stoneServo.setPosition(stoneServoInitPosition);
        markerServo = hwMap.servo.get("marker_servo");
        markerServo.setPosition(servoInitPosition);
        lockServo = hwMap.servo.get("lift_lock_servo");
        lockServo.setPosition(0.50);    // Locked Position, open Position applied in main Auto script
        //lockServo.setPosition(0.24);  // Open Position applied in main Auto script

    }
}
