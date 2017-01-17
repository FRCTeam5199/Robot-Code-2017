///*
// * Robot.java but all the commented crap is removed.
// * Not finished yet.
// */
//
//
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//
//import com.ni.vision.NIVision.Image;
//import com.ni.vision.NIVision.ShapeMode;
//
//import edu.wpi.first.wpilibj.ADXRS450_Gyro;
//import edu.wpi.first.wpilibj.AnalogGyro;
//import edu.wpi.first.wpilibj.CANTalon;
//import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
//import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
//import edu.wpi.first.wpilibj.CameraServer;
//import edu.wpi.first.wpilibj.Compressor;
//import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.PowerDistributionPanel;
//import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.SPI;
//import edu.wpi.first.wpilibj.SampleRobot;
//import edu.wpi.first.wpilibj.Servo;
//import edu.wpi.first.wpilibj.Solenoid;
//import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.TalonSRX;
//import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.Ultrasonic;
//import edu.wpi.first.wpilibj.Victor;
//import edu.wpi.first.wpilibj.interfaces.Gyro;
//import edu.wpi.first.wpilibj.smartdashboard.*;
//import edu.wpi.first.wpilibj.vision.AxisCamera;
//
//public class NewRobot extends SampleRobot {
//
//	//NewPixy pixyCam = new NewPixy();
////	PowerDistributionPanel pdp;
////	CANTalon talon;
////	DoubleSolenoid piston0;
////	DoubleSolenoid piston1;
////	Solenoid cylinder1, cylinder2, cylinder4, cylinder5;
////	Ultrasonic leftultra = new Ultrasonic(0, 1);
////	Ultrasonic rightultra = new Ultrasonic(2, 3);
////	double leftrange, rightrange;
////	DigitalInput HallEffectTest;
////	CANTalon ballMotor, testarino;
////	Solenoid cylinder3;
////	ADXRS450_Gyro gyro;
////	long millis, millis2, timePassed, millis3;
////	RobotDrive robotDrive;
////	Joystick stick, stick2, stickRacingWheel;
////	boolean value, pos1 = true, pos2, pos3, pos4, pos5, pos6;
////	int auto_counter, framenum = 0;
////	static SmartDashboard board;
////	Compressor compressor;
////	Talon leftMotors, rightMotors, frontRight, frontLeft, backRight, backLeft, Right, Left;
////	Victor port4;
////	Image frame;
////	AxisCamera camera;
////	Servo servo1, servo2;
////	BufferedImage blue = null;
////	BufferedImage red = null;
////	final int frontLeftChannel = 0;
////	final int rearLeftChannel = 0;
////	final int frontRightChannel = 1;
////	final int rearRightChannel = 1;
////	double DriveSpeed = -.5;
////	double MainCylTime;
////	boolean mainCylDir;
////	boolean mainCylOn;
////	boolean squaring;
////	boolean firstRunThroughTeleop = true;
////	int YBA, YBA2;
////	double sliderJoy;
////	int camUp = 90, CamX = 90;
////	boolean alternateAlert = true;
////	boolean fast180 = true;
////	int testNum;
////	double calRot = 15;
////	String codePos = "";
////	int integers = 0;
////	final int joystickChannel = 1;
////	double time1;
////	CameraServer server;
////
////	public NewRobot() {
////		
////		firstRunThroughTeleop = true;
////		
////		server = CameraServer.getInstance();
////		server.setQuality(25);
////		server.startAutomaticCapture("cam1");
////		
////		int camUp = 90, camX = 90;
////		
////		servo1 = new Servo(4);	//top camera servo
////		servo2 = new Servo(5);	//bottom camera servo
////		auto_counter = 0;
////
////		board = new SmartDashboard();
////		
////		cylinder1 = new Solenoid(4,0);
////		cylinder2 = new Solenoid(4,1);
////		cylinder3 = new Solenoid(4,2);
////		cylinder4 = new Solenoid(4,3);
////		
////		cylinder4 = new Solenoid(4,4);
////		cylinder5 = new Solenoid(4,5);
////		
////		leftMotors = new Talon(0);
////		rightMotors = new Talon(1);
////
////		stick = new Joystick(0);
////		stick2 = new Joystick(1);
////		
////		leftultra.setAutomaticMode(true);
////
////		rightultra.setAutomaticMode(true);
////		
////		gyro = new ADXRS450_Gyro();
////		
////		pdp = new PowerDistributionPanel();
////		
////		testarino = new CANTalon(1);
////		
////		testarino.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
////		
////		SmartDashboard.putString("Calibrate Rotation", String.valueOf(calRot));
//
//
//	}
//}
