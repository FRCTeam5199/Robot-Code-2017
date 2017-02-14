//package org.usfirst.frc.team5199.robot;
//
//import edu.wpi.first.wpilibj.SampleRobot;
//import edu.wpi.first.wpilibj.Servo;
//import edu.wpi.first.wpilibj.Talon;
//
//import org.opencv.core.Mat;
//
//import com.ctre.CANTalon;
//import com.ctre.CANTalon.FeedbackDevice;
//
//import edu.wpi.cscore.CvSink;
//import edu.wpi.cscore.CvSource;
//import edu.wpi.cscore.UsbCamera;
//import edu.wpi.first.wpilibj.CameraServer;
//import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//
///**
// * This is a demo program showing the use of the RobotDrive class. The
// * SampleRobot class is the base of a robot application that will automatically
// * call your Autonomous and OperatorControl methods at the right time as
// * controlled by the switches on the driver station or the field controls.
// *
// * The VM is configured to automatically run this class, and to call the
// * functions corresponding to each mode, as described in the SampleRobot
// * documentation. If you change the name of this class or the package after
// * creating this project, you must also update the manifest file in the resource
// * directory.
// *
// * WARNING: While it may look like a good choice to use for your code if you're
// * inexperienced, don't. Unless you know what you are doing, complex code will
// * be much more difficult under this system. Use IterativeRobot or Command-Based
// * instead if you're new.
// */
//public class RobotAlt extends SampleRobot {
//	Pixy pixyGear, pixyShooter;
//	PixyFunctions pixyFunctionsGear, pixyFunctionsShooter;
//	Talon rightMotor, leftMotor;
//	UltrasonicData ultraData;
//	UltrasonicFunctions ultraFunctions;
//	EncoderFunctions encoder;
//	RobotDrive robotDriver;
//	Joystick xBox, stick;
//	GyroFunctions gyro;
//	Servo servo;
//
//	public RobotAlt() {
//
//		rightMotor = new Talon(RobotMap.rightMotor);
//		leftMotor = new Talon(RobotMap.leftMotor);
//		encoder = new EncoderFunctions(rightMotor, leftMotor);
//		ultraData = new UltrasonicData(RobotMap.ultraRightEcho, RobotMap.ultraRightPing, RobotMap.ultraLeftEcho,
//				RobotMap.ultraLeftPing);
//		ultraFunctions = new UltrasonicFunctions(ultraData, rightMotor, leftMotor);
//		robotDriver = new RobotDrive(rightMotor, leftMotor);
//		stick = new Joystick(RobotMap.joyStickPort);
//		xBox = new Joystick(RobotMap.xBoxPort);
//		gyro = new GyroFunctions(rightMotor, leftMotor);
//		pixyGear = new Pixy(RobotMap.pixyGear);
//		pixyShooter = new Pixy(RobotMap.pixyShoot);
//		servo = new Servo(9);
//		// pixyFunctionsGear = new PixyFunctions(pixyGear, ultraFunctions,
//		// encoder);
//		// TODO change the parameters of the pixyFunctions to give it the
//		// controllers necessary to shoot
//		// pixyFunctionsShooter= new PixyFunctions(pixyShooter);
//	}
//
//	@Override
//	public void robotInit() {
//		Thread t = new Thread(() -> {
//
//			boolean allowCam1 = false;
//
//			UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
//			camera1.setResolution(320, 240);
//			camera1.setFPS(30);
//			UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(1);
//			camera2.setResolution(320, 240);
//			camera2.setFPS(30);
//
//			CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
//			CvSink cvSink2 = CameraServer.getInstance().getVideo(camera2);
//			CvSource outputStream = CameraServer.getInstance().putVideo("Switcher", 320, 240);
//
//			Mat image = new Mat();
//
//			while (!Thread.interrupted()) {
//
//				if (xBox.getRawButton(2)) {
//					allowCam1 = !allowCam1;
//				}
//
//				if (allowCam1) {
//					cvSink2.setEnabled(false);
//					cvSink1.setEnabled(true);
//					cvSink1.grabFrame(image);
//				} else {
//					cvSink1.setEnabled(false);
//					cvSink2.setEnabled(true);
//					cvSink2.grabFrame(image);
//				}
//
//				outputStream.putFrame(image);
//			}
//
//		});
//		t.start();
//		
//
//	}
//
//	@Override
//	public void autonomous() {
//		// Drive using encoders depending on autonomous start position
//
//		// Drive till three feet away from peg, using ultras
//		ultraFunctions.driveFowardUntil(36);
//		// Makes robot parallel to wall
//		ultraFunctions.selfStraight();
//		// fires the pixyCam for a little bit to gather data on the location
//		// pixyFunctionsGear.alignGear();
//		// drives forward until gear is on peg, and it swivels back and forth
//		ultraFunctions.swivelForward(RobotMap.distanceFromPeg); // distanceUntil
//																// peg
//		// engages shooter, to hopefully gain more autoPoints
//		// pixyFunctionsShooter.shoot();
//	}
//
//	@Override
//	public void operatorControl() {
//		// pixyProc.pixyTestReset();
//		encoder.initEncoders();
//		
//		while (isOperatorControl() && isEnabled()) {
//			// ultraData.leftUltraTest();
//			// ultraData.rightUltraTest();
//			// SmartDashboard.putNumber("Current Speed:",
//			// encoder.getSpeed()*30);
//			// SmartDashboard.putNumber("Current Speed (Possibly RPMs",
//			// encoder.getSpeed());
//			// SmartDashboard.putNumber("Current Velocity",
//			// encoder.getEncVelocity());
//			// SmartDashboard.putNumber("Current Position",
//			// encoder.getEncPosition());
//			if (xBox.getRawButton(3)) {
//				servo.set(0);
//			} else {
//				servo.set(1);
//			}
//			if(stick.getRawButton(1)){
//				encoder.driveForward(200*Math.PI);
//				//encoder.driveForward(10);
//			}
//			encoder.RPMs();
//				
//			// leftMotor.set(stick.getRawAxis(3));
//
//			// int distanceX = pixyProc.averageData(0,true)[0];
//			// pixyProc.pixyTest();
//			// pixyProc.pixyI2CTest();
//			//robotDriver.drive(xBox.getRawAxis(1), xBox.getRawAxis(4) * .6,1);
//		}
//	}
//
//	/**
//	 * Runs during test mode
//	 */
//	@Override
//	public void test() {
//	}
//}
