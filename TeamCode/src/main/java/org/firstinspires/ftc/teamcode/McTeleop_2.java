package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This OpMode executes a 2nd method of Mecanum Drive Teleop.
 *
 */

// Based on Mecanum algorithm from Piece of Cake

@TeleOp(name="McTeleop_2")
//@Disabled
public class McTeleop_2 extends LinearOpMode {

    private McDriveTest_HW robot = new McDriveTest_HW();
    
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
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", FleftPower, FrightPower);
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", BleftPower, BrightPower);
            telemetry.update();*/
           //Hello
        }
    }
}