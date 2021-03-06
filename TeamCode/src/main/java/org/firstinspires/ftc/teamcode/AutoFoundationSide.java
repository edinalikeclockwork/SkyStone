package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
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

@Autonomous(name = "Auto - Foundation Side")

//@Disabled

public class AutoFoundationSide extends LinearOpMode {

/*  ***************************************** *
 *  ***************************************** *
 *  *                                       * *
 *  *     SET BELOW BEFORE EVERY MATCH      * *
 *  *                                       * *
 *  *       Foundation Side ONLY!!          * *
 *  *                                       * *
 *  ***************************************** *
 *  ***************************************** */

    String alliance = "Blue";           // Will be "Red" or "Blue"
    String moveFoundation = "No";      // Will be "Yes" or "No". No = park only
    String parkingLane = "Inside";     // Will be "Inside" or "Outside"

/*  ***************************************** *
 *  *         End pre-match setup           * *
 *  ***************************************** */

    /* Declare OpMode members. */

    //McDriveTest_HW robot = new McDriveTest_HW();
    private TeleOpCompHWMain robot = new TeleOpCompHWMain();

    private ElapsedTime runtime = new ElapsedTime();

    Orientation lastAngles = new Orientation();
    double globalAngle, power = .60, correction;

    // Create static variables
    //static final double DRIVE_GEAR_REDUCTION = 0.705;    // 60:1 Gear ratio
    static final double DRIVE_GEAR_REDUCTION = 0.475;    // 40:1. This is < 1.0 if geared UP
    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double DRIVE_SPEED = 0.6;
    static final double MOVE_FOUNDATION_SPEED = 0.5;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                          (WHEEL_DIAMETER_INCHES * 3.14159);

    String direction = "";
    String direction_reverse = "";
    int lane_distance = 0;
    boolean move_foundation = ( moveFoundation == "Yes" ) ? true : false;

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addData("Status", "Initialized");

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */

        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        sleep(500);     // pause for telemetry msg to be seen

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Version", "0.2");
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.lf.getCurrentPosition(),
                robot.rf.getCurrentPosition());
        telemetry.update();

        telemetry.addData("Status", "Initialized - Waiting for start");
        telemetry.addData("", "");
        telemetry.addData("Setup for", "");
        telemetry.addData("  Alliance", alliance);
        telemetry.addData("  Move Foundation", moveFoundation);
        telemetry.addData("  Parking Side", parkingLane);
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Start autonomous actions here
        // Path: Launch from Depot side

        /* encoderDrive parameters (power, direction, distance, timeout)
        *                Power:  0.0 to 1.0
        *    Direction options:  "left", "right", "forward" or "backward"
        *     Distance options:  inches
        *              Timeout:  decimal number in seconds
        */

        // Set direction variables based on alliance color selection during init
        /* If Blue alliance:
         *      - direction = forward
         *      - reverse_direction = backward
         * If Red alliance:
         *      - direction = backward
         *      - reverse_direction = forward
         */
        direction = (alliance == "Blue") ? "forward": "backward";
        direction_reverse = (alliance == "Blue") ? "backward": "forward";
        // Set variable base on init selection controlling if foundation gets moved or not

