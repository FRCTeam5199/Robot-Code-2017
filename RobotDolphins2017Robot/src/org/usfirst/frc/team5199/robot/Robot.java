
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

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
@SuppressWarnings("unused")
public class Robot extends IterativeRobot {
	Pixy pixyGear, pixyShooter;
	PixyProcess pixyGearProc;
	PixyFunctions pixyFunctionsGear, pixyFunctionsShooter;
	CANTalon shooter;
	Spark leftMotor, rightMotor, climber, transport, sweeper;
	DoubleSolenoid solenoid;
	CANTalon turret;
	UltrasonicData ultraData;
	UltrasonicFunctions ultraFunctions;
	EncoderFunctions encoder;
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
	public static double straightMod = 1, turnMod = 1, driveMod = 1;
	public static int shiftStep = 0;
	public static int shifter = 0;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		pdp = new PowerDistributionPanel();
		rightMotor = new Spark(RobotMap.rightMotor);
		leftMotor = new Spark(RobotMap.leftMotor);
		shooter = new CANTalon(RobotMap.shooter);
		sweeper = new Spark(RobotMap.intake);
		transport = new Spark(RobotMap.transport);
		climber = new Spark(RobotMap.climber);
		turret = new CANTalon(RobotMap.turret);
		encoder = new EncoderFunctions(rightMotor, leftMotor, shooter);
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
		encoder.initEncoders();
		startTime = 0;
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		if (autoMode.equals("L") || autoMode.equals("l")) {
			// if (autoStep == 0) {
			// encoder.initEncoders();
			// autoStep = 1;
			// }
			// if (autoStep == 1) {
			// // TODO figure out number
			// if (encoder.driveForwardAuton(74)) {
			// autoStep = 2;
			// }
			// }
			// if (autoStep == 2) {
			// gyro.initGyro();
			// autoStep = 3;
			// }
			// if (autoStep == 3) {
			// if (gyro.moveDegreesAuton(60)) {
			// autoStep = 4;
			// }
			// }
			// if(autoStep ==4){
			// encoder.initEncoders();
			// autoStep = 5;
			// }
			// if(autoStep ==5){
			// if(encoder.driveStraightAuton(29)){
			// autoStep =6;
			// }
			// }
			// if(autoStep ==6){
			// if(ultraFunctions.driveFowardAuton(16)){
			// autoStep =7;
			// }
			// }
			// if(autoStep ==7){
			// robotDriver.stop();
			// }

			// if (autoStep == 4) {
			// if (pixyFunctionsGear.turnAndGoStraightAuton()) {
			// autoStep = 5;
			// }
			// } else if (autoStep == 5) {
			// if (!pixyFunctionsGear.turnAndGoStraightAuton()) {
			// autoStep = 4;
			// }
			// if (ultraFunctions.driveFowardAuton(14)) {
			// autoStep = 6;
			// }
			// }
			// if (autoStep == 6) {
			// robotDriver.stop();
			// }

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
				if (encoder.driveStraightAuton(45)) {
					autoStep = 2;
				}
			}
			if (autoStep == 2) {
				// if (ultraFunctions.selfStraight()) {
				autoStep = 3;
			}

