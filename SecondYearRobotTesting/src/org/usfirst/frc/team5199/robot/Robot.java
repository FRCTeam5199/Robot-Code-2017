package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends SampleRobot {
	public static robotDrive robotDriver;
	public static UltrasonicFunctions ultraFunctions;
	public static final double speedConstant = .5;
	Joystick stick;
	static SmartDashboard board;
	Talon leftMotor, rightMotor;
	double driveMod = 0.5;
	public static UltrasonicData data;
	
	//gyroData gyro;
	public Robot() {
	//ultra = new UltrasonicFunction(3,2,4,echo, 1, 2);
		
		
		//data = new UltrasonicData(4,5,3,2);
		leftMotor = new Talon(2);
		rightMotor = new Talon(1);
		robotDriver = new robotDrive(rightMotor,leftMotor);
		ultraFunctions =  new UltrasonicFunctions(4,5,3,2,rightMotor,leftMotor);
		// gyro = new gyroData(0);
		board = new SmartDashboard();
		
		stick = new Joystick(0);
		
	
	}

	/**
	 * Runs the motors with Mecanum drive.
	 */

	
	@SuppressWarnings("deprecation")
	public void operatorControl() {

		while (isOperatorControl() && isEnabled()) {
			// Kevin Drive Function
			
			
			
		//	gyro.getAngle();
			//gyro.rateOfMotion();
			
			if(stick.getRawButton(2)){
				ultraFunctions.selfStraight();
			}
//			
//			if(stick.getRawAxis(1)>0){
//				robotDriver.goForward(stick.getRawAxis(1)*speedConstant);
//				if(stick.getRawAxis(4)>0){
//					robotDriver.goRight(stick.getRawAxis(4)*speedConstant);
//				}
//			}else if(stick.getRawAxis(4)>0){
//				robotDriver.goRight(stick.getRawAxis(4)*speedConstant);
//			}
		robotDriver.drive(stick.getRawAxis(1), stick.getRawAxis(4), driveMod);

			
			
		}

	}

	public void autonomousPeriodic() {

	}

	public void autonomousInit() {

	}
}