/* *********************************************** *
 *   Autonomous Actions: Build (Foundation) Side   *
 * *********************************************** */

        if ( move_foundation ) {
            // ACTION 1:
            // Drive: Start moving to foundation
            encoderDrive(DRIVE_SPEED, "right", 16, 4.5);
            sleep(100);

            // ACTION 2:
            // Drive: Center on foundation
            double distance = 13.5;
            double blue_offset = 3.5;
            if (alliance == "Blue") { distance = distance - blue_offset; }
            encoderDrive(DRIVE_SPEED, direction, distance, 4.5);
            sleep(100);

            // ACTION 3:
            // Drive: Drive to foundation
            encoderDrive(DRIVE_SPEED, "right", 18.5, 6);
            sleep(250);

            // ACTION 4:
            // Lower foundation latch
            robot.servoFoundation.setPosition(0.1);
            sleep(500); // Delay long enough for latch to go down

            // ACTION 5:
            // Drive: Pull foundation to wall
            encoderDrive(MOVE_FOUNDATION_SPEED, "left", 47, 9);
            sleep(100);

            // ACTION 6:
            // Raise foundation latch
            robot.servoFoundation.setPosition(0.75);
            sleep(100);

            // ACTION 7:
            // Drive: Move towards bridge to get past foundation
            distance = 35;
            if (alliance == "Blue") { distance = distance - blue_offset; }
            encoderDrive(DRIVE_SPEED, direction_reverse, distance, 5);
            sleep(100);

            // ACTION 8:
            // Drive: Align with selected lane to park in
            lane_distance = (parkingLane == "Outside") ? 3 : 25;
            encoderDrive(DRIVE_SPEED, "right", lane_distance, 2);
            sleep(100);

            // ACTION 9:
            // Drive: Park on line under bridge
            encoderDrive(DRIVE_SPEED, direction_reverse, 16, 5);
            sleep(100);

            telemetry.addData("Autonomous Path", "Complete");
            telemetry.update();
        } else {
            // Do not move foundation, only park in selected lane

            // ACTION 1:
            // Drive: Align with selected lane to park in
            lane_distance = (parkingLane == "Outside") ? 5 : 27;
            encoderDrive(DRIVE_SPEED, "right", lane_distance, 6);
            sleep(100);

            // ACTION 2:
            // Drive: Park on line under bridge
            int distance = 36;
            if (alliance == "Blue") { distance = distance - 3; }
            encoderDrive(DRIVE_SPEED, direction_reverse, distance, 9);
            sleep(100);

            telemetry.addData("Autonomous Path", "Complete");
            telemetry.update();
        }

        // End autonomous actions here
    }

    public void encoderDrive(double speed, String direction,
                             double inches,
                             double timeoutS) {

        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftRearTarget;
        int newRightRearTarget;

        int lfDirection=0;
        int rfDirection=0;
        int lrDirection=0;
        int rrDirection=0;

        double cpiCompensation = 1;

        if (direction == "left") {
            lfDirection = 1;
            lrDirection = -1;
            rfDirection = -1;
            rrDirection = 1;
            cpiCompensation = 1.1;
        } else if (direction == "right") {
            lfDirection = -1;
            lrDirection = 1;
            rfDirection = 1;
            rrDirection = -1;
            cpiCompensation = 1.1;
        } else if (direction == "forward") {
            lfDirection = -1;
            lrDirection = -1;
            rfDirection = -1;
            rrDirection = -1;
        } else if (direction == "backward") {
            lfDirection = 1;
            lrDirection = 1;
            rfDirection = 1;
            rrDirection = 1;
        } else {
            lfDirection = 1;
            lrDirection = 1;
            rfDirection = 1;
            rrDirection = 1;
        }

        double CPI = COUNTS_PER_INCH * cpiCompensation;


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            telemetry.addData("Autonomous", "Ready to Start");
            telemetry.addData("Inches", inches);
            telemetry.addData("Tics/inch", CPI);
            telemetry.addData("Direction", "%12s", direction);
            telemetry.addData("Path",  "Running at %7d :%7d",
                    robot.lf.getCurrentPosition(),
                    robot.rf.getCurrentPosition());
            telemetry.update();

//sleep(2000);    // Testing: Allows time to read telemetry

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = robot.lf.getCurrentPosition() + (int)(inches * CPI * lfDirection);
            newRightFrontTarget = robot.rf.getCurrentPosition() + (int)(inches * CPI * rfDirection);
            newLeftRearTarget = robot.lr.getCurrentPosition() + (int)(inches * CPI * lrDirection);
            newRightRearTarget = robot.rr.getCurrentPosition() + (int)(inches * CPI * rrDirection);
            robot.lf.setTargetPosition(newLeftFrontTarget);
            robot.rf.setTargetPosition(newRightFrontTarget);
            robot.lr.setTargetPosition(newLeftRearTarget);
            robot.rr.setTargetPosition(newRightRearTarget);

            // Turn On RUN_TO_POSITION
            robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.lr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rr.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();

            robot.lf.setPower(Math.abs(speed));
            robot.rf.setPower(Math.abs(speed));
            robot.lr.setPower(Math.abs(speed));
            robot.rr.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and all motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that ALL motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.lf.isBusy() && robot.rf.isBusy() &&
                     robot.lr.isBusy() && robot.rr.isBusy() )) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftFrontTarget,  newRightFrontTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.lf.getCurrentPosition(),
                        robot.rf.getCurrentPosition());
                telemetry.addData("isBusy:", "lf: %3b  rf: %3b  lr: %3b  rr: %3b",
                        robot.lf.isBusy(), robot.rf.isBusy(),
                        robot.lr.isBusy(), robot.rr.isBusy() );
                telemetry.addData("Timers: ", "Timeout: %4f, Timer: %4f", timeoutS, runtime.seconds() );
                telemetry.update();
            }

            // Stop all motion;
            robot.lf.setPower(0);
            robot.rf.setPower(0);
            robot.lr.setPower(0);
            robot.rr.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.lr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
