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

@Autonomous(name = "StoneServoTest")

// @Disabled

public class StoneServoTest extends LinearOpMode {

    /* Declare OpMode members. */

    StoneServoHWMain robot = new StoneServoHWMain();

    private ElapsedTime runtime = new ElapsedTime();

    Orientation             lastAngles = new Orientation();
    double globalAngle, power = .60, correction;

    // Create static variables
    static final double deployServoPosition = 0.7;
    static final double resetServoPosition = 0;
    static boolean stoneServoState = true;

    // 09-14-2019: Stone Servo Testing
    static final double stoneServoOpen = 0.8;
    static final double stoneServoClosed = 1.0;

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
        telemetry.update();

        telemetry.addData("Mode", "waiting for start");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Stone Servo Testing
        robot.stoneServo.setPosition(stoneServoOpen);
        sleep(3000);
        robot.stoneServo.setPosition(stoneServoClosed);
        sleep(3000);
        // robot.stoneServo.setPosition(stoneServoOpen);

        sleep(50000);
    }

}
