package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * TeleOp OpMode to test features on B.E.R.T.
 * 
 * - 10-26-2019: Added Mecanum drive (method 2)
 * - 10-26-2019: Added foundation servo
 * - 10-29-2019: Added stone arm servo
 * - 11-10-2019: Added servos for ramps and intake ramp wheels
 *
 * This file contains a minimal TeleOp linear "OpMode".  This OpMode is for test
 * driving the the robot with a mecanum drive and also test the various servos
 * and sensors.
 *
 */
 
// Based on Mecanum algorithm from Piece of Cake

@TeleOp(name="Bert Test")

//@Disabled

public class BertTest extends LinearOpMode {

    private BertTestHWMain robot = new BertTestHWMain();
    
    private double powerMultipler = 1.0;

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

            robot.lf.setPower(-lf_power * powerMultipler);
            robot.rf.setPower(-rf_power * powerMultipler);
            robot.lr.setPower(-lr_power * powerMultipler);
            robot.rr.setPower(-rr_power * powerMultipler);

            // Show the elapsed game time and wheel power.
            /* telemetry.addData("Status", "Run Time: " + runtime].toString());
            telemetry.addData("Motors", "left-front (%.2f), (-lf_power * powerMultipler));
            telemetry.addData("Motors", "left-rear (%.2f), (-lr_power * powerMultipler)),
            telemetry.addData("Motors", "right-front (%.2f), (-rf_power * powerMultipler)),
            telemetry.addData("Motors", "right-rear (%.2f), (-rr_power * powerMultipler)),

            telemetry.update();*/

            // Foundation Servo Controls
            if (gamepad2.a) {
                robot.dumpRamp.setPosition(0.3);
                telemetry.addData("dumpRamp", "tip up");
                telemetry.update();
            } else if (gamepad2.b) {
                robot.dumpRamp.setPosition(0.59);
                telemetry.addData("dumpRamp", "move down");
                telemetry.update();
            }

            if ( gamepad2.x ) {
                // Toggle Stone Arm up and down using Gamepad2's X button
                if ( robot.servoStoneArm.getPosition() < 0.7 ) {
                    robot.servoStoneArm.setPosition(1.0);
                } else {
                    robot.servoStoneArm.setPosition(0.4);
                }
                sleep(100);
            } else if ( gamepad2.y ) {
                // Toggle foundation latch up and down using Gamepad2's Y button
                if ( robot.servoFoundation.getPosition() > 0.3 ) {
                    robot.servoFoundation.setPosition(0.1);
                } else {
                    robot.servoFoundation.setPosition(0.5);
                }
                sleep(100);
            }

            // Intake ramp uses two opposing servos.
            // To move ramp up the right servo value increases and
            // the left decreases.  Opposite for down.  Increase/decrease
            // each value the (roughly) same amount to keep the ramp level.
            if (gamepad2.right_bumper && true) {
                robot.intakeRampRight.setPosition(0.97);
                robot.intakeRampLeft.setPosition(0.06);
                double rampPositionR = robot.intakeRampRight.getPosition();
                double rampPositionL = robot.intakeRampLeft.getPosition();
                robot.lowerIntakeRight.setPosition(0.49);
                robot.lowerIntakeLeft.setPosition(0.5);
                robot.upperIntakeRight.setPosition(0.51);
                robot.upperIntakeLeft.setPosition(0.51);
                telemetry.addData("Intake Ramp: ", "Up - wheels off");
                telemetry.addData("Current Ramp Position", "%7f, %7f", rampPositionL, rampPositionR);
                telemetry.update();
            } else if (gamepad2.left_bumper && true) {
                robot.intakeRampRight.setPosition(0.41);
                robot.intakeRampLeft.setPosition(0.59);
                double rampPositionR = robot.intakeRampRight.getPosition();
                double rampPositionL = robot.intakeRampLeft.getPosition();
                telemetry.addData("Intake Ramp: ", "Down - wheels on");
                telemetry.addData("Current Ramp Position", "%7f, %7f", rampPositionL, rampPositionR);
                telemetry.update();
                sleep(250);
                robot.lowerIntakeRight.setPosition(-1.0);
                robot.lowerIntakeLeft.setPosition(1.0);
                robot.upperIntakeRight.setPosition(-1.0);
                robot.upperIntakeLeft.setPosition(1.0);
            }

            if ( gamepad2.left_stick_button ) {
                // Toggle intake ramp servos on/off
                if ( robot.lowerIntakeLeft.getPosition() > 0.6 ) {
                    robot.lowerIntakeRight.setPosition(0.49);
                    robot.lowerIntakeLeft.setPosition(0.5);
                    robot.upperIntakeRight.setPosition(0.51);
                    robot.upperIntakeLeft.setPosition(0.51);
                } else {
                    robot.lowerIntakeRight.setPosition(-1.0);
                    robot.lowerIntakeLeft.setPosition(1.0);
                    robot.upperIntakeRight.setPosition(-1.0);
                    robot.upperIntakeLeft.setPosition(1.0);
                }
                sleep(50);
            }
           
            double rampPosition = robot.intakeRampRight.getPosition(); // Not currently used
            double newRampPositionR;
            double newRampPositionL;
            if (gamepad2.dpad_up && rampPosition < 0.6) {
                newRampPositionR = rampPosition + 0.02;
                newRampPositionL = rampPosition - 0.02;
                robot.intakeRampRight.setPosition(newRampPositionR);
                robot.intakeRampLeft.setPosition(newRampPositionL);
                sleep(50);
            } else if (gamepad2.dpad_down) {
                newRampPositionR = rampPosition - 0.02;
                newRampPositionL = rampPosition + 0.02;
                robot.intakeRampRight.setPosition(newRampPositionR);
                robot.intakeRampLeft.setPosition(newRampPositionL);
                sleep(50);
            }

            //robot.servoStoneArm.setPosition(0.4);

        }
    }
}