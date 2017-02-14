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

	public static int leftMotor = 0; // pwm
	public static int rightMotor = 1; // pwm
	public static int shooter = 3; // pwm
	public static int angleAdjuster = 4; // pwm
	public static int turret = 0; // CAN Bus
	public static int shooterServo = 9; // pwm
	public static int intake = 2; // pwm
	public static int transport = 8; // pwm
	public static int climber = 7; // pwm

	public static int ultraRightPing = 3; // DIO
	public static int ultraRightEcho = 2; // DIO
	public static int ultraLeftPing = 5; // DIO
	public static int ultraLeftEcho = 4; // DIO

	public static int xBoxPort = 0; // computerUsb
	public static int joyStickPort = 1; // computerUsb

	public static int canTalonPort = 1; // canPort

	public static int encoderRightDIOA = 8; // DIO
	public static int encoderRightDIOB = 9; // DIO
	public static int encoderLeftDIOA = 1; // DIO
	public static int encoderLeftDIOB = 2; // DIO

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

	public static int pixyGearDistanceOff = 40; // in pixels for the gear
												// Pixycam
	public static int pixyGearDataBuffer = 5; // in pixels for the gear PixyCam
	public static int pixyShooterDistanceOff = 10;// in pixels for the shooter
													// Pixycam
	public static int pixyShooterDataBuffer = 5; // in pixels for the shooter
													// Pixycam

	public static double inchesPerRotation = 4 * Math.PI; // for 4 inch wheels
	public static double stoppedDetectionTime = .1; // sets time for max period
													// in seconds
	public static double minimumRateStopped = .5; // speed until stopped
	public static int distanceTurnedPerDegree = 0; // the amount of inches
													// turned per degree of
													// rotation

	public static double[] rampUpDistance = { 12, 36 }; // distance thresholds
														// for acceleration
	public static double[] rampUpSpeed = { .1, .2, .5 };// speeds for the
														// different distances
	public static double[] rampDownDistance = { 96, 48, 24 }; // distance
																// thresholds
																// for
																// deceleration
	public static double[] rampDownSpeed = { .5, .2, .1 };// speeds for the
															// different
															// distances
}
