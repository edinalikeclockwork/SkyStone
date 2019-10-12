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

        telemetry.addData("Mode", "waiting for start");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Start autonomous actions here
        // Path: Launch from Crater side

        // 10-12-2019: Servo Testing
        robot.servoLeft.setPosition(testServoOpen);
        robot.servoRight.setPosition(testServoOpen);
    }

}
