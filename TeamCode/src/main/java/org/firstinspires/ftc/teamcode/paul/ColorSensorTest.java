package org.firstinspires.ftc.teamcode.paul;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import android.graphics.Color;

/**
 * This file contains an minimal example of a Linear OpMode to test the REV color sensor
 */

//@TeleOp(name="ColorSensorTest")
@TeleOp(name = "Color Sensor Test", group = "Test Code")

//@Disabled
public class ColorSensorTest extends LinearOpMode {

    //public DigitalChannel digitalTouch;
    //public DistanceSensor distanceSensor;
    private ColorSensor colorSensor;

    private ElapsedTime runtime = new ElapsedTime();

    @Override

    public void runOpMode() {

        colorSensor = hardwareMap.colorSensor.get("color");
        colorSensor.enableLed(true);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        ElapsedTime runtime = new ElapsedTime();
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        /*
        double COLOR_SCALE_FACTOR = 1;
        float hsvValues[] = {0F, 0F, 0F};
        final float values[] = hsvValues;
        */

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            float[] hsv = {0F, 0F, 0F};

            Color.RGBToHSV(colorSensor.red() * 255, colorSensor.green() * 255, colorSensor.blue() * 255, hsv);

            if (hsv[2] < 10) telemetry.addData("Color", "Unknown");
            else if ((hsv[0] < 30 || hsv[0] > 340) && hsv[1] > .2)
                telemetry.addData("Color", "Red");
            else if ((hsv[0] > 140 && hsv[0] < 280) && hsv[1] > .2)
                telemetry.addData("Color", "Blue");
            else telemetry.addData("Color", "Unknown");

            // Show the elapsed game time and color sensor value.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Color RGB", "red (%.2f), green (%.2f), blue (%.2f)", hsv[0], hsv[1], hsv[2]);
            //telemetry.addData("Color", "left (%.2f), right (%.2f)", BleftPower, BrightPower);
            telemetry.update();

        }
    }
}