			if (autoStep == 3) {
				if (ultraFunctions.driveFowardAuton(4)) {
					autoStep = 4;
				}
			}
			if (autoStep == 4) {
				robotDriver.stop();
			}
		}
		// if (autoStep == 1) {
		// if (pixyFunctionsGear.turnAndGoStraightAuton()) {
		// autoStep = 2;
		// }
		// if (ultraFunctions.goBackTooClosePixyAuton()) {
		// autoStep = 3;
		// }
		// }
		// if (autoStep == 2) {
		// if (!pixyFunctionsGear.checkIfAlignedGear()) {
		// autoStep = 1;
		// }
		// if (ultraFunctions.driveFowardAuton(14)) {
		// autoStep = 4;
		// }
		// }
		// if (autoStep == 3) {
		// if (!ultraFunctions.goBackTooClosePixyAuton()) {
		// autoStep = 0;
		// }
		// }
		// double time = 0;
		// if (autoStep == 4) {
		// time = System.currentTimeMillis();
		// autoStep = 5;
		// robotDriver.stop();
		// }
		// if (autoStep == 5) {
		// if ((System.currentTimeMillis() - time) > 3000) {
		// autoStep = 6;
		// encoder.initEncoders();
		// }
		// }
		// if (autoStep == 6) {
		// if (encoder.driveBackwardsAuton(18)) {
		// autoStep = 7;
		// }
		// }
		// if (autoStep == 7) {
		// if (gyro.moveDegreesAuton(90)) {
		// autoStep = 8;
		// encoder.initEncoders();
		// }
		// }
		// if (autoStep == 8) {
		// if (encoder.driveForwardAuton(6)) {
		// autoStep = 9;
		// }
		// }
		// if (autoStep == 9) {
		// if (gyro.moveDegreesAuton(90)) {
		// autoStep = 10;
		// }
		// }
		// if (autoStep == 10) {
		// if (ultraFunctions.driveFowardAuton(4)) {
		// autoStep = 11;
		// }
		// }
		// if (autoStep == 11) {
		// robotDriver.stop();
		// }
		else if (autoMode.equals("R") || autoMode.equals("r")) {
		}

		// else if(autoMode.equals(arg0));
	}

	public void autonomousPeriodicState() {
	}

	@Override
	public void teleopInit() {
		// pixyProc.pixyTestReset();
		// encoder.initEncoders();
		// pixyGearProc.pixyTestReset();
		shooterServo.set(0.0);
		servoBoy.set(0.0);
		encoder.initEncoders();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {

		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		pixyGearProc.pixyI2CTest();
		pixyGearProc.pixyTest();

		// if(stick.getRawButton(11)){
		// if(stepShooter ==0){
		// stepShooter= 1;
		// if(shooterSpeed==2000){
		// shooterSpeed = 4000;
		// }else if(shooterSpeed==4000){
		// shooterSpeed = 6000;
		// }else{
		// shooterSpeed=2000;
		// }
		//
		// }else{
		// stepShooter =0;
		// }

		SmartDashboard.putNumber("ultra left", ultraData.distanceLeft());
		SmartDashboard.putNumber("ultra right", ultraData.distanceRight());
		// pixyGearProc.xPos();
		SmartDashboard.putNumber("servo", servo.get());
		// // pixyGearProc.averageData(0, true);
		//
		// // Trigger shoots... It enables flywheel, transport and sweeper to
		// // create flow
		// // Thumb button spins up flywheel
		// // Joystick buttons 3 and 4, as well as the right trigger activate
		// the
		// // sweeper
		//

		if (stick.getRawButton(1) || stick.getRawButton(2)) {
			shooter.set(-.75);
			SmartDashboard.putNumber("Power to Shooter", stick.getRawAxis(3));
			encoder.RPMs();
		} else {
			shooter.set(0);
		}
		//// Stick button to enable the sweeper
		if (stick.getRawButton(4) || stick.getRawButton(1) || xBox.getRawAxis(3) != 0) {
			sweeper.set(-1);
		} else {
			sweeper.set(0);
		}
		// //
		if (stick.getRawButton(1)) {
			transport.set(-1);
		} else {
			transport.set(0);
		}

		// if (xBox.getRawButton(6)) {
		// if (shiftStep == 0) {
		// shiftStep = 1;
		// if (shifter == 0) {
		// shifter = 1;
		// } else {
		// shifter = 0;
		// }
		// }
		// shifter = 1;
		// } else {
		//// shiftStep = 0;
		// shifter =0;
		// }

		// if (xBox.getRawButton(9)) {
		// gearShift.set(DoubleSolenoid.Value.kForward);
		// } else {
		// if (shifter == 1) {
		// gearShift.set(DoubleSolenoid.Value.kReverse);
		// } else {
		// gearShift.set(DoubleSolenoid.Value.kOff);
		// }
		// }
		// SmartDashboard.putNumber("Shifter", shifter);

		if (xBox.getRawButton(3)) {
			SmartDashboard.putString("I love robotics", "Turn me right");
			gyro.moveDegrees(60, currAngle);
		} else if (xBox.getRawButton(4)) {
			SmartDashboard.putString("I love robotics", "Turn me left");
			gyro.moveDegrees(-60, currAngle);

			// TODO: fix *4 fudge factor
		} else if (xBox.getRawButton(1)) {
			gyro.moveDegreesAuton(180, currAngle);
			encoder.RPMs();
		}  else if (stick.getRawButton(9)) {
			rightMotor.set(-.5);
		} else if(xBox.getRawButton(6)&&xBox.getRawAxis(1)!=0){
			if(gearLoadStep==0){
			if(ultraFunctions.selfStraight()){
				gearLoadStep =1;
			}
			}
			if(gearLoadStep==1){
				if(ultraFunctions.driveFowardAuton(1)){
					gearLoadStep =2;
				}
			}
			if(gearLoadStep ==2){
				robotDriver.stop();
			}
		}else if(stick.getRawButton(RobotMap.encoderTestDriveButton)){  //This is a test using button 12
			if(step==0){
				encoder.initEncoders();
				step =1;
			}
			if(step==1){
				if(encoder.driveStraightAuton(80)){
					step =2;
				}
			}
			if(step ==2){
				robotDriver.stop();
			}
		}else if(stick.getRawButton(RobotMap.enableRumbleButton)){
			xBox.setRumble(RumbleType.kRightRumble, 1);
			xBox.setRumble(RumbleType.kLeftRumble, 1);
		}else{
			xBox.setRumble(RumbleType.kRightRumble, 0);
			xBox.setRumble(RumbleType.kLeftRumble, 0);
			step = 0;
			stepShooter = 0;
			gearLoadStep =0;
			currAngle = gyro.getAngle();
			robotDriver.drive(xBox.getRawAxis(1) * straightMod, xBox.getRawAxis(4) * turnMod, driveMod);
		}
		
		
		// if (xBox.getRawButton(4)) {
		// if (step == 0) {
		// step = 1;
		// if (flipped) {
		// flipped = false;
		// } else {
		// flipped = true;
		// }
		// }
		// } else {
		// step = 0;
		// }
		// if (flipped) {
		// driveMod = -1;
		// } else {
		// driveMod = 1;
		// }
		//
		// //
		// //
		// // // // Reads "twist" of the joystick to set the Azimuth of shooter
		// //
		if (Math.abs(stick.getZ()) > .4) {
			turret.set(stick.getZ() / 6);
		} else {
			turret.set(0);
		}
		//
		// //
		// // // Set Linear servo to full extension or full retraction
		//
		if (stick.getPOV(0) == 0) {
			shooterServo.increment(-1);
		}

		else if (stick.getPOV(0) == 180) {
			shooterServo.increment(1);
		}

		if (stick.getRawButton(8)) {
			shooterServo.set(1.0);
		}
		if (stick.getRawButton(7)) {
			shooterServo.set(0.0);
		}

		if (stick.getRawButton(5)) {
			servoBoy.increment(-1);
		}
		if (stick.getRawButton(3)) {
			servoBoy.increment(1);
		}

		// XBox trigger (left) to enable climber
//		if (stick.getRawButton(6)) {
//			climber.set(1);
//		} else if (xBox.getRawAxis(2) != 0) {
//			climber.set(xBox.getRawAxis(2));
//		} else {
//			climber.set(0);
//		}
		if (stick.getRawButton(6)) {
			double current = pdp.getCurrent(RobotMap.climberChannel);
			climber.set(1);
		} else if (xBox.getRawAxis(2) != 0) {
			climber.set(xBox.getRawAxis(2));
		} else {
			climber.set(0);
		}

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		if (first) {
			first = false;
			pixyFunctionsGear.alignGearPegTest();
		}

	}
}
