package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.Set;
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

    private ElapsedTime runtime = new ElapsedTime();

    private TeleOpCompHWMain robot = new TeleOpCompHWMain();

    // Drive motor powers
    private double fullDrivePower = 1.0;
    private double slowDrivePower = 0.5;
    private double superSlowDrivePower = 0.3;
    private double drivePower = fullDrivePower;

    private double intakePower = 0.5;
    private double intakePowerReverse = -0.5;

    private double liftPower = 0.85;    // liftMotor power
    private double liftDistance = 150;   // liftMotor encoder ticks to move

    @Override

    public void runOpMode() {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        boolean topLimitHit = false;
        boolean bottomLimitHit = false;

        ElapsedTime runtime = new ElapsedTime();
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Mecanum wheel drive power
            // Move forward, back, left, right: Left stick
            // Turn chassis left and right: Right stick (x-axis only)
            final double x = Math.pow(gamepad1.left_stick_x, 3.0);
            final double y = Math.pow(-gamepad1.left_stick_y, 3.0);

            final double rotation = Math.pow(gamepad1.right_stick_x, 3.0);
            final double direction = Math.atan2(x, y);
            final double speed = Math.min(1.0, Math.sqrt(x * x + y * y));

            final double lf_power = speed * Math.sin(direction + Math.PI / 4.0) + rotation;
            final double rf_power = speed * Math.cos(direction + Math.PI / 4.0) - rotation;
            final double lr_power = speed * Math.cos(direction + Math.PI / 4.0) + rotation;
            final double rr_power = speed * Math.sin(direction + Math.PI / 4.0) - rotation;

            robot.lf.setPower(-lf_power * drivePower);
            robot.rf.setPower(-rf_power * drivePower);
            robot.lr.setPower(-lr_power * drivePower);
            robot.rr.setPower(-rr_power * drivePower);
            String drivePowerWord = drivePower == fullDrivePower ? "full" : "slow";
            telemetry.addData("Drive power", drivePowerWord);
            telemetry.update();

            // Show the elapsed game time and wheel power.
            /* telemetry.addData("Status", "Run Time: " + runtime].toString());
            telemetry.addData("Motors", "left-front (%.2f), (-lf_power * powerMultipler));
            telemetry.addData("Motors", "left-rear (%.2f), (-lr_power * powerMultipler)),
            telemetry.addData("Motors", "right-front (%.2f), (-rf_power * powerMultipler)),
            telemetry.addData("Motors", "right-rear (%.2f), (-rr_power * powerMultipler)),
            telemetry.update();*/


            // Capstone delivery control - Uses Gamepad1
             if (gamepad1.y) {
                robot.capstoneServo.setPosition(0.85);
                telemetry.addData("CapstoneArm", "move down - all the way");
                while (gamepad1.y && opModeIsActive()) { }
                sleep(50);
            } else if (gamepad1.x) {
                robot.capstoneServo.setPosition(0.2);
                telemetry.addData("CapstoneArm", "move up");
                while (gamepad1.x && opModeIsActive()) { }
                sleep(50);
            } else if (gamepad1.b) {
                robot.capstoneServo.setPosition(0.7);
                telemetry.addData("CapstoneArm", "move down - partial");
                while (gamepad1.b && opModeIsActive()) { }
                sleep(50);
            }

            // Disabled!  Raise/Lower lift arm
            boolean notAtLimit = robot.magLimit.getState();
            if (false && gamepad2.left_trigger > 0.0 || gamepad2.right_trigger > 0.0) {
                while ((gamepad2.left_trigger > 0.0 || gamepad2.right_trigger > 0.0)
                        && notAtLimit && opModeIsActive()) {
                    int moveDirection = (gamepad2.left_trigger > 0.0) ? -1 : 1;
                    notAtLimit = robot.magLimit.getState();
                    robot.liftMotor.setPower(liftPower * moveDirection);
                    telemetry.addData("Limit switch", notAtLimit);
                    telemetry.update();
                }
                robot.liftMotor.setPower(0);
            }

            // Raise/Lower lift arm
            if ( gamepad2.right_stick_y != 0.0 ) {
                notAtLimit = robot.magLimit.getState();
                telemetry.addData("Right Stick Y", gamepad2.right_stick_y);
                telemetry.addData("Limit switch", notAtLimit);
                telemetry.addData("Top Limit Hit", topLimitHit);
                telemetry.addData("Btm Limit Hit", bottomLimitHit);

                if ( Math.abs(gamepad2.right_stick_y) != 1.0 ){
                    // Stop lift motor when stick zero or near zero
                    robot.liftMotor.setPower(0);
                    telemetry.addData("Stop Moving Arm - Stick", 0);
                } else if ( gamepad2.right_stick_y == -1.0 ) {
                    // Raise arm
                    if ( notAtLimit && !topLimitHit ) {
                        robot.liftMotor.setPower(liftPower);
                        if (robot.magLimit.getState()) {
                            bottomLimitHit = false;
                        }
                        telemetry.addData("Raise Arm", liftPower);
                    } else {
                        topLimitHit = true;
                        robot.liftMotor.setPower(0);
                        telemetry.addData("Stop Raising Arm", 0);
                    }
                } else if ( gamepad2.right_stick_y == 1.0 ){
                    // Lower arm
                    robot.liftMotor.setPower(liftPower * -1);
/*                    if ( notAtLimit ) {
                        // Use negative power to go down (reverse motor)
                        robot.liftMotor.setPower(liftPower * -1);
                        if (robot.magLimit.getState()) {
                            topLimitHit = false;
                        }
                        telemetry.addData("Lower Arm", liftPower * -1);
                    } else {
                        bottomLimitHit = true;
                        robot.liftMotor.setPower(0);
                        telemetry.addData("Stop Lowering Arm", 0);
                    }
*/
                }
                telemetry.update();
            }


            // Toggle robot drive speed between full and slow
            if (gamepad1.a) {
                if ( drivePower == fullDrivePower ) {
                    drivePower = slowDrivePower;
                    // telemetry.addData("Drive power", "** slow **");
                } else if ( drivePower == slowDrivePower || drivePower == superSlowDrivePower ) {
                    drivePower = fullDrivePower;
                    // telemetry.addData("Drive power", "full");
                }
                while (gamepad1.a && opModeIsActive()) { }
                sleep(50);
            } else if (gamepad1.y) {
                if ( drivePower != 0.0 && drivePower != superSlowDrivePower ) {
                    drivePower = superSlowDrivePower;
                    // telemetry.addData("Drive power", "** super slow **");
                }
                while (gamepad1.y && opModeIsActive()) { }
                sleep(50);
            }

            if (gamepad2.x) {
                // Toggle Stone Arm up and down using Gamepad2's X button
                if (robot.servoStoneArm.getPosition() < 0.7) {
                    robot.servoStoneArm.setPosition(1.0);
                    telemetry.addData("Move stone arm", "down");
                } else {
                    robot.servoStoneArm.setPosition(0.0);
                    telemetry.addData("Move stone arm", "up");
                }
                while (gamepad2.x && opModeIsActive()) { }
                sleep(50);
            } else if (gamepad2.y) {
                // Toggle foundation latch up and down using Gamepad2's Y button
                if (robot.servoFoundation.getPosition() > 0.3) {
                    robot.servoFoundation.setPosition(0.1);
                    telemetry.addData("Move foundation latch", "down");
                } else {
                    robot.servoFoundation.setPosition(0.65);
                    telemetry.addData("Move foundation latch", "up");
                }
                while (gamepad2.y && opModeIsActive()) { }
                sleep(50);
            } else if (gamepad2.dpad_up){
                telemetry.addData("Return stone hand", "of robot");
                robot.handServo.setPosition(0.5); // Close hand
                // raise lift motor
                // liftStoneArm(liftPower, "up", liftDistance, 4.0);
                robot.pivotServo.setPosition(1);
                while (gamepad2.y && opModeIsActive()) { }
                sleep(50);
                // TODO: Need to also return hand to inside robot position
                telemetry.update();
            } else if (gamepad2.dpad_down){
                telemetry.addData("Lift stone out", "to robot");
                robot.handServo.setPosition(1);
                sleep(350);
                // raise lift motor
                //lowerStoneArm(4.0);
                robot.pivotServo.setPosition(0);
                while (gamepad2.y && opModeIsActive()) { }
                sleep(50);
                // TODO: Need to also return hand to inside robot position
                telemetry.update();
            }

            // Control intake wheels
            double currentIntakePower = robot.intakeRight.getPower();
            if (gamepad2.a) {
                telemetry.addData("Button pressed...", "Gamepad 2 A");

                // Toggle intake motor power
                if (currentIntakePower == 0.0) {
                    // Tunn on intake wheels
                    robot.intakeLeft.setPower(intakePower);
                    robot.intakeRight.setPower(intakePower);
                    telemetry.addData("Intake wheels", "on");
                } else {
                    // Tunn off intake wheels
                    double stopPower = 0.0;
                    robot.intakeLeft.setPower(stopPower);
                    robot.intakeRight.setPower(stopPower);
                    telemetry.addData("Intake wheels", "off");
                }
                while (gamepad2.a && opModeIsActive()) { }
                sleep(50);

            }  else if (gamepad2.right_bumper) {
                //telemetry.addData("Button pressed...", "Gamepad 2 right_bumper");
                if ( currentIntakePower > 0.4 && currentIntakePower < 0.6 ) {
                    currentIntakePower = 0.5;
                } else if ( currentIntakePower < -0.4 && currentIntakePower > -0.6 ) {
                    currentIntakePower = -0.5;
                }
                // Reverse direction of intake wheels
                if (currentIntakePower == intakePowerReverse) {
                    robot.intakeLeft.setPower(intakePower);
                    robot.intakeRight.setPower(intakePower);
                    telemetry.addData("Intake wheels", "forward spin");
                } else if (intakePower == intakePower) {
                    robot.intakeLeft.setPower(intakePowerReverse);
                    robot.intakeRight.setPower(intakePowerReverse);
                    telemetry.addData("Intake wheels", "reverse spin");
                }
                telemetry.addData("Intake power", currentIntakePower);
                telemetry.update();
                while ( gamepad2.right_bumper && opModeIsActive() ) { }
                sleep(50);

            // Toggle stone hand open/closed
            } else if (gamepad2.b) {
                double handPosition = robot.handServo.getPosition();
                if ( handPosition == 1.0 ) {
                    robot.handServo.setPosition(0.5);
                    telemetry.addData("Hand Position", "open");
                } else {
                    robot.handServo.setPosition(1.0);
                    telemetry.addData("Hand Position", "closed");
                }
                while (gamepad2.b && opModeIsActive()) { }
                sleep(50);
            }
            //telemetry.update();
        }
    }

    public void liftStoneArm(double speed, String direction,
                        double ticks, double timeoutS) {

        int liftTarget;

        int liftDirection = (direction == "up") ? 1 : -1;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            liftTarget = robot.liftMotor.getCurrentPosition() + (int) (ticks * liftDirection);
            robot.liftMotor.setTargetPosition(liftTarget);

            // Turn On RUN_TO_POSITION
            robot.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();

            robot.liftMotor.setPower(Math.abs(speed));

            while (opModeIsActive() && (runtime.seconds() < timeoutS)
                                    && (robot.liftMotor.isBusy()) ) {

                // Display it for the driver.
                telemetry.addData("Lift Arm", "Running to %7d", liftTarget);
                telemetry.addData("Current", "Running at %7d", robot.liftMotor.getCurrentPosition());
                telemetry.addData("isBusy:", "liftMotor: %3b", robot.liftMotor.isBusy());
                telemetry.addData("Timers: ", "Timeout: %4f, Timer: %4f", timeoutS, runtime.seconds());
                telemetry.update();
            }

            // Stop lift motor;
            robot.liftMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void lowerStoneArm(double timeoutS) {
        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            // reset the timeout time and start motion.
            runtime.reset();

            robot.liftMotor.setPower(-liftPower);
            boolean notAtLimit = robot.magLimit.getState();
            while ( opModeIsActive() && (runtime.seconds() < timeoutS) && notAtLimit) {
                telemetry.addData("Lift Arm", "Moving down");
                telemetry.addData("Limit Switch", notAtLimit);
                telemetry.addData("Timers: ", "Timeout: %4f, Timer: %4f", timeoutS, runtime.seconds());
                telemetry.update();
            }
            robot.liftMotor.setPower(0);
        }
    }
}
