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
 * - 11-21-2019: Added additional flapper motor gamepad2 controls
 * - 11-23-2019: Added servo for delivering capstone
 *
 * This file contains a minimal TeleOp linear "OpMode".  This OpMode is for test
 * driving the the robot with a mecanum drive and also test the various servos
 * and sensors.
 *
 */
 
// Based on Mecanum algorithm from Piece of Cake

@TeleOp(name="Bert Teleop")

//@Disabled

public class BertTest extends LinearOpMode {

    private BertTestHWMain robot = new BertTestHWMain();
    
    private double powerMultipler = 1.0;
    
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
            if ( gamepad2.a ) {
                robot.dumpRamp.setPosition(0.3);
                telemetry.addData("dumpRamp", "tip up");
                telemetry.update();
            } else if ( gamepad2.b ) {
                robot.dumpRamp.setPosition(0.44);
                telemetry.addData("dumpRamp", "move down");
                telemetry.update();
            } 
            
            // Capstone delivery control - Uses Gamepad1
            //if ( gamepad1.y )  {
            if ( gamepad1.y )  {
                robot.capstoneServo.setPosition(0.85);
                telemetry.addData("CapstoneArm", "move down - all the way");
                telemetry.update();
            } else if ( gamepad1.x ) {
                robot.capstoneServo.setPosition(0.2);
                telemetry.addData("CapstoneArm", "move up");
                telemetry.update();
            } else if ( gamepad1.b ) {
                robot.capstoneServo.setPosition(0.7);
                telemetry.addData("CapstoneArm", "move down - partial");
                telemetry.update();
            } 
            
            

            if ( gamepad2.x ) {
                // Toggle Stone Arm up and down using Gamepad2's X button
                if ( robot.servoStoneArm.getPosition() < 0.7 ) {
                    robot.servoStoneArm.setPosition(1.0);
                } else {
                    robot.servoStoneArm.setPosition(0.47);
                }
                while ( gamepad2.x && opModeIsActive() ) { }
                sleep(100);
            } else if ( gamepad2.y ) {
                // Toggle foundation latch up and down using Gamepad2's Y button
                if ( robot.servoFoundation.getPosition() > 0.3 ) {
                    robot.servoFoundation.setPosition(0.1);
                } else {
                    robot.servoFoundation.setPosition(0.65);
                }
                while ( gamepad2.y && opModeIsActive() ) { }
                sleep(100);
            }

            // Intake ramp uses two opposing servos.
            // To move ramp up the right servo value increases and
            // the left decreases.  Opposite for down.  Increase/decrease
            // each value the (roughly) same amount to keep the ramp level.
            if (gamepad2.right_bumper && true) {
                // Lift ramp
                robot.intakeRampRight.setPosition(0.97);
                robot.intakeRampLeft.setPosition(0.06);
                double rampPositionR = robot.intakeRampRight.getPosition();
                double rampPositionL = robot.intakeRampLeft.getPosition();
                robot.flapper.setPower(0);
                robot.lowerIntakeRight.setPosition(0.49);
                robot.lowerIntakeLeft.setPosition(0.5);
                robot.upperIntakeRight.setPosition(0.51);
                robot.upperIntakeLeft.setPosition(0.51);
                telemetry.addData("Intake Ramp: ", "Up - wheels off");
                telemetry.addData("Current Ramp Position", "%7f, %7f", rampPositionL, rampPositionR);
                telemetry.update();
            } else if (gamepad2.left_bumper && true) {
                // Lower ramp
                robot.intakeRampRight.setPosition(0.38);
                robot.intakeRampLeft.setPosition(0.62);
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
                robot.flapper.setPower(FLAPPER_SPEED);
                int i = 0;
                while ( false && i < 5000 && opModeIsActive() ) {
                int flapperPosition = robot.flapper.getCurrentPosition();
                double flapperPower = robot.flapper.getPower();
                double flapperRevs = flapperPosition / COUNTS_PER_FLAPPER_REV;
                telemetry.addData("Flapper Ticks", "%7d", flapperPosition);
                telemetry.addData("Flapper Revs", "%7f", flapperRevs);
                telemetry.addData("Flapper Power", "%7f", flapperPower);
                telemetry.addData("Counter", "%7d", i);
                telemetry.update();
                i++;
                }
            }

            if ( gamepad2.left_stick_button ) {
                // Toggle intake ramp wheels and flapper on/off
                if ( robot.lowerIntakeLeft.getPosition() > 0.6 ) {
                    robot.lowerIntakeRight.setPosition(0.49);
                    robot.lowerIntakeLeft.setPosition(0.5);
                    robot.upperIntakeRight.setPosition(0.51);
                    robot.upperIntakeLeft.setPosition(0.51);
                    robot.flapper.setPower(0);
                } else {
                    robot.lowerIntakeRight.setPosition(-1.0);
                    robot.lowerIntakeLeft.setPosition(1.0);
                    robot.upperIntakeRight.setPosition(-1.0);
                    robot.upperIntakeLeft.setPosition(1.0);
                    robot.flapper.setPower(FLAPPER_SPEED);
                }
                while ( gamepad2.left_stick_button && opModeIsActive()) { }
                sleep(50);
            } else if ( gamepad2.right_stick_button ) {
                // Toggle intake ramp servos on/off
                double intakeWheelPosition = robot.lowerIntakeLeft.getPosition();
                if ( intakeWheelPosition > 0.9 ) {
                    // Left wheel running forward, switch to reverse
                    robot.lowerIntakeRight.setPosition(1.0);
                    robot.lowerIntakeLeft.setPosition(-1.0);
                    robot.upperIntakeRight.setPosition(1.0);
                    robot.upperIntakeLeft.setPosition(-1.0);
                } else if ( intakeWheelPosition > -0.9 ) {
                    // Left wheel running in reverse, switch to forward
                    robot.lowerIntakeRight.setPosition(-1.0);
                    robot.lowerIntakeLeft.setPosition(1.0);
                    robot.upperIntakeRight.setPosition(-1.0);
                    robot.upperIntakeLeft.setPosition(1.0);
                }
                sleep(100);
                while ( gamepad2.left_stick_button && opModeIsActive() ) { }
                sleep(50);
            }
 
