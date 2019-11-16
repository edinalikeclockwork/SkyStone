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

@Autonomous(name = "Move Foundation Test")

// @Disabled

public class AutoMoveFoundation extends LinearOpMode {

    /* Declare OpMode members. */

    //McDriveTest_HW robot = new McDriveTest_HW();
    private BertTestHWMain robot = new BertTestHWMain();


    private ElapsedTime runtime = new ElapsedTime();

    Orientation lastAngles = new Orientation();
    double globalAngle, power = .60, correction;

    // Create static variables
    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 0.705;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double DRIVE_SPEED = 0.6;
    static final double MOVE_FOUNDATION_SPEED = 0.3;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                          (WHEEL_DIAMETER_INCHES * 3.14159);
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

        sleep(500);     // pause for telemetry msg to be seen

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Version", "0.2");
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.lf.getCurrentPosition(),
                robot.rf.getCurrentPosition());
        telemetry.update();

        /**
        // TODO: Init gyro - should set to zero (? validate this)
        telemetry.addData("Mode", "calibrating...");
        telemetry.update();

        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !robot.imu.isGyroCalibrated()) {
            sleep(50);
            idle();
        }
        **/
        telemetry.addData("Mode", "waiting for start");
        // telemetry.addData("imu calib status", robot.imu.getCalibrationStatus().toString());
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
        
        // ACTION 0:
        // Drive in a 12 inch square
        /* 
        encoderDrive(DRIVE_SPEED, "forward", 12, 6.0);
        sleep(1000);
        encoderDrive(DRIVE_SPEED, "right", 12, 2.0);
        sleep(1000);
        encoderDrive(DRIVE_SPEED, "backward", 12, 2.0);
        sleep(1000);
        encoderDrive(DRIVE_SPEED, "left", 12, 2.0);
        sleep(1000);
        */

        // ACTION 1:
        // Drive: Start moving to foundation
        encoderDrive(DRIVE_SPEED, "right", 16, 4);
        sleep(100);

        // ACTION 2:
        // Drive: Center on foundation
        encoderDrive(DRIVE_SPEED, "forward", 11, 4);
        sleep(100);

        // ACTION 3:
        // Drive: Drive to foundation
        encoderDrive(DRIVE_SPEED, "right", 16.5, 4);
        sleep(250);

        // ACTION 4:
        // Lower foundation latch
        robot.servoFoundation.setPosition(-0.1);
        sleep(100);

        // ACTION 5:
        // Drive: Pull foundation to wall
        encoderDrive(MOVE_FOUNDATION_SPEED, "left", 40, 100);
        sleep(100);

        // ACTION 6:
        // Raise foundation latch
        robot.servoFoundation.setPosition(0.5);
        sleep(100);

        // ACTION 7:
        // Drive: Park on line under bridge
        encoderDrive(DRIVE_SPEED, "backward", 56, 6);
        sleep(100);

        // End autonomous actions here

        telemetry.addData("Autonomous Path", "Complete");
        telemetry.update();

        sleep(100);
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

//sleep(15000);

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

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.lf.isBusy() || robot.rf.isBusy() ||
                     robot.lr.isBusy() || robot.rr.isBusy() )) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftFrontTarget,  newRightFrontTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.lf.getCurrentPosition(),
                        robot.rf.getCurrentPosition());
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
    
    public void MoveSideways (double power, String direction, int time)
    {
        if (direction == "left")
        {
            robot.lf.setPower(-power);
            robot.lr.setPower(power);
            robot.rf.setPower(power);
            robot.rr.setPower(-power);
            
            sleep(time);
        }
        
        else if (direction == "right")
        {
            robot.lf.setPower(power);
            robot.lr.setPower(-power);
            robot.rf.setPower(-power);
            robot.rr.setPower(power);
            
            sleep(time);
        }
    }
    
    public void MoveFwdBack (double power, String direction, int time)
    {
        if (direction == "forward")
        {
            robot.lf.setPower(power);
            robot.lr.setPower(power);
            robot.rf.setPower(power);
            robot.rr.setPower(power);
            
            sleep(time);
        }
        
        else if (direction == "backward")
        {
            robot.lf.setPower(-power);
            robot.lr.setPower(-power);
            robot.rf.setPower(-power);
            robot.rr.setPower(-power);
            
            sleep(time);
       }
    }
}
