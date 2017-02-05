package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends SampleRobot {
	public static robotDrive robotDriver;
	public static UltrasonicFunctions ultraFunctions;
	public static final double speedConstant = .5;
	Joystick joyRt, joyLf, XBACKS, stick;
	static SmartDashboard board;
	Talon leftMotor, rightMotor, testarino;
	CANTalon encoder;
	double driveMod = 1;
	float speedMax = 0;
	public static UltrasonicData data;
	NewPixy pixyCam = new NewPixy();
	CameraServer server;
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	pixyData Pixydata;
	PixyFunctions pixyFunction;
	Potentiometer pot = new AnalogPotentiometer(0, 360, -900);

	gyroData gyro;
	double angle = 90;
	boolean firstRun = false;

	public Robot() {

		// data = new UltrasonicData(4,5,3,2);
		data = new UltrasonicData(4, 5);
		leftMotor = new Talon(2);
		rightMotor = new Talon(1);
		robotDriver = new robotDrive(rightMotor, leftMotor);
		ultraFunctions = new UltrasonicFunctions(data, rightMotor, leftMotor);
		// gyro = new gyroData(1, rightMotor, leftMotor);
		gyro = new gyroData(rightMotor, leftMotor);
		encoder = new CANTalon(1);
		board = new SmartDashboard();
		Pixydata = new pixyData(pixyCam);
		// pixyFunction = new PixyFunctions(Pixydata, rightMotor, leftMotor,
		// ultraFunctions);
		XBACKS = new Joystick(0);
		stick = new Joystick(1);
		joyRt = new Joystick(2);
		joyLf = new Joystick(3);
		server = CameraServer.getInstance();
		server.startAutomaticCapture(0);
		encoder.reset();
		encoder.reverseSensor(false);

		encoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		// encoder.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		encoder.configEncoderCodesPerRev(360);
		encoder.setPosition(0);

	}

	/**
	 * Runs the motors with Mecanum drive.
	 */

	@SuppressWarnings("deprecation")
	public void operatorControl() {

		int count = 0;
		while (isOperatorControl() && isEnabled()) {
			// if pressed center the robot on the peg.
			if (XBACKS.getRawButton(1)) {
				// pixyFunction.alignGearPeg();
				// encoderFunctions.putData();
			}
			// gives data between -21600 and 21600
//			// need to norm the data to between 0 and
//			SmartDashboard.putNumber("Pot Degrees (420 BLAZE IT LOLXD)", pot.get());
//			SmartDashboard.putNumber("Left Ultra", data.distanceLeft());
//			double encoder775rpm = encoder.getEncVelocity();
//			encoder775rpm = ((encoder775rpm + 30000) / 4);
//			if (encoder775rpm != 5400) {
//				SmartDashboard.putNumber("Current RPM:", encoder775rpm);
//			}
//			if (encoder775rpm > 12000 || encoder775rpm < -12000) {
//				SmartDashboard.putString("OVERSPEED", "TRUE");
//			} else {
//				SmartDashboard.putString("OVERSPEED", "FALSE");
//			}
//			if (encoder775rpm > speedMax) {
//				// speedMax = encoder.getSpeed();
//				speedMax = (float) encoder775rpm;
//			}
//			SmartDashboard.putNumber("Max RPM", speedMax);
			// Kevin Drive Function

			// gyro.getAngle();
			// gyro.rateOfMotion();

			// if (stick.getRawButton(2)) {
			// ultraFunctions.driveFowardUntil();
			// //ultraFunctions.selfStraight();
			// }
			// if(joyRt.getRawButton(2)){
			// pixyFunction.center();
			// }

			// SmartDashboard.putNumber("Current", pdp.getCurrent(2));

			// if (stick.getRawButton(3)) {
			// gyro.moveDegrees(90);
			//

			// }
			// robotDriver.driveJS(-joyLf.getY()*.8, joyRt.getY(), .5);
			robotDriver.drive(XBACKS.getRawAxis(1), XBACKS.getRawAxis(4) * .6, 1);
			// robotDriver.drive(joyRt.getY(), joyRt.getX(), 1);
			//
//			if (stick.getRawButton(1)) {
//				// encoderFunctions.putData();
//
//				leftMotor.set(stick.getRawAxis(3));
//			} else if (stick.getRawButton(2)) {
//				leftMotor.set(1);
//			} else {
//				leftMotor.set(0);
//			}

			// if(stick.getRawButton(2)){
			// rightMotor.set(stick.getRawAxis(3));
			// }
			// else{
			// rightMotor.set(0);
			// }

		}

	}

	public void autonomous() {
		// Put code here

		ultraFunctions.driveFowardUntil(36);
		gyro.moveDegrees(90);
		ultraFunctions.driveFowardUntil(36);
		robotDriver.stop();

	}
}
