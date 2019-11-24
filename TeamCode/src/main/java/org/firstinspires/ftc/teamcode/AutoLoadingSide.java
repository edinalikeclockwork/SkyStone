package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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

@Autonomous(name = "Auto - Loading Side")

// @Disabled

public class AutoLoadingSide extends LinearOpMode {

/*  ***************************************** *
 *  ***************************************** *
 *  *                                       * *
 *  *     SET BELOW BEFORE EVERY MATCH      * *
 *  *                                       * *
 *  *         Loading Side ONLY!!           * *
 *  *                                       * *
 *  ***************************************** *
 *  ***************************************** */

    String alliance = "Red";           // Will be "Red" or "Blue"
    String parkingLane = "Outside";     // Will be "Inside" or "Outside"

/*  ***************************************** *
 *  *         End pre-match setup           * *
 *  ***************************************** */

    /* Declare OpMode members. */

    //McDriveTest_HW robot = new McDriveTest_HW();
    private BertTestHWMain robot = new BertTestHWMain();

    private ElapsedTime runtime = new ElapsedTime();

    Orientation lastAngles = new Orientation();
    double globalAngle, power = .60, correction;

    // Create static variables
    static final double COUNTS_PER_MOTOR_REV = 1440;     // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 0.705;    // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double DRIVE_SPEED = 0.7;
    static final double MOVE_FOUNDATION_SPEED = 0.5;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                          (WHEEL_DIAMETER_INCHES * 3.14159);

    String reposition = "Yes";    // Will always be "Yes" since we cannot move a stone

    String direction = "";
    String direction_reverse = "";
    int lane_distance = 0;
    boolean park_only = false;

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */

        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
/*  Moved this section to BertTestHWMain.java
*/

        sleep(500);     // pause for telemetry msg to be seen

        // Send telemetry message to indicate successful Encoder reset
/*
        telemetry.addData("Version", "0.2");
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.lf.getCurrentPosition(),
                robot.rf.getCurrentPosition());
        telemetry.update();
*/
        telemetry.addData("Mode", "waiting for start");
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
        park_only = ( reposition == "Yes" ) ? true : false;

/* ********************************** *
 *   Autonomous Actions: Loading Side *
 * ********************************** */
        if ( park_only == false ) {
        // This should never exexute at Lakeville since we are unable to move a stone
            // ACTION 1:
            // Drive: Start moving to blocks
            encoderDrive(DRIVE_SPEED, "left", 16, 3.5);
            sleep(100);

            // ACTION 2:
            // Drive: Center on a block
            encoderDrive(DRIVE_SPEED, direction_reverse, 4, 4);
            sleep(100);

            // ACTION 3:
            // Drive: Drive to block
            encoderDrive(DRIVE_SPEED, "left", 17, 4);
            sleep(250);

            // ACTION 4:
            // Lower stone arm
            robot.servoStoneArm.setPosition(1.0);
            sleep(500); // Delay long enough for latch to go down

            // ACTION 5a:
            // Drive: Back away from stones and align with parking lane
            lane_distance = (parkingLane == "Outside") ? 32 : 12;
            encoderDrive(DRIVE_SPEED, "right", lane_distance, 2);
            sleep(100);

            // ACTION 5b:
            // Drive: Bring block across line
            encoderDrive(DRIVE_SPEED, direction_reverse, 43, 9);
            sleep(100);

            // ACTION 6:
            // Raise stone arm
            robot.servoStoneArm.setPosition(0.4);
            sleep(100);

            // ACTION 7:
            // Drive: Park on under bridge
            encoderDrive(DRIVE_SPEED, direction, 15, 5);
            sleep(100);

        } else {
            // ACTION 1:
            // Drive: Move away from wall and align with selected lane
            lane_distance = (parkingLane == "Outside") ? 4 : 27;
            encoderDrive(DRIVE_SPEED, "left", lane_distance, 8);
            sleep(100);

            // ACTION 2:
            // Drive: Park on line
            encoderDrive(DRIVE_SPEED, direction_reverse, 40, 9);
            sleep(100);

            telemetry.addData("Autonomous Path", "Complete");
            telemetry.addData("", "");
            telemetry.addData("Sleeping", "to keep ramp up");
            telemetry.update();

            sleep(20000);   // Sleep to keep ramp up for most of Autonomous
        }

        // End autonomous actions here

        telemetry.addData("Autonomous Path", "Complete");
        telemetry.update();

        sleep(2000);
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
            sleep(100);
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
