package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

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

@Autonomous(name = "Auto - LoadingSide")

// @Disabled

public class AutoLoadingSide extends LinearOpMode {

    // Note: This was ColorLoadingSideTest.java


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
    String parkingLane = "Inside";     // Will be "Inside" or "Outside"
    boolean find_skystone = true;      // true to find Skystone, false park only
                                       // NOTE: *no* quote around true or false

    /*  ***************************************** *
     *  *         End pre-match setup           * *
     *  ***************************************** */

    // Disabled use of 'reposition' variable.  Use 'park_only' above
    //String reposition = "No";           // Will always be "Yes" since we cannot move a stone

    /* Declare OpMode members. */

    //private GP_HWMain robot = new GP_HWMain();
    private TeleOpCompHWMain robot = new TeleOpCompHWMain();

    private ElapsedTime runtime = new ElapsedTime();

    Orientation lastAngles = new Orientation();
    double globalAngle, power = .60, correction;

    // Create static variables
    static final double COUNTS_PER_MOTOR_REV = 1440;     // eg: TETRIX Motor Encoder
    //static final double DRIVE_GEAR_REDUCTION = 0.705;   // 60:1 Gear ratio (Guinea Pig)
    static final double DRIVE_GEAR_REDUCTION = 0.475;    // 40:1 (BERT). This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double DRIVE_SPEED = 0.6;
    static final double COLOR_SPEED = 0.3;
    static final double MOVE_FOUNDATION_SPEED = 0.5;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159);


    String direction = "";
    String direction_reverse = "";
    int lane_distance = 0;
    int deliver_distance = 43;  // Base inches to deliver stone under bridge
    double distance_inch1 = 0;   // Holds return from distance sensor
    double distance_inch2 = 0;   // Holds return from distance sensor

    boolean[] skystone;

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

        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        sleep(500);     // pause for telemetry msg to be seen

        telemetry.addData("Status", "Initialized - Waiting for start");
        telemetry.addData("", "");
        telemetry.addData("Setup for", "");
        telemetry.addData("  Alliance", alliance);
        telemetry.addData("  Find SkyStone", find_skystone);
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
        direction = (alliance == "Blue") ? "forward" : "backward";
        direction_reverse = (alliance == "Blue") ? "backward" : "forward";
        //park_only = (reposition == "Yes") ? true : false;

        boolean skystone_primary;
        boolean skystone_secondary;

