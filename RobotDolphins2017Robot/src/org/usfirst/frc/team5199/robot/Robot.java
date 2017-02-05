package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class. The
 * SampleRobot class is the base of a robot application that will automatically
 * call your Autonomous and OperatorControl methods at the right time as
 * controlled by the switches on the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're
 * inexperienced, don't. Unless you know what you are doing, complex code will
 * be much more difficult under this system. Use IterativeRobot or Command-Based
 * instead if you're new.
 */
public class Robot extends SampleRobot {
	Pixy pixyGear, pixyShooter;
	PixyFunctions pixyFunctionsGear, pixyFunctionsShooter;
	Talon rightMotor, leftMotor;
	UltrasonicData data;
	UltrasonicFunctions ultraFunctions;
	EncoderFunctions encoder;
	RobotDrive robotDriver;
	Joystick xBox, stick;
	GyroFunctions gyro;
	
	public Robot() {
		
		rightMotor			= new Talon(RobotMap.rightMotor);
		leftMotor 			= new Talon(RobotMap.leftMotor);
		encoder 			= new EncoderFunctions(rightMotor, leftMotor);
		data 				= new UltrasonicData(RobotMap.ultraRightEcho, RobotMap.ultraRightPing, RobotMap.ultraLeftEcho, RobotMap.ultraLeftPing);
		ultraFunctions		= new UltrasonicFunctions(data, rightMotor, leftMotor);
		robotDriver 		= new RobotDrive(rightMotor, leftMotor);
		stick 				= new Joystick(RobotMap.joyStickPort);
		xBox				= new Joystick(RobotMap.xBoxPort);
		gyro 				= new GyroFunctions(rightMotor, leftMotor);
		pixyGear 			= new Pixy(RobotMap.pixyGear);
		pixyShooter 		= new Pixy(RobotMap.pixyShoot);
		pixyFunctionsGear	= new PixyFunctions(pixyGear, ultraFunctions, encoder);
		//TODO change the parameters of the pixyFunctions to give it the controllers necessary to shoot
		pixyFunctionsShooter= new PixyFunctions(pixyShooter);
	}

	@Override
	public void robotInit() {

	}

	@Override
	public void autonomous() {
		//Drive using encoders depending on autonomous start position
		
		//Drive till three feet away from peg, using ultras
		ultraFunctions.driveFowardUntil(36); 
		//Makes robot parallel to wall
		ultraFunctions.selfStraight();
		//fires the pixyCam for a little bit to gather data on the location
		pixyFunctionsGear.alignGear();
		//drives forward until gear is on peg, and it swivels back and forth
		ultraFunctions.swivelForward(RobotMap.distanceFromPeg); //distanceUntil peg
		//engages shooter, to hopefully gain more autoPoints
		pixyFunctionsShooter.shoot();
	}

	
	@Override
	public void operatorControl() {
		//pixyProc.pixyTestReset();
		while (isOperatorControl() && isEnabled()) {
			data.leftUltraTest();
			data.rightUltraTest();
			encoder.RPMs();
		
				leftMotor.set(stick.getRawAxis(3));
		
//			int distanceX = pixyProc.averageData(0,true)[0];
//			pixyProc.pixyTest();
			//pixyProc.pixyI2CTest();
			//robotDriver.drive(xBox.getRawAxis(1), xBox.getRawAxis(4) * .6, 1);
		}
	}

	/**
	 * Runs during test mode
	 */
	@Override
	public void test() {
	}
}
