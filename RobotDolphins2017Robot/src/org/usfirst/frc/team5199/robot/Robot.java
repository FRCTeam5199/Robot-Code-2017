
package org.usfirst.frc.team5199.robot;

import org.opencv.core.Mat;

import com.ctre.CANTalon;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	Pixy pixyGear, pixyShooter;
	PixyFunctions pixyFunctionsGear, pixyFunctionsShooter;
	TalonSRX shooter;
	Spark leftMotor, rightMotor, climber, transport, sweeper;
	CANTalon turret;
	UltrasonicData ultraData;
	UltrasonicFunctions ultraFunctions;
	EncoderFunctions encoder;
	RobotDrive robotDriver;
	Joystick xBox, stick;
	GyroFunctions gyro;
	Servo servo;
	ServoDude servoDude;
	double strokePos;
	String autoMode;
	Compressor compressor;
	int autoStep;
	CameraServer server;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		rightMotor = new Spark(RobotMap.rightMotor);
		leftMotor = new Spark(RobotMap.leftMotor);
		shooter = new TalonSRX(RobotMap.shooter);
		sweeper = new Spark(RobotMap.intake);
		transport = new Spark(RobotMap.transport);
		climber = new Spark(RobotMap.climber);
		turret = new CANTalon(RobotMap.turret);
		encoder = new EncoderFunctions(rightMotor, leftMotor);
		ultraData = new UltrasonicData(RobotMap.ultraRightEcho, RobotMap.ultraRightPing, RobotMap.ultraLeftEcho,
				RobotMap.ultraLeftPing);
		ultraFunctions = new UltrasonicFunctions(ultraData, rightMotor, leftMotor);
		robotDriver = new RobotDrive(rightMotor, leftMotor);
		stick = new Joystick(RobotMap.joyStickPort);
		xBox = new Joystick(RobotMap.xBoxPort);
		gyro = new GyroFunctions(rightMotor, leftMotor);
		pixyGear = new Pixy(RobotMap.pixyGear);
		pixyShooter = new Pixy(RobotMap.pixyShoot);
		servo = new Servo(RobotMap.shooterServo);
		servoDude = new ServoDude(servo);
		strokePos = 0.0;
		server = CameraServer.getInstance();
		server.startAutomaticCapture(0);

		SmartDashboard.putString("Autonomous Mode", "Enter Value");
		// pixyFunctionsGear = new PixyFunctions(pixyGear, ultraFunctions,
		// encoder);
		// TODO change the parameters of the pixyFunctions to give it the
		// controllers necessary to shoot
		// pixyFunctionsShooter= new PixyFunctions(pixyShooter);
		// Thread t = new Thread(() -> {
		//
		// boolean allowCam1 = false;
		//
		// UsbCamera camera1 =
		// CameraServer.getInstance().startAutomaticCapture(0);
		// camera1.setResolution(320, 240);
		// camera1.setFPS(30);
		// UsbCamera camera2 =
		// CameraServer.getInstance().startAutomaticCapture(1);
		// camera2.setResolution(320, 240);
		// camera2.setFPS(30);
		//
		// CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
		// CvSink cvSink2 = CameraServer.getInstance().getVideo(camera2);
		// CvSource outputStream =
		// CameraServer.getInstance().putVideo("Switcher", 320, 240);
		//
		// Mat image = new Mat();
		//
		// while (!Thread.interrupted()) {
		//
		// if (xBox.getRawButton(5)) {
		// allowCam1 = !allowCam1;
		// }
		//
		// if (allowCam1) {
		// cvSink2.setEnabled(false);
		// cvSink1.setEnabled(true);
		// cvSink1.grabFrame(image);
		// } else {
		// cvSink1.setEnabled(false);
		// cvSink2.setEnabled(true);
		// cvSink2.grabFrame(image);
		// }
		//
		// outputStream.putFrame(image);
		// }
		//
		// });
		//// t.start();

	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		compressor = new Compressor(0);
		compressor.setClosedLoopControl(true);

	}

	@Override
	public void disabledPeriodic() {
		compressor.setClosedLoopControl(true);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autoMode = SmartDashboard.getString("Autonomous Mode");
		autoStep = 0;
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		if (autoMode.equals("L") || autoMode.equals("l")) {
			if (autoStep == 0) {
				if (encoder.driveForwardAuton(24)) {
					autoStep = 1;
				}
			} else if (autoStep == 1) {
				if (!PixyFunctions.turnAndGoStraightAuton()) {
					autoStep = 0;
				}
				if (ultraFunctions.driveFowardGearLoading()) {
					autoStep = 2;
				}
			}
			if (autoStep == 2) {
				robotDriver.stop();
			}

		} else if (autoMode.equals("C") || autoMode.equals("c")) {
			if (autoStep == 0) {
				if (PixyFunctions.turnAndGoStraightAuton()) {
					autoStep = 1;
				}
			} else if (autoStep == 1) {
				if (!PixyFunctions.turnAndGoStraightAuton()) {
					autoStep = 0;
				}
				if (ultraFunctions.driveFowardGearLoading()) {
					autoStep = 2;
				}
			}
			if (autoStep == 2) {
				robotDriver.stop();
			}
		} else if (autoMode.equals("R") || autoMode.equals("r")) {

		}

	}

	@Override
	public void teleopInit() {
		// pixyProc.pixyTestReset();
		encoder.initEncoders();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {

		// if (stick.getRawAxis(1) < -0.1) {
		// strokePos = strokePos + 0.003;
		// if (strokePos > 1.0) {
		// strokePos = 1.0;
		// }
		// }
		//
		// if (stick.getRawAxis(1) > 0.1) {
		// strokePos = strokePos - 0.003;
		// if (strokePos < 0.0) {
		// strokePos = 0.0;
		// }
		// }
		// servo.set(strokePos);

		// This is for encoder testing.
		// driveForward drives forward a specific distance (in inches)
		// the function accelerates in 3 steps and decelerates in 3 steps.
		// Configuration of the steps is done in RobotMap.
		// if (stick.getRawButton(12)) {
		// encoder.driveForward(200 * Math.PI);
		// }

		// Trigger shoots... It enables flywheel, transport and sweeper to
		// create flow
		// Thumb button spins up flywheel
		// Joystick buttons 3 and 4, as well as the right trigger activate the
		// sweeper

		if (stick.getRawButton(1) || stick.getRawButton(2)) {
			shooter.set(1);
		} else {
			shooter.set(0);
		}

		// Stick button to enable the sweeper
		if (stick.getRawButton(4) || stick.getRawButton(3) || stick.getRawButton(1) || xBox.getRawAxis(3) != 0) {
			sweeper.set(-1);
		} else {
			sweeper.set(0);
		}

		if (stick.getRawButton(1)) {
			transport.set(-1);
		} else {
			transport.set(0);
		}

		if (xBox.getRawButton(3)) {
			SmartDashboard.putString("I love robotics", "Turn me left");
			gyro.moveDegreesTest(-90);

		}
		if (xBox.getRawButton(4)) {
			SmartDashboard.putString("I love robotics", "Turn me right");
			gyro.moveDegreesTest(90);

		}

		// Reads "twist" of the joystick to set the Azimuth of shooter
		turret.set(stick.getZ() / 6);

		// Set Linear servo to full extension or full retraction
		if (stick.getRawButton(7)) {
			servo.set(0.0);
		} else if (stick.getRawButton(8)) {
			servo.set(1.0);
		}

		// Manual extension/retraction for servo using stick
		if (stick.getRawAxis(1) != 0) {
			servoDude.increment(stick.getRawAxis(1));
		}

		// Uses sticks on xbox to set the forward/reverse and turning
		robotDriver.drive(xBox.getRawAxis(1), xBox.getRawAxis(4) * .6, 1);
	}

	// XBox trigger (left) to enable climber
	// climber.set(xBox.getRawAxis(2));

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {

	}
}
