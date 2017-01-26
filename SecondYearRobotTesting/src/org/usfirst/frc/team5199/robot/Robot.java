package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends SampleRobot {
	public static robotDrive robotDriver;
	public static UltrasonicFunctions ultraFunctions;
	public static final double speedConstant = .5;
	Joystick stick;
	static SmartDashboard board;
	Talon leftMotor, rightMotor, testarino;
	double driveMod = 1;
	public static UltrasonicData data;
	NewPixy pixyCam = new NewPixy();
	CameraServer server;
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	pixyFunctions pixyFunction;
	gyroData gyro;
	double angle = 90;
	boolean firstRun = false;

	public Robot() {

		//data = new UltrasonicData(4,5,3,2);
		data = new UltrasonicData(4,5);
		leftMotor = new Talon(2);
		rightMotor = new Talon(1);
		robotDriver = new robotDrive(rightMotor, leftMotor);
		ultraFunctions = new UltrasonicFunctions(data, rightMotor, leftMotor);
		// gyro = new gyroData(1, rightMotor, leftMotor);
		gyro = new gyroData(rightMotor, leftMotor);

		board = new SmartDashboard();
		pixyFunction = new pixyFunctions(pixyCam, rightMotor, leftMotor, ultraFunctions);
		stick = new Joystick(1);
		server = CameraServer.getInstance();
		server.startAutomaticCapture(0);
	}

	/**
	 * Runs the motors with Mecanum drive.
	 */

	@SuppressWarnings("deprecation")
	public void operatorControl() {
		int count = 0;
		while (isOperatorControl() && isEnabled()) {
			SmartDashboard.putNumber("Left Ultra", data.distanceLeft());
			// Kevin Drive Function

			// gyro.getAngle();
			// gyro.rateOfMotion();
			double distanceRight = 0, distanceLeft = 0, distanceOff = 0;
			

			if (count % 100 == 0) {
				SmartDashboard.putNumber("Count: ", (double) count);
			}

			if (stick.getRawButton(2)) {
				ultraFunctions.driveFowardUntil();
				//ultraFunctions.selfStraight();
			}
			if(stick.getRawButton(5)){
				pixyFunction.center();
			}

		

			// SmartDashboard.putNumber("Current", pdp.getCurrent(2));
			//
			// if(stick.getRawAxis(1)>0){
			// robotDriver.goForward(stick.getRawAxis(1)*speedConstant);
			// if(stick.getRawAxis(4)>0){
			// robotDriver.goRight(stick.getRawAxis(4)*speedConstant);
			// }
			// }else if(stick.getRawAxis(4)>0){
			// robotDriver.goRight(stick.getRawAxis(4)*speedConstant);
			// }
			
			if (stick.getRawButton(3)) {
				gyro.moveDegrees(90);
				
			} 
				robotDriver.drive(stick.getRawAxis(1), stick.getRawAxis(2), .5);
				
		}

	}

	public void autonomous() {
		// Put code here
		
		ultraFunctions.driveFowardUntil();
		gyro.moveDegrees(90);
		ultraFunctions.driveFowardUntil();
		robotDriver.stop();
		
		
		}
	}

