package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.Gyroscope;
import java.lang.annotation.Target;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.hardware.DigitalChannel;

import android.graphics.Color;

/**
  *
  * TeleOp for competition matches
  *   Gamepad1:  Drive control uses y-axis of left and right stick
  *              Button X: Drive motor power offset toggle
  *              Arm servo controlled with left and right bumper
  *
  *   Gamepad2:  Lift controlled with dpad up and down
  *
  */

@TeleOp ( name = "TeleOp for Competition")

public class TeleOpComp extends LinearOpMode {

    // private Gyroscope imu;
    public DcMotor  leftDrive;
    public DcMotor  rightDrive;
    public DcMotor  leftLift;
    public DcMotor  rightLift;
    public Servo    lockServo;
    public Servo    armServo;
    public DigitalChannel digitalTouch;
    public DistanceSensor distanceSensor;

    private Servo servoTest;

    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime button_x_timer = new ElapsedTime();

    @Override
    public void runOpMode () {
        // imu = hardwareMap.get(Gyroscope.class, "imu");
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        leftLift = hardwareMap.get(DcMotor.class, "left_lift");
        rightLift = hardwareMap.get(DcMotor.class, "right_lift");

        lockServo = hardwareMap.servo.get("lift_lock_servo");
        armServo  = hardwareMap.servo.get("as");

        digitalTouch = hardwareMap.digitalChannel.get("touch_sensor");

        // set the digital channel to input.
        digitalTouch.setMode(DigitalChannel.Mode.INPUT);

        telemetry.addData("Status", "Initialized - All Systems Go!");
        telemetry.update();

        // Wait for game to start (driver presses PLAY)
        waitForStart();
        // run until the end of the match (driver presses STOP)

        double  left = 0;
        double  right = 0;
        int     liftArm = 0;
        double  powerOffset = 1;
        double  directionSwap = -1;
        double  liftPowerInit = 0.40;
        boolean touchState = true;
        boolean gamePadXState;
        boolean prevGamePadXState = false;
        boolean buttonXPressed = false;

        double liftPower = liftPowerInit;

        // Used with color sensor
        double COLOR_SCALE_FACTOR = 1;
        float hsvValues[] = {0F, 0F, 0F};
        final float values[] = hsvValues;

        // Open lift lock to make sure it's not in way of lift during TeleOp
        lockServo.setPosition(0.9);    // Open Position

        while (opModeIsActive() && false) {
            // Testing continuous rotation servo
            // 1 = Clockwise - full power
            // 0 = Counter-clockwise - full power
            // 0.5 = Stop
            if ( this.gamepad1.left_bumper ) {
                armServo.setPosition(1);
            } else if ( this.gamepad1.right_bumper ) {
                armServo.setPosition(0);
            } else {
                armServo.setPosition(0.5);
            }
        }

        while (opModeIsActive() && true) {
            // Use gamepad button x to toggle a power offset to drive motors
            telemetry.addData("Timer:", (int)(runtime.seconds()) );
            //Lower drive power when time reaches 90 seconds
            if ( (int)(runtime.seconds() ) == 90) {
                powerOffset = 0.5;
            }

            gamePadXState = gamepad1.x;
            if ( gamePadXState && gamePadXState != prevGamePadXState ) {
                if (powerOffset == 1) {
                    powerOffset = 0.5;
                    prevGamePadXState = true;
                } else {
                    powerOffset = 1;
                    prevGamePadXState = false;
                }
            }

            // Use gamepad 2 button b to set lift lock
            // Disabled as it caused more problems than helped
            /**
            if (gamepad2.b) {
                lockServo.setPosition(0.57);    // Locked Position
            }
            */

            if (gamepad2.y) {
                // Open lift lock with button Y
                lockServo.setPosition(0.24);    // Open Position
            }

            // Use gamepad1 bumpers to move arm in and out
            if ( this.gamepad1.left_bumper ) {
                armServo.setPosition(1);
            } else if ( this.gamepad1.right_bumper ) {
                armServo.setPosition(0);
            } else {
                armServo.setPosition(0.5);
            }

            left = directionSwap * -this.gamepad1.left_stick_y;
            right = directionSwap * this.gamepad1.right_stick_y;
            leftDrive.setPower(left * powerOffset);
            rightDrive.setPower(right * powerOffset);

            // Control lift Arm - Stop motors when raising lift when touch sensor turns false
            touchState = digitalTouch.getState();

            if ( this.gamepad2.dpad_up && touchState ) {
                liftArm = 1;
                liftPower = liftPowerInit;
            } else if ( this.gamepad2.dpad_down ) {
                liftArm = -1;
                liftPower = liftPowerInit * 1.25;
            } else {
                liftArm = 0;
            }

            leftLift.setPower(liftArm * liftPower);
            rightLift.setPower(liftArm * -liftPower);

            telemetry.addData("Target Power - Left", left);
            telemetry.addData("Target Power - Right", right);
            telemetry.addData("Motor Power - Left", leftDrive.getPower());
            telemetry.addData("Motor Power - Right", rightDrive.getPower());
            telemetry.addData("Power Offset:", powerOffset);
            // telemetry.addData("Lift Power", liftPower);
            // telemetry.addData("Direction Swap", directionSwap );
            telemetry.addData("Touch Sensor", touchState );
            //telemetry.addData("Status", "Running");
            telemetry.update();

            // Try to improve consistency of button X controlling speed offset
            if ( prevGamePadXState != gamePadXState ) { sleep(100); }
            prevGamePadXState = gamePadXState;
        }
    }
}
