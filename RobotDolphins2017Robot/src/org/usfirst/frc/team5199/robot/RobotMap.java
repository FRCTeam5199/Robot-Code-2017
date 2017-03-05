package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	public static int rightMotor = 0; // pwm	
	public static int leftMotor = 1; // pwm 
	public static int intake = 2; // pwm
	public static int shooterServo2 = 3; //pwm
	public static int climber = 7; // pwm
	public static int transport = 8; // pwm
	public static int shooterServo = 9; // pwm
	
	public static int shooter = 0; // CAN Bus
	public static int turret = 1; // CAN Bus
	
	public static int xBoxPort = 0; // computerUsb
	public static int joyStickPort = 1; // computerUsb
	
	public static int release = 4;
	public static int gearShift = 5; //Pneumatics Ctrl Module PCM

	public static int canTalonPort = 1; // canPort
	
	public static int ultraLeftPing = 0; // DIO
	public static int ultraLeftEcho = 1; // DIO
	public static int ultraRightPing = 2; // DIO
	public static int ultraRightEcho = 3; // DIO
	public static int encoderLeftDIOA = 4; // DIO
	public static int encoderLeftDIOB = 5; // DIO
	public static int encoderRightDIOA = 6; // DIO
	public static int encoderRightDIOB = 7; // DIO
	public static int encoderShooterDIOA = 8; //DIO
	public static int encoderShooterDIOB = 9; //DIO

	public static int usbCamera1 = 0; // RoboRio usb
	public static int usbCamera2 = 1; // RoboRio usb

	public static int pixyGear = 0x51; // PixyMon address on I2C bus
	public static int pixyShoot = 0x53; // PixyMon address on I2C bus

	// Constants for parts of the robot
	public static double ultraDistanceOff = 3; // in inches for the ultrasonic
												// sensors
	public static double ultraBuffer = 1; // in inches for the ultrasonic
											// sensors
	public static double distanceFromPeg = 13; // in inches for the distance
												// from the gear peg

	public static int pixyGearDistanceOff = 4; // in pixels for the gear
												// Pixycam
	public static int pixyGearDataBuffer = 10; // in pixels for the gear PixyCam
	public static int pixyShooterDistanceOff = 10;// in pixels for the shooter
													// Pixycam
	public static int pixyShooterDataBuffer = 5; // in pixels for the shooter
													// Pixycam

	public static double inchesPerRotation = 4 * Math.PI; // for 4 inch wheels
	public static double inchesPerRotationShooter = 1; //for the rpms
	public static double stoppedDetectionTime = .1; // sets time for max period
													// in seconds
	public static double minimumRateStopped = .5; // speed until stopped
	public static int distanceTurnedPerDegree = 0; // the amount of inches
													// turned per degree of
													// rotation

	public static double[] rampUpDistance = { 6, 18 }; // distance thresholds
														// for acceleration
	public static double[] rampUpSpeed = { .3, .4, .5 };// speeds for the
														// different distances
	public static double[] rampDownDistance = { 36, 12, 6 }; // distance
																// thresholds
																// for
																// deceleration
	public static double[] rampDownSpeed = { .5, .4, .3 };// speeds for the
															// different
															// distances
	public static double   maxStraightSpeed = 1.0;      // speeds for the
	public static int 		climberChannel =10;
	
	public static double AdjustRotationCompensation = .05; //Adjusts 5% per inch off
	public static double AdjustRotationLimit 		= 1.3; //allows up to 30% compensation, must be >1
	public static double driveForwardEncoderCompensation = .05; //Allows 5% per inch off
	public static double driveForwardEncoderLimit = .7; //Allows up to 30% compensation, must be less than <1
	
	//The loader turn target is 110 degrees. The turn is fast (power = .8)
	//Do not increase power because we need head room for rotation compensation
	//There is a lot of overshoot, which is why it is set to 75 degrees
	//-9 compensation was determined empirically on concrete and may need adjustment for carpet
	public static double loaderTurnOffSet 			= -9; 
	public static double loaderTurnSpeed 			= .8;
	public static double loaderTurnAngle			= 75;
	
	//The auton turn 60 function turns 60 degrees 60 is not here because it is in function name
	//We want to have a slower speed, to have an accurate turn
	//These values are determined with a battery above 12.2 volts on concrete floor 
	public static double autonSixtyTurnOffSetRight  = -9;
	public static double autonSixyTurnOffSetLeft 	= -11;
	public static double autonSixtyTurnSpeed = .4;
	
	public static double autonNinetyTurnOffSetRight = -14;
	public static double autonNinetyTurnOffSetLeft = -14;
	public static double autonNinetyTurnSpeed = .4;
	  
	//Button Map
	//Xbox Controller Map
	
	public static int loaderTurnButton =3;
	public static int loaderTurnAxis =0;
	public static int flipperButt = 4;
	public static int ultraForwardButton =1;
	//Unused Buttons:
	/*
	 * left Bumper
	 * right bumper
	 * left trigger
	 * right trigger
	 */
	//Joystick Controller Map
	public static int shootButton = 1;
	public static int transportButton =2;
	public static int sweeperButton =3;
	public static int climberButton =4;
	public static int highServoButton = 5;
	public static int lowServoButton =6;
	public static int turretButton = 7; //axis not a button
	public static int enableRumbleButton =11;
	public static int encoderTestDriveButton =12;
	/*
	 * Joystick
	 * Trigger is full shoot
	 * spin up flywheel thumb
	 * shooter constant of .9
	 * 4 and 3 intake 
	 * pov hat is servo
	 * 5 and 6 climber
	 * 
	 * 
	 * xBox
	 * stick driving (halo)
	 * drive swap on y also switches camera
	 * 
	 */
	
}
