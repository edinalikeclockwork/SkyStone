package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * TeleOp OpMode to test features on B.E.R.T.
 * <p>
 * - 10-26-2019: Added Mecanum drive (method 2)
 * - 10-26-2019: Added foundation servo
 * - 10-29-2019: Added stone arm servo
 * - 11-10-2019: Added servos for ramps and intake ramp wheels
 * - 11-21-2019: Added additional flapper motor gamepad2 controls
 * - 11-23-2019: Added servo for delivering capstone
 * <p>
 * This file contains a minimal TeleOp linear "OpMode".  This OpMode is for test
 * driving the the robot with a mecanum drive and also test the various servos
 * and sensors.
 */

// Based on Mecanum algorithm from Piece of Cake

@TeleOp(name = "Intake Test")

//@Disabled

public class IntakeTest extends LinearOpMode {

    private IntakeHWMain robot = new IntakeHWMain();

    private double powerMultiplier = 1.0;

    static final int COUNTS_PER_FLAPPER_REV = 288;    // eg: REV Core Hex Motor Encoder
    static final double FLAPPER_SPEED = -0.6;

    @Override

    public void runOpMode() {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        ElapsedTime runtime = new ElapsedTime();
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
        /*
            ** McTeleop_1 joystick controls

            // Setup a variable for each drive wheel to save power level for telemetry
            //Forward/Backward
            robot.lf.setPower(gamepad1.left_stick_y);
            robot.lr.setPower(gamepad1.left_stick_y);
            robot.rf.setPower(gamepad1.right_stick_y);
            robot.rr.setPower(gamepad1.right_stick_y);

            //Left
            robot.lf.setPower(gamepad1.left_trigger);
            robot.lr.setPower(-gamepad1.left_trigger);
            robot.rf.setPower(-gamepad1.left_trigger);
            robot.rr.setPower(gamepad1.left_trigger);
            //Right
            robot.lf.setPower(-gamepad1.right_trigger);
            robot.lr.setPower(gamepad1.right_trigger);
            robot.rf.setPower(gamepad1.right_trigger);
            robot.rr.setPower(-gamepad1.right_trigger);
        */

            final double x = Math.pow(gamepad1.left_stick_x, 3.0);
            final double y = Math.pow(-gamepad1.left_stick_y, 3.0);

            final double rotation = Math.pow(-gamepad1.right_stick_x, 3.0);
            final double direction = Math.atan2(x, y);
            final double speed = Math.min(1.0, Math.sqrt(x * x + y * y));

            final double lf_power = speed * Math.sin(direction + Math.PI / 4.0) + rotation;
            final double rf_power = speed * Math.cos(direction + Math.PI / 4.0) - rotation;
            final double lr_power = speed * Math.cos(direction + Math.PI / 4.0) + rotation;
            final double rr_power = speed * Math.sin(direction + Math.PI / 4.0) - rotation;

            robot.lf.setPower(-lf_power * powerMultiplier);
            robot.rf.setPower(-rf_power * powerMultiplier);
            robot.lr.setPower(-lr_power * powerMultiplier);
            robot.rr.setPower(-rr_power * powerMultiplier);

            // Show the elapsed game time and wheel power.
            /* telemetry.addData("Status", "Run Time: " + runtime].toString());
            telemetry.addData("Motors", "left-front (%.2f), (-lf_power * powerMultipler));
            telemetry.addData("Motors", "left-rear (%.2f), (-lr_power * powerMultipler)),
            telemetry.addData("Motors", "right-front (%.2f), (-rf_power * powerMultipler)),
            telemetry.addData("Motors", "right-rear (%.2f), (-rr_power * powerMultipler)),

            telemetry.update();
            */

            telemetry.addData("Running...", "Wait for cmd");
            telemetry.update();
                // Turn on/off flapper motor
                // Use a negative power to turn on with correct rotation
                double intakePower = robot.intakeRight.getPower();
                if (gamepad1.a) {
                    telemetry.addData("Button pressed...", "Gamepad 1 A");
                    telemetry.update();

                    // Toggle drive motor power
                    if (intakePower == 0.0) {
                        intakePower = 0.5;

                        robot.intakeLeft.setPower(-intakePower);
                        robot.intakeRight.setPower(intakePower);
                    } else {

                        intakePower = 0.0;
                        robot.intakeLeft.setPower(intakePower);
                        robot.intakeRight.setPower(intakePower);
                    }
                    while (gamepad1.a && opModeIsActive()) {
                    }
                    sleep(100);

                }
            }
        }
    }
