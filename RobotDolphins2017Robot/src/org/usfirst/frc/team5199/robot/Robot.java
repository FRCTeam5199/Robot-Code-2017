package org.usfirst.frc.team5199.robot;

import org.opencv.core.Mat;

import com.ctre.CANTalon;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * *****************************************************************************
 * ***************************************************** Robot Dolphins from
 * Outer Space should use the extension SampleRobot due to its efficiency in
 * looping quickly through the tele-op periodic IterativeRobot extension should
 * be avoided because a fast iteration rate is absolutely necessary (1000+
 * loops/sec vs 20 loops/sec)
 * 
 * 
 */
public class Robot extends SampleRobot {
	public int counter = 0;

	Relay relay;
	Pixy pixyGear, pixyShooter;
	PixyProcess pixyGearProc;
	PixyFunctions pixyFunctionsGear, pixyFunctionsShooter;
	CANTalon shooter;
	Spark leftMotor, rightMotor, climber, transport, sweeper;
	DoubleSolenoid solenoid;
	CANTalon turret;
	UltrasonicData ultraData;
	UltrasonicFunctions ultraFunctions;
	EncoderDriveFunctions encoder;
	RobotDrive robotDriver;
	Joystick xBox, stick;
	GyroFunctions gyro;
	Servo servo, servo2;
	ServoDude shooterServo, servoBoy;
	double strokePos;
	double currAngle;
	String autoMode;
	Compressor compressor;
	int autoStep;
	int gearLoadStep;
	boolean flipped = false;
	CameraServer server;
	boolean first = true;
	double startTime = 0;
	PowerDistributionPanel pdp;
	public static int step = 0;
	int stepShooter = 0;
	int shooterSpeed = 2000;
	public static double position = 0.0, increment = 0.001;
	public static double straightMod = 1, turnMod = .6, driveMod = 1;
	public static int shiftStep = 0;
	public static int shifter = 0;
	boolean left;

	public Robot() {

	}

