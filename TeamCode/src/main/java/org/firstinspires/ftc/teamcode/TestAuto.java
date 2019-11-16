package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous

public class TestAuto extends LinearOpMode
{
    private McDriveTest_HW robot = new McDriveTest_HW();
    
    private double powerMultipler = 1.0;

    @Override
    
    public void runOpMode() 
    {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        ElapsedTime runtime = new ElapsedTime();
        
        waitForStart();
        runtime.reset();
        
        MoveSideways(0.5, "right", 1000);
        MoveFwdBack(0.5, "backward", 1000);
        MoveSideways(0.5, "left", 1000);
        MoveFwdBack(0.5, "forward", 1000);
    }
    
    public void MoveSideways (double power, String direction, int time)
    {
        if (direction == "left")
        {
            robot.lf.setPower(-power);
            robot.lr.setPower(power);
            robot.rf.setPower(power);
            robot.rr.setPower(-power);
            
            sleep(time);
        }
        
        else if (direction == "right")
        {
            robot.lf.setPower(power);
            robot.lr.setPower(-power);
            robot.rf.setPower(-power);
            robot.rr.setPower(power);
            
            sleep(time);
        }
    }
    
    public void MoveFwdBack (double power, String direction, int time)
    {
        if (direction == "forward")
        {
            robot.lf.setPower(power);
            robot.lr.setPower(power);
            robot.rf.setPower(power);
            robot.rr.setPower(power);
            
            sleep(time);
        }
        
        else if (direction == "backward")
        {
            robot.lf.setPower(-power);
            robot.lr.setPower(-power);
            robot.rf.setPower(-power);
            robot.rr.setPower(-power);
            
            sleep(time);
       }
    }
    
    public void Rotate (double power, double degrees, int time)
    {
        
    }
}
