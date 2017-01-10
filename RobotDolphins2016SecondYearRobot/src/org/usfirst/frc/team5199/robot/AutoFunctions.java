package org.usfirst.frc.team5199.robot;
import edu.wpi.first.wpilibj.RobotDrive;

public class AutoFunctions {
    RobotDrive robotDrive;
	public void Forward(){
        robotDrive.mecanumDrive_Cartesian(.25, 0, 0, 0);
	}
	public void Stop(){
        robotDrive.mecanumDrive_Cartesian(0, 0, 0, 0);
	}
	public void RightTurn(){
        robotDrive.mecanumDrive_Cartesian(0, 0, .25, 0);
	}
	public void LeftTurn(){
        robotDrive.mecanumDrive_Cartesian(0, 0, -.25, 0);
	}
}


