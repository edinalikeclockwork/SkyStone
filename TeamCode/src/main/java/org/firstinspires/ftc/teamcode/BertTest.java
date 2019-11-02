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
 *
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This OpMode executes a 2nd method of Mecanum Drive Teleop.
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


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        robot.lf.setDirection(DcMotor.Direction.FORWARD);
        robot.lr.setDirection(DcMotor.Direction.FORWARD);
        robot.rf.setDirection(DcMotor.Direction.REVERSE);
        robot.rr.setDirection(DcMotor.Direction.REVERSE);

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
                robot.servoFoundation.setPosition(-0.1);
            } else if (gamepad2.b) {
                robot.servoFoundation.setPosition(0.5);
            }
            if (gamepad2.x) {
                robot.servoStoneArm.setPosition(1.0);
            } else if (gamepad2.y) {
                robot.servoStoneArm.setPosition(0.35);
            }

            //robot.servoStoneArm.setPosition(0.5);

        }
    }
}