boolean test_color_sensor_only = false;
while (test_color_sensor_only) {
    // Loop for testing color sensor
    // Set test_color_sensor_only = false to run below actions
        skystone = sampleStone();
        //telemetry.addData("int g = ", colorSensorTest.function(1));
        //telemetry.addData("1st Skystone = ", skystone);
        //telemetry.update();
        sleep(5000);
        skystone = sampleStone();
        sleep(5000);
        skystone = sampleStone();
        sleep(5000);
        skystone = sampleStone();
        sleep(10000);
}


        /* ********************************** *
         *   Autonomous Actions: Loading Side *
         * ********************************** */
        if (find_skystone == true) {
            // Find and deliver Skystone

            // ACTION 1:
            // Drive: Move to stones.  31.5 worked, very close
            encoderDrive(DRIVE_SPEED*0.8, "left", 31, 5);
            sleep(80);

            // ACTION 2:
            // Find Skystone
            skystone = sampleStone();
            skystone_primary = skystone[0];
            skystone_secondary = skystone[1];
            // Get distance from stone and convert to inches
            distance_inch1 = robot.distanceSensorPrimary.getDistance(DistanceUnit.CM);
            distance_inch1 *= 0.3937007874015748; // cm -> in

            sleep(100);    // KEEP SMALL FOR COMPETITION
            if (!skystone_primary && !skystone_secondary) {
                encoderDrive(COLOR_SPEED, direction, 8, 2.0);
                deliver_distance += 8;
                skystone = sampleStone();
                skystone_primary = skystone[0];
                skystone_secondary = skystone[1];
                sleep(80);    // KEEP SMALL FOR COMPETITION
                if (!skystone_primary) {
                    encoderDrive(COLOR_SPEED, direction, 8, 2.0);
                    deliver_distance += 8;
                }
            } else if (false && skystone_primary && !skystone_secondary) {
                // Disabled: This logic doesn't work when sensor faulty
                encoderDrive(COLOR_SPEED, direction, 1, 3.5);
            }

            // Action 3:
            // Pickup Stone
            // a) Center on Stone?
            // b) Check Distance to Stone
            distance_inch2 = robot.distanceSensorPrimary.getDistance(DistanceUnit.CM);
            distance_inch2 *= 0.3937007874015748; // cm -> in
            // c) Drive closer to stone if necessary
            if ( distance_inch2 > 1.0 ) {
                double adjustDistance = distance_inch2 - 0.75;
                telemetry.addData("Distance 1:", distance_inch1);
                telemetry.addData("Distance 2:", distance_inch2);
                telemetry.addData("Drive adjust:", adjustDistance);
                telemetry.update();
                encoderDrive(COLOR_SPEED, "left", adjustDistance+3.0, 2);
            } else if ( distance_inch2 > 1.0 ) {
                double adjustDistance = distance_inch2 - 1;
                encoderDrive(COLOR_SPEED, "left", adjustDistance, 2);
            }
            sleep(80);
            // c) Lower stone arm
            robot.servoStoneArm.setPosition(1.0);
            sleep(700); // ALERT: Delay long enough for arm to go down

            // ACTION 4:
            // Drive: Backup from stones
            lane_distance = (parkingLane == "Outside") ? 32 : 13;
            encoderDrive(DRIVE_SPEED, "right", lane_distance, 5);
            sleep(80);

            // ACTION 4a:
            // Check if (Sky)Stone captured
            int have_stone = stoneInArm();

            // Active 5a:
            // No stone captured, just park on line
            if (have_stone == 0) {

                robot.servoStoneArm.setPosition(0.0);
                int park_only_distance = deliver_distance - 18;
                telemetry.addData("Only park on line:", park_only_distance);
                telemetry.update();
                encoderDrive(DRIVE_SPEED, direction_reverse, park_only_distance, 3.5);
                sleep(100);

                // Action 4b:
                // Move farther into lane if parking on inside
                if (parkingLane == "Inside") {
                    encoderDrive(DRIVE_SPEED, "left", 4, 2);
                    sleep(100);
                }
            // Stone captured!
            // Action 5b:
            } else {

                // Drive under bridge to deliver stone (43+)
                telemetry.addData("Deliver distance:", deliver_distance);
                telemetry.update();
                encoderDrive(DRIVE_SPEED, direction_reverse, deliver_distance, 3.5);
                sleep(80);

                // Action 5a:
                // Move farther into lane if parking on inside
                if (parkingLane == "Inside") {
                    encoderDrive(DRIVE_SPEED, "left", 4, 2);
                    sleep(80);
                }

                // Action 6:
                // Release Stone
                robot.servoStoneArm.setPosition(0.0);
                sleep(80);

                // Action 7:
                // Park under bridge
                encoderDrive(DRIVE_SPEED*0.75, direction, 18, 3.5);
            }
        } else {
            // Parking Only

            // ACTION 1:
            // Drive: Move away from wall and align with selected lane
            lane_distance = (parkingLane == "Outside") ? 4 : 26;
            double speedDrop = 1;
            if (parkingLane == "Inside") {
                speedDrop = 0.75;
            }
            encoderDrive(DRIVE_SPEED * speedDrop, "left", lane_distance, 8);
            sleep(100);

            // ACTION 2:
            // Drive: Park on line in selected lane
            encoderDrive(DRIVE_SPEED * speedDrop, direction_reverse, 24, 7);
            sleep(100);

            telemetry.addData("Autonomous Path", "Complete");
            telemetry.addData("", "");
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

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = robot.lf.getCurrentPosition() + (int) (inches * CPI * lfDirection);
            newRightFrontTarget = robot.rf.getCurrentPosition() + (int) (inches * CPI * rfDirection);
            newLeftRearTarget = robot.lr.getCurrentPosition() + (int) (inches * CPI * lrDirection);
            newRightRearTarget = robot.rr.getCurrentPosition() + (int) (inches * CPI * rrDirection);
            robot.lf.setTargetPosition(newLeftFrontTarget);
            robot.rf.setTargetPosition(newRightFrontTarget);
            robot.lr.setTargetPosition(newLeftRearTarget);
            robot.rr.setTargetPosition(newRightRearTarget);

            telemetry.addData("Autonomous", "Ready to Start");
            telemetry.addData("Inches", inches);
            telemetry.addData("Tics/inch", CPI);
            telemetry.addData("Direction", "%12s", direction);
            telemetry.addData("Path", "Running at %7d :%7d",
                    robot.lf.getCurrentPosition(),
                    robot.rf.getCurrentPosition());
            telemetry.addData("Targets", "Go to %7d :%7d",
                    newLeftFrontTarget,
                    newRightFrontTarget);
            telemetry.update();
sleep(100);    // Testing: Allows time to read telemetry

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
                telemetry.addData("Path1", "Running to %7d :%7d :%7d :%7d",
                                   newLeftFrontTarget, newRightFrontTarget,
                                   newLeftRearTarget,  newRightRearTarget);
                telemetry.addData("Path2", "Running at %7d :%7d :%7d :%7d",
                        robot.lf.getCurrentPosition(),
                        robot.rf.getCurrentPosition(),
                        robot.lr.getCurrentPosition(),
                        robot.rr.getCurrentPosition());
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
    }

    public boolean[] sampleStone() {

        int counter = 0;
        int sample_loops = 20;  // Number of times to sample color in loop

        double h_total_primary = 0.0;
        double h_total_secondary = 0.0;
        double r_total = 0.0;
        double b_total = 0.0;
        double h_average_primary = 0.0;
        double h_average_secondary = 0.0;
        double r_average = 0.0;
        double b_average = 0.0;
        double difference = 0.0;

        // values[Primary|Secondary] are a references to the respective hsvValues arrays.
        float[] hsvValuesPrimary = new float[3];
        final float valuesPrimary[] = hsvValuesPrimary;
        float[] hsvValuesSecondary = new float[3];
        final float valuesSecondary[] = hsvValuesSecondary;


        while (opModeIsActive() && counter <= sample_loops) {

            // Read the sensors
            // Primary sensor - middle of robot under stone arm
            NormalizedRGBA colorsPrimary = robot.colorSensorPrimary.getNormalizedColors();
            Color.colorToHSV(colorsPrimary.toColor(), hsvValuesPrimary);
            // Secondary sensor - Left of primary sensor by 2 1/2"
            NormalizedRGBA colorsSecondary = robot.colorSensorSecondary.getNormalizedColors();
            Color.colorToHSV(colorsSecondary.toColor(), hsvValuesSecondary);


            telemetry.addLine()
                    .addData("H", "%.3f", hsvValuesPrimary[0])
                    .addData("S", "%.3f", hsvValuesPrimary[1])
                    .addData("V", "%.3f", hsvValuesPrimary[2]);
            telemetry.addLine()
                    .addData("H2", "%.3f", hsvValuesSecondary[0])
                    .addData("S2", "%.3f", hsvValuesSecondary[1])
                    .addData("V2", "%.3f", hsvValuesSecondary[2]);
            telemetry.addLine()
                    .addData("a", "%.3f", colorsPrimary.alpha)
                    .addData("r", "%.3f", colorsPrimary.red)
                    .addData("g", "%.3f", colorsPrimary.green)
                    .addData("b", "%.3f", colorsPrimary.blue);

            // Running total of red and blue values
            h_total_primary = h_total_primary + hsvValuesPrimary[0];
            h_total_secondary = h_total_secondary + hsvValuesSecondary[0];
            r_total = r_total + colorsPrimary.red;
            b_total = b_total + colorsPrimary.blue;

            counter++;
            telemetry.addData("Counter", counter);
            telemetry.update();
            sleep(10);

        }

        //Calculate red and blue average and then find difference
        h_average_primary = h_total_primary/counter;
        h_average_secondary = h_total_secondary/counter;
        r_average = r_total/counter;
        b_average = b_total/counter;
        telemetry.addData("** h_average_p = ", h_average_primary);
        telemetry.addData("** h_average_s = ", h_average_secondary);
        telemetry.addData("r_average = ", r_average);
        telemetry.addData("b_average = ", b_average);
        difference = Math.abs(r_average - b_average);
        telemetry.addData("difference = ", difference);

        //checks if data leads to skystone or not
        boolean skystone_primary;
        if ( h_average_primary > 100 ) {
            skystone_primary = true;
        } else {
            skystone_primary = false;
        }
        boolean skystone_secondary;
        if ( h_average_secondary > 100 ) {
            skystone_secondary = true;
        } else {
            skystone_secondary = false;
        }

/*
        // difference of red and blue
        if (difference < 0.200) {
            skystone = true;
        } else {
            skystone = false;
        }
*/

        telemetry.addData("Method: skystone primary = ", skystone_primary);
        telemetry.addData("Method: skystone secondary = ", skystone_secondary);
        telemetry.update();
        sleep(100);

        boolean[] results = {skystone_primary, skystone_secondary};

        return results;
    }

    public int stoneInArm() {
    // Uses color sensors to check if stone captured
    // Return: 2 if SkyStone, 1 if Stone and 0 if nothing captured

        int counter = 0;
        int sample_loops = 20;  // Number of times to sample color in loop

        double h_total_primary = 0.0;
        double h_total_secondary = 0.0;
        double h_average_primary = 0.0;
        double h_average_secondary = 0.0;

        // values[Primary|Secondary] are a references to the respective hsvValues arrays.
        float[] hsvValuesPrimary = new float[3];
        final float valuesPrimary[] = hsvValuesPrimary;
        float[] hsvValuesSecondary = new float[3];
        final float valuesSecondary[] = hsvValuesSecondary;


        while (opModeIsActive() && counter <= sample_loops) {

            // Read the sensors
            // Primary sensor - middle of robot under stone arm
            NormalizedRGBA colorsPrimary = robot.colorSensorPrimary.getNormalizedColors();
            Color.colorToHSV(colorsPrimary.toColor(), hsvValuesPrimary);
            // Secondary sensor - Left of primary sensor by 2 1/2"
            NormalizedRGBA colorsSecondary = robot.colorSensorSecondary.getNormalizedColors();
            Color.colorToHSV(colorsSecondary.toColor(), hsvValuesSecondary);

            telemetry.addLine()
                    .addData("H", "%.3f", hsvValuesPrimary[0]);
            telemetry.addLine()
                    .addData("H2", "%.3f", hsvValuesSecondary[0]);

            // Running total of red and blue values
            h_total_primary = h_total_primary + hsvValuesPrimary[0];
            h_total_secondary = h_total_secondary + hsvValuesSecondary[0];

            counter++;
            telemetry.addData("Counter", counter);
            telemetry.update();
            sleep(10);
        }

        //Calculate red and blue average and then find difference
        h_average_primary = h_total_primary/counter;
        h_average_secondary = h_total_secondary/counter;
        telemetry.addData("** h_average_p = ", h_average_primary);
        telemetry.addData("** h_average_s = ", h_average_secondary);

        int return_value;

        if ( h_average_primary > 100 || h_average_secondary > 100 ) {
            return_value = 2;
        } else if ( h_average_primary > 60 || h_average_secondary > 60 ) {
            return_value = 1;
        } else {
            return_value = 0;
        }

        //skystone = false;
        telemetry.addData("Have Stone?", return_value);
        telemetry.update();
        sleep(100);

        return return_value;
    }
}
