package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name = "ServoTest")

// @Disabled

public class ServoTest extends LinearOpMode {

    /* Declare OpMode members. */

    ServoTestHWMain robot = new ServoTestHWMain();

    private ElapsedTime runtime = new ElapsedTime();

    Orientation lastAngles = new Orientation();
    double globalAngle, power = .60, correction;

    // Create static variables
    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 0.8;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double DRIVE_SPEED = 0.5;
    static final double TURN_SPEED = 0.3;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double deployServoPosition = 0.7;
    static final double openLiftLockServoPosition = 0.9;
    static final double resetServoPosition = 0;
    static final double liftPower = 0.4;
    static boolean touchState = true;
    static int down = -1;   // Reverses direction of motors to lower lift
    static boolean testServoState = true;

    // 09-14-2019: Servo Testing
    static final double testServoOpen = 0.8;
    static final double testServoClosed = 1.0;

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */

        robot.init(hardwareMap);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Version", "0.1");
        //telemetry.addData("Path0",  "Starting at %7d :%7d",
        //robot.leftDrive.getCurrentPosition(),
        //robot.rightDrive.getCurrentPosition());
        telemetry.update();

        // TODO: Init gyro - should set to zero (? validate this)
        telemetry.addData("Mode", "calibrating...");
        telemetry.update();

        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !robot.imu.isGyroCalibrated()) {
            sleep(50);
            idle();
        }

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", robot.imu.getCalibrationStatus().toString());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Start autonomous actions here
        // Path: Launch from Crater side

        // 09-14-2019: Servo Testing
        robot.servoTest.setPosition(testServoOpen);
        sleep(3000);
        robot.servoTest.setPosition(testServoClosed);
        sleep(3000);
        // robot.servoTest.setPosition(testServoOpen);

        sleep(50000);
    }

}