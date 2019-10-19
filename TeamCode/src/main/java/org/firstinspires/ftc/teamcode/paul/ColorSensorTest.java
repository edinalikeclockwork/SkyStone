package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import android.graphics.Color;

/**
 * This file contains an minimal example of a Linear OpMode to test the REV color sensor
*/

@TeleOp(name="ColorSensorTest")
//@Disabled
public class McTeleop_1 extends LinearOpMode {

    private ColorSensorTest_HW robot = new ColorSensorTest_HW();
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

            // Show the elapsed game time and color sensor value.
            telemetry.addData("Status", "Run Time: " + runtime].toString());
            telemetry.addData("Color RGB", "red (%.2f), green (%.2f), blue (%.2f)", red, green, blue);
            telemetry.addData("Color", "left (%.2f), right (%.2f)", BleftPower, BrightPower);
            telemetry.update();
        }
    }
}
