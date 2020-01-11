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

@TeleOp(name = "TeleOpComp")

//@Disabled

public class TeleOpComp extends LinearOpMode {

    private TeleOpCompHWMain robot = new TeleOpCompHWMain();

    private double powerMultiplier = 1.0;

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

            telemetry.update();*/


            // Capstone delivery control - Uses Gamepad1
            //if ( gamepad1.y )  {
            if (gamepad1.y) {
                robot.capstoneServo.setPosition(0.85);
                telemetry.addData("CapstoneArm", "move down - all the way");
                telemetry.update();
            } else if (gamepad1.x) {
                robot.capstoneServo.setPosition(0.2);
                telemetry.addData("CapstoneArm", "move up");
                telemetry.update();
            } else if (gamepad1.b) {
                robot.capstoneServo.setPosition(0.7);
                telemetry.addData("CapstoneArm", "move down - partial");
                telemetry.update();
            }


            if (gamepad2.x) {
                // Toggle Stone Arm up and down using Gamepad2's X button
                if (robot.servoStoneArm.getPosition() < 0.7) {
                    robot.servoStoneArm.setPosition(1.0);
                } else {
                    robot.servoStoneArm.setPosition(0.47);
                }
                while (gamepad2.x && opModeIsActive()) {
                }
                sleep(100);
            } else if (gamepad2.y) {
                // Toggle foundation latch up and down using Gamepad2's Y button
                if (robot.servoFoundation.getPosition() > 0.3) {
                    robot.servoFoundation.setPosition(0.1);
                } else {
                    robot.servoFoundation.setPosition(0.65);
                }
                while (gamepad2.y && opModeIsActive()) {
                }
                sleep(100);
            }
            double intakePower = robot.intakeRight.getPower();
            if (gamepad2.a) {
                telemetry.addData("Button pressed...", "Gamepad 2 A");
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
                while ( gamepad2.a && opModeIsActive() ) { }
                sleep(100);

            }
        }
    }
}