            // DPad Up/Down buttons control increment ramp movement
            double rampPositionR = robot.intakeRampRight.getPosition();
            double rampPositionL = robot.intakeRampRight.getPosition();
            // String rampPosition = (robot.intakeRampRight.getPosition() < 0.7) ? "down" : "up";
            double newRampPositionR;
            double newRampPositionL;
            if (gamepad2.dpad_up && rampPositionR < 0.6) {
                newRampPositionR = ( rampPositionR + 0.02 ) > 1 ? 1 : rampPositionR + 0.02;
                newRampPositionL = ( rampPositionL - 0.02 ) < 0 ? 0 : rampPositionL - 0.02;
                robot.intakeRampRight.setPosition(newRampPositionR);
                robot.intakeRampLeft.setPosition(newRampPositionL);
                sleep(50);
            } else if (gamepad2.dpad_down) {
                //newRampPositionR = rampPositionR - 0.02;
                //newRampPositionL = rampPositionL + 0.02;
                newRampPositionR = ( rampPositionR - 0.02 ) < 0 ? 0 : rampPositionR - 0.02;
                newRampPositionL = ( rampPositionL + 0.02 ) > 1 ? 1 : rampPositionL + 0.02;
                robot.intakeRampRight.setPosition(newRampPositionR);
                robot.intakeRampLeft.setPosition(newRampPositionL);
                sleep(50);
            }
            
            // Turn on/off flapper motor - DISABLED, see if clause below this one
            // Use a negative power to turn on with correct rotation
            /*
            double flapperCurrentPower = robot.flapper.getPower();
            double rampPosition = robot.intakeRampRight.getPosition();
            if (false && gamepad2.dpad_right && rampPosition < 0.6 && flapperCurrentPower > -0.3) {
                robot.flapper.setPower(-0.6);
                telemetry.addData("[IF] DPad_Right Pressed", "%6b", gamepad2.dpad_right);
                telemetry.addData("Postions:", "Ramp (r): %7f, Flapper: %7f", rampPosition, flapperCurrentPower);
                telemetry.update();
                while ( gamepad2.dpad_right && opModeIsActive() ) { }
            } else if ( false && gamepad2.dpad_right ) {
                // Attempt to stop flapper in known position, not needed
                if ( false ) {
                int flapperPosition;
                double flapperRevs = flapperPosition / COUNTS_PER_FLAPPER_REV;
                double ticks;
                do {
                    flapperPosition = robot.flapper.getCurrentPosition();
                    if ( flapperPosition != 0 ) {
                        flapperRevs = flapperPosition / COUNTS_PER_FLAPPER_REV;
                        ticks = flapperPosition - (flapperRevs * COUNTS_PER_FLAPPER_REV);
                    } else {
                        ticks = 0;
                    }
                    flapperRevs = ( flapperPosition != 0 ) ? flapperPosition / COUNTS_PER_FLAPPER_REV : 0;
                    ticks = ( flapperPosition != 0 ) ? 288 % (flapperPosition/288) : 0;
                    telemetry.addData("Data::  ", "Flapper Pos: %7d, Ticks: %7f", flapperPosition, ticks);
                    telemetry.update();
                } while (ticks > 30 && ticks < 258);
                }
                robot.flapper.setPower(0);
                telemetry.addData("[ELSE] DPad_Right Pressed", "%6b", gamepad2.dpad_right);
                telemetry.addData("Postions:", "Ramp (r): %7f, Flapper: %7f", rampPosition, flapperCurrentPower);
                telemetry.update();
                while ( gamepad2.dpad_right && opModeIsActive() ) { }
                sleep(50);
            }
            */
            
            // Turn on/off flapper motor
            // Use a negative power to turn on with correct rotation
            double flapperCurrentPower = robot.flapper.getPower();
            if ( gamepad2.dpad_right ) {
                double flap_speed = ( flapperCurrentPower == 0.0 ) ? (FLAPPER_SPEED * 0.75) : 0.0;
                robot.flapper.setPower(flap_speed); // Turn on flapper with half power (speed)
                while ( gamepad2.dpad_right && opModeIsActive() ) { }
                telemetry.addData("DPad_Right Pressed", "%6b", gamepad2.dpad_right);
                telemetry.addData("Postions:", "flap_speed: %7f, current power: %7f", flap_speed, flapperCurrentPower);
                telemetry.update();
            }
        }
    }
}
