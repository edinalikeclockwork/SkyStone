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
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

// Based on TeamCode/src/main/java/org/firstinspires/ftc/teamcode/MecanumTeleopV2.java

@TeleOp(name="McTeleop_1")
//@Disabled
public class McTeleop_1 extends LinearOpMode {

    private McDriveTest_HW robot = new McDriveTest_HW();
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

// ADD DRIVE CODE HERE

            // Show the elapsed game time and wheel power.
           /* telemetry.addData("Status", "Run Time: " + runtime].toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", FleftPower, FrightPower);
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", BleftPower, BrightPower);
            telemetry.update();*/
           //Hello
        }
    }
}
