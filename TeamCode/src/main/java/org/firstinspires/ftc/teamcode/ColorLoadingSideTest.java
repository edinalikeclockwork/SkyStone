package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
//import com.qualcomm.robotcore.hardware.ColorSensor;
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

@Autonomous(name = "Auto - ColorLoadingSide")

// @Disabled

public class ColorLoadingSideTest extends LinearOpMode {

//ColorSensorTest colorSensorTest = new ColorSensorTest();
//StoneColor stoneColor = new StoneColor();


    /*  ***************************************** *
     *  ***************************************** *
     *  *                                       * *
     *  *     SET BELOW BEFORE EVERY MATCH      * *
     *  *                                       * *
     *  *         Loading Side ONLY!!           * *
     *  *                                       * *
     *  ***************************************** *
     *  ***************************************** */

    String alliance = "Blue";           // Will be "Red" or "Blue"
    String parkingLane = "Inside";     // Will be "Inside" or "Outside"
    String reposition = "No";           // Will always be "Yes" since we cannot move a stone

    /*  ***************************************** *
     *  *         End pre-match setup           * *
     *  ***************************************** */

    /* Declare OpMode members. */

    //McDriveTest_HW robot = new McDriveTest_HW();
    private GP_HWMain robot = new GP_HWMain();

    private ElapsedTime runtime = new ElapsedTime();

    Orientation lastAngles = new Orientation();
    double globalAngle, power = .60, correction;

    // Create static variables
    static final double COUNTS_PER_MOTOR_REV = 1440;     // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 0.705;    // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double DRIVE_SPEED = 0.5;
    static final double COLOR_SPEED = 0.3;
    static final double MOVE_FOUNDATION_SPEED = 0.5;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159);


    String direction = "";
    String direction_reverse = "";
    int lane_distance = 0;
    int deliver_distance = 43;  // Base inches to deliver stone under bridge
    boolean park_only = false;
    boolean skystone;

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

