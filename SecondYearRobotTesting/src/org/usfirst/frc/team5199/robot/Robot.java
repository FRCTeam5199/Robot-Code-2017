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
		stick = new Joystick(0);
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
			// if (pixyCam.getStartOfData() == 1) {
			//
			// short sig;
			//
			// byte[] syncedBufferWithoutSync = new byte[12];
			// syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(12);
			// SyncedBlock ourBlock1 = new SyncedBlock(syncedBufferWithoutSync);
			// sig = ourBlock1.getSignature();
			// SmartDashboard.putNumber(sig + " Synced Checksum:",
			// ourBlock1.getChecksum());
			// // SmartDashboard.putNumber("Synced Signature:",
			// // ourBlock.getSignature());
			// distanceRight = ourBlock1.getX();
			// SmartDashboard.putString(sig + " Synced X:",
			// String.valueOf(ourBlock1.getX()));
			// SmartDashboard.putString(sig + " Synced Y:",
			// String.valueOf(ourBlock1.getY()));
			// // SmartDashboard.putString("Synced X HEX:",
			// // SyncedBlock.getHexRepresentation(ourBlock.rawData[4],
			// // ourBlock.rawData[5]));
			// // SmartDashboard.putString("Synced Y HEX:",
			// // SyncedBlock.getHexRepresentation(ourBlock.rawData[6],
			// // ourBlock.rawData[7]));
			// SmartDashboard.putString(sig + " Synced Width:",
			// String.valueOf(ourBlock1.getWidth()));
			// SmartDashboard.putString(sig + " Synced height:",
			// String.valueOf(ourBlock1.getHeight()));
			//
			// byte[] secondBlockSync = new byte[2];
			// secondBlockSync = pixyCam.getVariableSizeBuffer(2);
			// if ((secondBlockSync[0] == (byte) 0x55) && (secondBlockSync[1] ==
			// (byte) 0xAA)) {
			// syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(12);
			// SyncedBlock ourBlock2 = new SyncedBlock(syncedBufferWithoutSync);
			// sig = ourBlock2.getSignature();
			// SmartDashboard.putNumber("DistanceOff ",
			// (distanceRight-ourBlock2.getX()));
			// SmartDashboard.putNumber(sig + " Synced Checksum:",
			// ourBlock2.getChecksum());
			// SmartDashboard.putString(sig + " Synced X:",
			// String.valueOf(ourBlock2.getX()));
			// SmartDashboard.putString(sig + " Synced Y:",
			// String.valueOf(ourBlock2.getY()));
			// SmartDashboard.putString(sig + " Synced Width:",
			// String.valueOf(ourBlock2.getWidth()));
			// SmartDashboard.putString(sig + " Synced height:",
			// String.valueOf(ourBlock2.getHeight()));
			// }
			// }

			if (count % 100 == 0) {
				SmartDashboard.putNumber("Count: ", (double) count);
			}

			if (stick.getRawButton(2)) {
				ultraFunctions.driveFowardUntil();
				//ultraFunctions.selfStraight();
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
				robotDriver.drive(stick.getRawAxis(1), stick.getRawAxis(4), .5);
				
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