	@Override
	public void robotInit() {
		counter = 0;
		pdp = new PowerDistributionPanel();
		rightMotor = new Spark(RobotMap.rightMotor);
		leftMotor = new Spark(RobotMap.leftMotor);
		shooter = new CANTalon(RobotMap.shooter);
		sweeper = new Spark(RobotMap.intake);
		transport = new Spark(RobotMap.transport);
		climber = new Spark(RobotMap.climber);
		turret = new CANTalon(RobotMap.turret);
		encoder = new EncoderDriveFunctions(rightMotor, leftMotor, shooter);
		encoder.resetDrive();
		ultraData = new UltrasonicData(RobotMap.ultraRightEcho, RobotMap.ultraRightPing, RobotMap.ultraLeftEcho,
				RobotMap.ultraLeftPing);
		ultraFunctions = new UltrasonicFunctions(ultraData, rightMotor, leftMotor);
		robotDriver = new RobotDrive(rightMotor, leftMotor);
		stick = new Joystick(RobotMap.joyStickPort);
		xBox = new Joystick(RobotMap.xBoxPort);
		gyro = new GyroFunctions(rightMotor, leftMotor);
		SmartDashboard.putNumber("Gyro Adjust", 8.0);
		pixyGear = new Pixy(RobotMap.pixyGear);
		pixyGearProc = new PixyProcess(pixyGear);
		pixyShooter = new Pixy(RobotMap.pixyShoot);
		pixyFunctionsGear = new PixyFunctions(pixyGear, ultraFunctions, encoder, robotDriver);
		servo = new Servo(RobotMap.shooterServo);
		shooterServo = new ServoDude(servo);
		servo2 = new Servo(RobotMap.shooterServo2);
		servoBoy = new ServoDude(servo2);
		strokePos = 0.0;
		solenoid = new DoubleSolenoid(4, 5);
		gyro.gyro.calibrate();

		relay = new Relay(0);
		// server = CameraServer.getInstance();
		// server.startAutomaticCapture(0);
		SmartDashboard.putString("Autonomous Mode", "Enter Value");

		// TODO change the parameters of the pixyFunctions to give it the
		// controllers necessary to shoot
		Thread t = new Thread(() -> {

			boolean allowCam1 = false;

			UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
			camera1.setResolution(320, 240);
			camera1.setFPS(30);
			UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(1);
			camera2.setResolution(320, 240);
			camera2.setFPS(30);

			CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
			CvSink cvSink2 = CameraServer.getInstance().getVideo(camera2);
			CvSource outputStream = CameraServer.getInstance().putVideo("Switcher", 320, 240);

			Mat image = new Mat();

			while (!Thread.interrupted()) {

				if (xBox.getRawButton(5)) {
					allowCam1 = !allowCam1;
				}
				// if (flipped) {
				// cvSink1.setEnabled(false);
				// cvSink2.setEnabled(true);
				// cvSink2.grabFrame(image);
				// } else
				if (allowCam1) {
					cvSink2.setEnabled(false);
					cvSink1.setEnabled(true);
					cvSink1.grabFrame(image);
				} else {
					cvSink1.setEnabled(false);
					cvSink2.setEnabled(true);
					cvSink2.grabFrame(image);
				}

				outputStream.putFrame(image);
			}

		});
		t.start();

		autoMode = SmartDashboard.getString("Autonomous Mode");
		autoStep = 0;
		encoder.initEncoders();
		startTime = 0;
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * if-else structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomous() {
		if (autoMode.equals("L") || autoMode.equals("l")) {
			if (autoStep == 0) {
				encoder.initEncoders();
				solenoid.set(DoubleSolenoid.Value.kForward);
				startTime = System.currentTimeMillis();
				autoStep = 1;
			}

			if (autoStep == 1) {
				// TODO: Set time in robot map
				if (System.currentTimeMillis() - startTime > 1000) {
					solenoid.set(DoubleSolenoid.Value.kOff);
				}
				if (encoder.driveStraightAuton(57)) {
					autoStep = 2;
					currAngle = gyro.getAngle();
					encoder.initEncoders();
				}
			}
			if (autoStep == 2) {
				if (encoder.autonSixtyDegreeTurn(currAngle, false)) {
					autoStep = 3;
				}
			}
			if (autoStep == 3) {
				encoder.initEncoders();
				autoStep = 4;
			}
			if (autoStep == 4) {
				if (encoder.driveStraightAuton(21)) {
					autoStep = 5;
				}
			}
			if (autoStep == 5) {
				if (pixyFunctionsGear.turnAndGoStraightAuton()) {
					autoStep = 6;
				}
				if (ultraData.distanceRight() < 10 || ultraData.distanceLeft() < 10) {
					autoStep = 6;
				}
			}

			if (autoStep == 6) {
				if (ultraFunctions.driveFowardAuton(4)) {
					autoStep = 7;
				}
			}
			if (autoStep == 7) {
				robotDriver.stop();
			}
		} else if (autoMode.equals("C") || autoMode.equals("c")) {
			if (autoStep == 0) {
				encoder.initEncoders();
				solenoid.set(DoubleSolenoid.Value.kForward);
				startTime = System.currentTimeMillis();
				autoStep = 1;
			}

			if (autoStep == 1) {
				// TODO: Set time in robot map
				if (System.currentTimeMillis() - startTime > 1000) {
					solenoid.set(DoubleSolenoid.Value.kOff);
				}
				if (encoder.driveStraightAuton(38)) {
					autoStep = 2;
				}
			}
			if (autoStep == 2) {
				if (pixyFunctionsGear.turnAndGoStraightAuton()) {
					autoStep = 3;
				}
				if (ultraData.distanceRight() < 10 || ultraData.distanceLeft() < 10) {
					autoStep = 3;
				}
			}

			if (autoStep == 3) {
				if (ultraFunctions.driveFowardAuton(4)) {
					autoStep = 4;
				}
			}
			if (autoStep == 4) {
				robotDriver.stop();
				startTime = System.currentTimeMillis();
			}
		} else if (autoMode.equals("R") || autoMode.equals("r")) {
			if (autoStep == 0) {
				encoder.initEncoders();
				solenoid.set(DoubleSolenoid.Value.kForward);
				startTime = System.currentTimeMillis();
				autoStep = 1;
			}

			if (autoStep == 1) {
				// TODO: Set time in robot map
				if (System.currentTimeMillis() - startTime > 1000) {
					solenoid.set(DoubleSolenoid.Value.kOff);
				}
				if (encoder.driveStraightAuton(57)) {
					autoStep = 2;
					currAngle = gyro.getAngle();
					encoder.initEncoders();
				}
			}
			if (autoStep == 2) {
				if (encoder.autonSixtyDegreeTurn(currAngle, true)) {
					autoStep = 3;
				}
			}
			if (autoStep == 3) {
				encoder.initEncoders();
				autoStep = 4;
			}
			if (autoStep == 4) {
				if (encoder.driveStraightAuton(21)) {
					autoStep = 5;
				}
			}
			if (autoStep == 5) {
				if (pixyFunctionsGear.turnAndGoStraightAuton()) {
					autoStep = 6;
				}
				if (ultraData.distanceRight() < 10 || ultraData.distanceLeft() < 10) {
					autoStep = 6;
				}
			}

			if (autoStep == 6) {
				if (ultraFunctions.driveFowardAuton(4)) {
					autoStep = 7;
				}
			}
			if (autoStep == 7) {
				robotDriver.stop();
			}
		} else if (autoMode.equals("2GB") || autoMode.equals("2gb")) {
			if (autoStep == 0) {
				encoder.initEncoders();
				solenoid.set(DoubleSolenoid.Value.kForward);
				startTime = System.currentTimeMillis();
				autoStep = 1;
			}

			if (autoStep == 1) {
				// TODO: Set time in robot map
				if (System.currentTimeMillis() - startTime > 1000) {
					solenoid.set(DoubleSolenoid.Value.kOff);
				}
				if (encoder.driveStraightAuton(38)) {
					autoStep = 2;
				}
			}
			if (autoStep == 2) {
				if (pixyFunctionsGear.turnAndGoStraightAuton()) {
					autoStep = 3;
				}
				if (ultraData.distanceRight() < 10 || ultraData.distanceLeft() < 10) {
					autoStep = 3;
				}
			}

			if (autoStep == 3) {
				if (ultraFunctions.driveFowardAuton(4)) {
					autoStep = 4;
				}
			}
			if (autoStep == 4) {
				robotDriver.stop();
				startTime = System.currentTimeMillis();
				Timer.delay(3);
				autoStep = 5;
			}
			if (autoStep == 5) {
				if (encoder.driveStraightAuton(-24)) {
					autoStep = 6;
					currAngle = gyro.getAngle();
					encoder.initEncoders();
				}
			}
			if (autoStep == 6) {
				if (encoder.autonNinetyDegreeTurn(currAngle, false)) {
					encoder.initEncoders();
					autoStep = 7;
				}
			}
			if (autoStep == 7) {
				// TODO change number 34, this is experimental
				if (encoder.driveStraightAuton(34)) {
					currAngle = gyro.getAngle();
					encoder.initEncoders();
					autoStep = 8;
				}
			}
			if (autoStep == 8) {
				if (encoder.autonNinetyDegreeTurn(currAngle, false)) {
					encoder.initEncoders();
					autoStep = 9;
				}
			}
			if (autoStep == 9) {
				if (encoder.driveStraightAuton(10)) {
					autoStep = 10;
				}
			}
			if (autoStep == 10) {
				if (ultraFunctions.selfStraight()) {
					autoStep = 11;
				}
			}
			if (autoStep == 11) {
				if (ultraFunctions.driveFowardAuton(4)) {
					autoStep = 12;
				}
			}
			if (autoStep == 12) {
				robotDriver.stop();
				Timer.delay(3);
				encoder.initEncoders();
				autoStep = 13;
			}
			if (autoStep == 13) {
				if (encoder.driveStraightAuton(-24)) {
					autoStep = 14;
					currAngle = gyro.getAngle();
					encoder.initEncoders();
				}
			}
			if (autoStep == 14) {
				if (encoder.autonNinetyDegreeTurn(currAngle, false)) {
					encoder.initEncoders();
					autoStep = 15;
				}
			}
			if (autoStep == 15) {
				// TODO change number 34, this is a number derived from using a
				// caliper to calculate pixels.
				if (encoder.driveStraightAuton(34)) {
					currAngle = gyro.getAngle();
					encoder.initEncoders();
					autoStep = 16;
				}
			}
			if (autoStep == 16) {
				if (encoder.autonNinetyDegreeTurn(currAngle, false)) {
					encoder.initEncoders();
					autoStep = 17;
				}
			}
			if (autoStep == 17) {
				if (encoder.driveStraightAuton(14)) {
					autoStep = 18;
				}
			}
			if (autoStep == 18) {
				if (pixyFunctionsGear.turnAndGoStraightAuton()) {
					autoStep = 19;
				}
				if (ultraData.distanceRight() < 10 || ultraData.distanceLeft() < 10) {
					autoStep = 19;
				}
			}

			if (autoStep == 19) {
				if (ultraFunctions.driveFowardAuton(4)) {
					autoStep = 20;
				}
			}
			if (autoStep == 20) {
				robotDriver.stop();
			}
		} else if (autoMode.equals("2GR") || autoMode.equals("2gr")) {
			if (autoStep == 0) {
				encoder.initEncoders();
				solenoid.set(DoubleSolenoid.Value.kForward);
				startTime = System.currentTimeMillis();
				autoStep = 1;
			}

			if (autoStep == 1) {
				// TODO: Set time in robot map
				if (System.currentTimeMillis() - startTime > 1000) {
					solenoid.set(DoubleSolenoid.Value.kOff);
				}
				if (encoder.driveStraightAuton(38)) {
					autoStep = 2;
				}
			}
			if (autoStep == 2) {
				if (pixyFunctionsGear.turnAndGoStraightAuton()) {
					autoStep = 3;
				}
				if (ultraData.distanceRight() < 10 || ultraData.distanceLeft() < 10) {
					autoStep = 3;
				}
			}

			if (autoStep == 3) {
				if (ultraFunctions.driveFowardAuton(4)) {
					autoStep = 4;
				}
			}
			if (autoStep == 4) {
				robotDriver.stop();
				startTime = System.currentTimeMillis();
				Timer.delay(3);
				autoStep = 5;
			}
			if (autoStep == 5) {
				if (encoder.driveStraightAuton(-24)) {
					autoStep = 6;
					currAngle = gyro.getAngle();
					encoder.initEncoders();
				}
			}
			if (autoStep == 6) {
				if (encoder.autonNinetyDegreeTurn(currAngle, true)) {
					encoder.initEncoders();
					autoStep = 7;
				}
			}
			if (autoStep == 7) {
				// TODO change number 34, this is experimental
				if (encoder.driveStraightAuton(34)) {
					currAngle = gyro.getAngle();
					encoder.initEncoders();
					autoStep = 8;
				}
			}
			if (autoStep == 8) {
				if (encoder.autonNinetyDegreeTurn(currAngle, true)) {
					encoder.initEncoders();
					autoStep = 9;
				}
			}
			if (autoStep == 9) {
				if (encoder.driveStraightAuton(10)) {
					autoStep = 10;
				}
			}
			if (autoStep == 10) {
				if (ultraFunctions.selfStraight()) {
					autoStep = 11;
				}
			}
			if (autoStep == 11) {
				if (ultraFunctions.driveFowardAuton(4)) {
					autoStep = 12;
				}
			}
			if (autoStep == 12) {
				robotDriver.stop();
				Timer.delay(3);
				encoder.initEncoders();
				autoStep = 13;
			}
			if (autoStep == 13) {
				if (encoder.driveStraightAuton(-24)) {
					autoStep = 14;
					currAngle = gyro.getAngle();
					encoder.initEncoders();
				}
			}
			if (autoStep == 14) {
				if (encoder.autonNinetyDegreeTurn(currAngle, true)) {
					encoder.initEncoders();
					autoStep = 15;
				}
			}
			if (autoStep == 15) {
				// TODO change number 34, this is a number derived from using a
				// caliper to calculate pixels.
				if (encoder.driveStraightAuton(34)) {
					currAngle = gyro.getAngle();
					encoder.initEncoders();
					autoStep = 16;
				}
			}
			if (autoStep == 16) {
				if (encoder.autonNinetyDegreeTurn(currAngle, true)) {
					encoder.initEncoders();
					autoStep = 17;
				}
			}
			if (autoStep == 17) {
				if (encoder.driveStraightAuton(14)) {
					autoStep = 18;
				}
			}
			if (autoStep == 18) {
				if (pixyFunctionsGear.turnAndGoStraightAuton()) {
					autoStep = 19;
				}
				if (ultraData.distanceRight() < 10 || ultraData.distanceLeft() < 10) {
					autoStep = 19;
				}
			}

			if (autoStep == 19) {
				if (ultraFunctions.driveFowardAuton(4)) {
					autoStep = 20;
				}
			}
			if (autoStep == 20) {
				robotDriver.stop();
			}
		}
	}

	/**
	 * Runs the motors with arcade steering.
	 */
	@Override
	public void operatorControl() {
		// pixyProc.pixyTestReset();
		// encoder.initEncoders();
		// pixyGearProc.pixyTestReset();
		gyro.resetGyro();
		// shooterServo.set(0.0);
		// servoBoy.set(0.0);
		encoder.initEncoders();
		// SmartDashboard.putNumber("Gyro Adjust", 0);
		// SmartDashboard.putNumber("Override Angle", 0);
		// SmartDashboard.putNumber("Turn Speed", 0);
		counter = 0;
		while (isOperatorControl() && isEnabled()) {

			relay.setDirection(Relay.Direction.kBoth);

			// Assorted data for testing, to see if sensors are working etc.
			SmartDashboard.putNumber("Ultra Right", ultraData.distanceRight());
			SmartDashboard.putNumber("Ultra Left", ultraData.distanceLeft());

			// Flywheel activated when trigger or thumb button are pressed
			if (stick.getRawButton(1) || stick.getRawButton(2)) {
				shooter.set(-.75);
				SmartDashboard.putNumber("Power to Shooter", stick.getRawAxis(3));
			} else {
				shooter.set(0);
			}

			// Stick button to enable the sweeper.
			// Also triggers with XBox right trigger
			if (stick.getRawButton(4) || stick.getRawButton(1) || xBox.getRawAxis(3) != 0) {
				sweeper.set(-1);
			} else {
				sweeper.set(0);
			}

			// Ball transport only active when trigger is held
			if (stick.getRawButton(1)) {
				transport.set(-1);
			} else {
				transport.set(0);
			}

			// ----------------------------------------------------------------------
			// // TODO do not delete this code use for calibration of gyro at
			// // competition
			// // TODO
			// // TODO do not delete this code use for calibration of gyro at
			// // competition
			// // TODO
			// // TODO do not delete this code use for calibration of gyro at
			// // competition
			// // TODO
			// //
			// ------------------------------------------------------------------------
			// // if(xBox.getRawButton(1)){
			// // if(step ==0){
			// // SmartDashboard.putNumber("Init Angle", currAngle);
			// // encoder.initEncoders();
			// // step =1;
			// // }
			// // if(step==1){
			// // if(encoder.calibrateTurnWithGyrosAndEncoders()){
			// // step =2;
			// // }
			// // if(step==2){
			// // SmartDashboard.putNumber("Change in angle",
			// // gyro.getAngle()-currAngle);
			// // robotDriver.stop();
			// // }
			// // }
			// // }else{
			// // currAngle = gyro.getAngle();
			// // step =0;
			// // robotDriver.stop();
			// // }
			// //
			// ----------------------------------------------------------------------
			// // TODO do not delete this code use for calibration of gyro at
			// // competition
			// // TODO
			// // TODO do not delete this code use for calibration of gyro at
			// // competition
			// // TODO
			// // TODO do not delete this code use for calibration of gyro at
			// // competition
			// // TODO
			// //
			// ------------------------------------------------------------------------

			if (xBox.getRawButton(1)) {
				// THIS CODE FUCKIN WORKS FOR THE PIXY CENTERING
				// HOLY SHIT WE DID IT BOYS WE REALLY FUCKIN DID IT

				if (pixyFunctionsGear.turnAndGoStraightAuton()) {
					if (ultraFunctions.driveFowardAuton(3)) {
						robotDriver.drive(0.05, 0, 1);
					}
				}
			}

			// Pressing the X Button and moving the left stick will activate
			// a 110 degree turn
			else if (xBox.getRawButton(RobotMap.loaderTurnButton)) {
				if (Math.abs(xBox.getRawAxis(RobotMap.loaderTurnAxis)) >= .25) {
					// by reaching this point, driver pressed button and pulled
					// stick.
					// this is for quick turn on robot.
					// buttons must be released for another quick turn
					if (step == 0) {
						currAngle = gyro.getAngle();
						encoder.initEncoders();
						step = 1;
					}
					if (step == 1) {
						if (xBox.getRawAxis(RobotMap.loaderTurnAxis) > .25) {
							left = true;
						} else if (xBox.getRawAxis(RobotMap.loaderTurnAxis) < -.25) {
							left = false;
						}
						if (encoder.loaderTurn(currAngle, left)) {
							step = 2;
						}
						if (step == 2) {
							robotDriver.stop();
						}
					}
				} else {
					robotDriver.stop();
				}

				// Pressing Y will invert the robot controls
				// for easier reverse driving
			} else if (xBox.getRawButton(RobotMap.flipperButt)) {
				if (step == 0) {
					step = 1;
					if (driveMod == 1) {
						driveMod = -1;
					} else {
						driveMod = 1;
					}
				}
			}

			// Defaults to manual driving. Also resets all steps and gyro
			else {
				gyro.resetGyro();
				step = 0;
				stepShooter = 0;
				gearLoadStep = 0;
				robotDriver.drive(xBox.getRawAxis(1) * straightMod, xBox.getRawAxis(4) * turnMod, driveMod);
			}

			// Watkins conditioning apparatus
			if (stick.getRawButton(RobotMap.enableRumbleButton)) {
				xBox.setRumble(RumbleType.kRightRumble, 1);
				xBox.setRumble(RumbleType.kLeftRumble, 1);
			} else {
				xBox.setRumble(RumbleType.kRightRumble, 0);
				xBox.setRumble(RumbleType.kLeftRumble, 0);
			}

			// Reads "twist" of the joystick to set the Azimuth of shooter
			if (Math.abs(stick.getZ()) > .4) {
				turret.set(stick.getZ() / 6);
			} else {
				turret.set(0);
			}

			// TODO: We've been having problems where we can only move one servo
			// at a time
			// All servo inputs maniplate just one servo
			// Need to get to the bottom of that and find out how we can move
			// two servos separately

			// Manipulates the servo up or down
			if (stick.getPOV(0) == 0) {
				shooterServo.increment(-1);
			}

			else if (stick.getPOV(0) == 180) {
				shooterServo.increment(1);
			}

			if (stick.getRawButton(5)) {
				servoBoy.increment(-1);
			}
			if (stick.getRawButton(3)) {
				servoBoy.increment(1);
			}

			// Should set the servos to full extension and retraction
			if (stick.getRawButton(8)) {
				shooterServo.set(1.0);
			}
			if (stick.getRawButton(7)) {
				shooterServo.set(0.0);
			}

			// Activates the climber, giving the Operator stick priority
			if (stick.getRawButton(6)) {
				double current = pdp.getCurrent(RobotMap.climberChannel);
				climber.set(1);
			} else if (xBox.getRawAxis(2) != 0) {
				climber.set(xBox.getRawAxis(2));
			} else {
				climber.set(0);
			}

		}

	}

	/**
	 * Runs during test mode
	 */
	@Override
	public void test() {

	}
}