/*
        skystone = sampleStone();
        //telemetry.addData("int g = ", colorSensorTest.function(1));
        telemetry.addData("1st Skystone = ", skystone);
        telemetry.update();
        sleep(8000);
        skystone = sampleStone();
        telemetry.addData("2nd Skystone = ", skystone);
        telemetry.update();
        sleep(12000);
*/


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
        direction = (alliance == "Blue") ? "forward" : "backward";
        direction_reverse = (alliance == "Blue") ? "backward" : "forward";
        park_only = (reposition == "Yes") ? true : false;

        /* ********************************** *
         *   Autonomous Actions: Loading Side *
         * ********************************** */
        if (park_only == false) {
            // Find and deliver Skystone

            // ACTION 1:
            // Drive: Move to stones
            encoderDrive(DRIVE_SPEED, "left", 30, 4);
            sleep(100);

            // ACTION 2:
            // Find Skystone
            skystone = sampleStone();
            sleep(20000);
            if (!skystone) {
                encoderDrive(COLOR_SPEED, direction, 8, 3.5);
                deliver_distance += 8;
                skystone = sampleStone();
                if (!skystone) {
                    encoderDrive(COLOR_SPEED, direction, 8, 3.5);
                    deliver_distance += 8;
                }
            }

            // Action 3:
            // Pickup Stone
            // a) Center on Stone
            // b) Drive to Stone
            // c) Lower stone arm
                    //robot.servoStoneArm.setPosition(1.0);
                    //sleep(500); // Delay long enough for latch to go down

            // ACTION 4:
            // Drive: Backup from stones
            lane_distance = (parkingLane == "Outside") ? 32 : 12;
            encoderDrive(DRIVE_SPEED, "right", lane_distance, 5);
            sleep(100);

            // Action 5:
            // Drive under bridge to deliver stone (43+)
            telemetry.addData("Deliver distance:", deliver_distance);
            telemetry.update();
            sleep(2000);
            encoderDrive(DRIVE_SPEED, direction_reverse, deliver_distance, 3.5);
            sleep(100);

            // Action 6:
            // Release Stone
            // robot.servoStoneArm.setPosition(0.4);
            sleep(100);

            // Action 7:
            // Park under bridge
            encoderDrive(DRIVE_SPEED, direction, 18, 3.5);

        } else {
            // Parking Only

            // ACTION 1:
            // Drive: Move away from wall and align with selected lane
            lane_distance = (parkingLane == "Outside") ? 4 : 27;
            encoderDrive(DRIVE_SPEED, "left", lane_distance, 8);
            sleep(100);

            // ACTION 2:
            // Drive: Park on line in selected lane
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

/* * * * * * *
 *  Methods  *
 * * * * * * */

    public void encoderDrive(double speed, String direction,
                             double inches,
                             double timeoutS) {

        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftRearTarget;
        int newRightRearTarget;

        int lfDirection = 0;
        int rfDirection = 0;
        int lrDirection = 0;
        int rrDirection = 0;

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
            telemetry.addData("Path", "Running at %7d :%7d",
                    robot.lf.getCurrentPosition(),
                    robot.rf.getCurrentPosition());
            telemetry.update();

//sleep(2000);    // Testing: Allows time to read telemetry

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = robot.lf.getCurrentPosition() + (int) (inches * CPI * lfDirection);
            newRightFrontTarget = robot.rf.getCurrentPosition() + (int) (inches * CPI * rfDirection);
            newLeftRearTarget = robot.lr.getCurrentPosition() + (int) (inches * CPI * lrDirection);
            newRightRearTarget = robot.rr.getCurrentPosition() + (int) (inches * CPI * rrDirection);
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
                            robot.lr.isBusy() && robot.rr.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftFrontTarget, newRightFrontTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.lf.getCurrentPosition(),
                        robot.rf.getCurrentPosition());
                telemetry.addData("isBusy:", "lf: %3b  rf: %3b  lr: %3b  rr: %3b",
                        robot.lf.isBusy(), robot.rf.isBusy(),
                        robot.lr.isBusy(), robot.rr.isBusy());
                telemetry.addData("Timers: ", "Timeout: %4f, Timer: %4f", timeoutS, runtime.seconds());
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
        ColorSensorTest colorSensorTest = new ColorSensorTest();

        telemetry.addData("F", colorSensorTest.function(1));
        telemetry.update();
    }

    public boolean sampleStone() {

        int counter = 0;
        int sample_loops = 5;  // Number of times to sample color in loop

        double h_total = 0.0;
        double r_total = 0.0;
        double b_total = 0.0;
        double h_average = 0.0;
        double r_average = 0.0;
        double b_average = 0.0;
        double difference = 0.0;

        // values is a reference to the hsvValues array.
        float[] hsvValues = new float[3];
        final float values[] = hsvValues;

        while (opModeIsActive() && counter <= sample_loops) {

            // Read the sensor
            NormalizedRGBA colors = robot.colorSensor.getNormalizedColors();
            Color.colorToHSV(colors.toColor(), hsvValues);

            telemetry.addLine()
                    .addData("H", "%.3f", hsvValues[0])
                    .addData("S", "%.3f", hsvValues[1])
                    .addData("V", "%.3f", hsvValues[2]);
            telemetry.addLine()
                    .addData("a", "%.3f", colors.alpha)
                    .addData("r", "%.3f", colors.red)
                    .addData("g", "%.3f", colors.green)
                    .addData("b", "%.3f", colors.blue);

            // Running total of red and blue values
            h_total = h_total + hsvValues[0];
            r_total = r_total + colors.red;
            b_total = b_total + colors.blue;

            counter++;
            telemetry.addData("Counter", counter);
            telemetry.update();
            sleep(10);

        }

        //Calculate red and blue average and then find difference
        h_average = h_total/counter;
        r_average = r_total/counter;
        b_average = b_total/counter;
        telemetry.addData("r_average = ", r_average);
        telemetry.addData("b_average = ", b_average);
        difference = Math.abs(r_average - b_average);
        telemetry.addData("difference = ", difference);

        //checks if data leads to skystone or not
        boolean skystone;
        if ( h_average > 100 ) {
            skystone = true;
        } else {
            skystone = false;
        }
/*
        if (difference < 0.200) {
            skystone = true;
        } else {
            skystone = false;
        }
*/
        //skystone = false;
        telemetry.addData("Method: skystone = ", skystone);
        telemetry.update();

sleep(1000);
        return skystone;
    }
}
