package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "ServoPickupTest")

// @Disabled

public class ServoPickupTest extends LinearOpMode {

    /* Declare OpMode members. */

    HWMainSimple robot = new HWMainSimple();

    private ElapsedTime runtime = new ElapsedTime();

    // Create static variables
   
    static boolean testServoState = true;

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
        robot.testServo.setPosition(testServoOpen);
        sleep(3000);
        robot.testServo.setPosition(testServoClosed);
        sleep(3000);
        // robot.testServo.setPosition(testServoOpen);

        sleep(50000);
    }

}
