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
	public static int leftMotor 		=2; //pwm
	public static int rightMotor 		=1; //pwm
	public static int ultraRightPing 	=4; //pwm
	public static int ultraRightEcho 	=5; //pwm
	public static int ultraLeftPing 	=3; //pwm
	public static int ultraLeftEcho 	=2; //pwm
	
	public static int xBoxPort 			=0; //computerUsb
	public static int joyStickPort 		=1; //computerUsb
	
	public static int canTalonPort 		=1; //canPort
	
	public static int usbCamera1 		=0; //RoboRio usb
	public static int usbCamera2 		=1; //RoboRio usb
	
	public static int pixyGear 			=0x51; //PixyMon address on I2C bus
	public static int pixyShoot			=0x53; //PixyMon address on I2C bus
}
