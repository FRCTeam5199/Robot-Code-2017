package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;

import java.sql.Driver;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
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
 * instead if you're new.ss
 */
public class Robot extends SampleRobot {
	DataBank bank = new DataBank();
	int step = 0;
	public Victor right = bank.right, left = bank.left;
	CANTalon turret = new CANTalon(0);
	Joystick stick = new Joystick(1);
	Joystick xBox = new Joystick(0);
	Relay exampleRelay = new Relay(0);
	GyroFunctions gyro = new GyroFunctions(right, left);
	boolean lightOn = false;
	UltrasonicFunctions ultraFunctions;
	// Servo pwmLED = new Servo(8);
	int PWMSheez = 0;
	double EncoderNum, count = 0;
	// Jaguar JagMan = new Jaguar(4);
	CANTalon JagMan = new CANTalon(1);
	double JagPow = 0;
	private Relay relay;
	private PixyFunctions pixyGearFunc, pixyShooterFunc;
	RobotDrive Driver = new RobotDrive(right, left);
	boolean shooting = false;
	public static int counter = 0;
	public static long start = 0;
	public static final int ultrasonicArraySize = 100;
	public static boolean firstBufferRight = true;
	public static double sumBufferRight = 0;
	public static int counterRight = 0;
	public static Double[] distanceArrayRight;
	public static boolean leftTurn = true;
	int autoStep = 0;

	public Robot() {
	}

	@Override
	public void robotInit() {
		ultraFunctions = new UltrasonicFunctions(bank.ultraData, right, left);
		pixyGearFunc = new PixyFunctions(bank.pixyGear, ultraFunctions, bank.driveEncoders, Driver);
		pixyShooterFunc = new PixyFunctions(bank.pixyShooter, turret);
		// CameraServer.getInstance().startAutomaticCapture();
		distanceArrayRight = new Double[ultrasonicArraySize];
		for (int i = 0; i < ultrasonicArraySize; i++) {
			distanceArrayRight[i] = 0.0;
		}
		JagPow = 0;
		bank.time = System.currentTimeMillis();
		SmartDashboard.putNumber("Pixy Alignment Subtraction", 160);

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
		autoStep = 0;
		double AVG = 0;
		while (isAutonomous() && isEnabled()) {
			if(autoStep >= 1){
				pixyShooterFunc.alignShooterX();
			}
			
			AVG = bank.shooterRPM();
			// For the close gear peg position
			if (AVG < 1000) {
				JagMan.set(1);
			} else if (AVG < 1500) {
				JagMan.set(.85);
			} else if (AVG < 2000) {
				JagMan.set(.6);
			} else if (AVG < 2300) {
				JagMan.set(.49);
			} else if (AVG < 3350) {
				JagPow += 0.00025;
				JagMan.set(JagPow + .49);
			} else {
				JagMan.set(.49);
				JagPow -= 0.0001;
			}
			
			if (autoStep == 0) {
				Driver.drive(.3, 0, 1);
				Timer.delay(3.75);
				Driver.stop();
				Timer.delay(.5);
				autoStep = 1;
			}
			if (autoStep == 1) {
				gyro.moveDegreesAuton(-60, 0);
				Timer.delay(.5);
				autoStep = 2;

			}
			if (autoStep == 2) {
				if (bank.ultraDistanceLeft() > 6) {
					if (pixyGearFunc.turnAndGoStraightAuton()) {
						Driver.drive(0.4, 0, 1);
					}
				} else {
					if (ultraFunctions.driveFowardAuton(3)) {
						autoStep = 3;
					}
				}
			}
			if (autoStep == 3) {
				Driver.stop();
			}

		}

	}

	/**
	 * Runs the motors with arcade steering.
	 */
	@Override
	public void operatorControl() {
		double AVG = 0;
		bank.time = System.currentTimeMillis();
		lightOn = false;
		bank.pixyShooterProc.pixyTestReset();
		bank.pixyGearProc.pixyTestReset();
		start = System.currentTimeMillis();
		double currAngle = 0.0;
		while (isOperatorControl() && isEnabled()) {
			SmartDashboard.putNumber("Gyro", gyro.gyro.getAngle());
			AVG = bank.shooterRPM();
			// AVG = bank.flywheel.getRate();
			bank.pixyShooterProc.pixyShooterI2CTest();
			bank.pixyShooterProc.pixyShooterTest();
			bank.pixyGearProc.pixyGearI2CTest();
			bank.pixyGearProc.pixyGearTest();
			SmartDashboard.putNumber("Left Ultra", bank.ultraDistanceLeft());
			SmartDashboard.putNumber("Right Ultra", bank.ultraDistanceRight());
			if (xBox.getRawButton(1)) {
				if (bank.ultraDistanceLeft() > 6) {
					if (pixyGearFunc.turnAndGoStraightAuton()) {
						Driver.drive(0.4, 0, 1);
					}
				} else {
					ultraFunctions.driveFowardAuton(3);
				}

			} else if (xBox.getRawButton(RobotMap.loaderTurnButton)) {
				gyro.moveDegreesAuton(-60, 0);
				// if (Math.abs(xBox.getRawAxis(RobotMap.loaderTurnAxis)) >=
				// .25) {
				// // by reaching this point, driver pressed button and pulled
				// // stick.
				// // this is for quick turn on robot.
				// // buttons must be released for another quick turn
				// if (step == 0) {
				// currAngle = gyro.getAngle();
				// bank.driveEncoders.initEncoders();
				// step = 1;
				// }
				// if (step == 1) {
				// if (xBox.getRawAxis(RobotMap.loaderTurnAxis) > .25) {
				// leftTurn = true;
				// } else if (xBox.getRawAxis(RobotMap.loaderTurnAxis) < -.25) {
				// leftTurn = false;
				// }
				// // if (driveEncoders.loaderTurn(currAngle, left)){
				// if (bank.driveEncoders.autonSixtyDegreeTurn(currAngle,
				// !leftTurn)) {
				// step = 2;
				// }
				// if (step == 2) {
				// Driver.stop();
				// }
				// }
			}
			// Pressing Y will invert the robot controls
			// for easier reverse driving
			else {
				gyro.resetGyro();
				Driver.drive(xBox.getRawAxis(1) * -1, xBox.getRawAxis(4) * .6, 1);
			}

			if (stick.getRawButton(3)) {
				PWMSheez -= 10;
			}
			if (stick.getRawButton(5)) {
				PWMSheez += 10;
			}
			if (stick.getRawButton(4) || xBox.getRawButton(5)) {
				pixyShooterFunc.alignShooterX();
			} else if (Math.abs(stick.getZ()) > .15) {
				turret.set(stick.getZ() / 5 * stick.getThrottle());
				SmartDashboard.putNumber("Stick Turn", stick.getZ() / stick.getThrottle());
			} else {
				turret.set(0);
			}
			// if (stick.getRawButton(1)) {
			// if (step == 0) {
			// shooting = !shooting;
			// step = 1;
			// }
			// }
			// else{
			// step = 0;
			// }
			// if (stick.getRawButton(11)) {
			// xBox.setRumble(RumbleType.kRightRumble, 1);
			// xBox.setRumble(RumbleType.kLeftRumble, 1);
			// } else {
			// xBox.setRumble(RumbleType.kRightRumble, 0);
			// xBox.setRumble(RumbleType.kLeftRumble, 0);
			// }

			if (stick.getRawButton(9)) {
				// For the close gear peg position
				if (AVG < 1000) {
					JagMan.set(1);
				} else if (AVG < 1500) {
					JagMan.set(.85);
				} else if (AVG < 2000) {
					JagMan.set(.6);
				} else if (AVG < 2300) {
					JagMan.set(.49);
				} else if (AVG < 3350 + (100 * stick.getThrottle())) {
					JagPow += 0.00025;
					JagMan.set(JagPow + .49);
				} else {
					JagMan.set(.49);
					JagPow -= 0.0001;
				}
			} else if (stick.getRawButton(7)) {
				// For the boiler hopper position
				if (AVG < 1000) {
					JagMan.set(1);
				} else if (AVG < 1500) {
					JagMan.set(.85);
				} else if (AVG < 2000) {
					JagMan.set(.8);
				} else if (AVG < 2500) {
					JagMan.set(.66);
				} else if (AVG < 3000) {
					JagPow += 0.00001;
					JagMan.set(JagPow + .66);
				} else {
					JagPow -= 0.000005;
					JagMan.set(.66);
				}
				SmartDashboard.putNumber("Jag Power", JagPow + .66);

			} else if (stick.getRawButton(11)) {
				// For the center gear position
				// Last calibrated 3/19/17 at Code Orange approx 4:30
				if (AVG < 1500) {
					JagMan.set(1);
				} else if (AVG < 2000) {
					JagMan.set(.9);
				} else if (AVG < 2500) {
					JagMan.set(.8);
				} else if (AVG < 3000) {
					JagMan.set(.52);
				} else if (AVG < 3575 + (100 * stick.getThrottle())) {
					JagPow += 0.0003;
					JagMan.set(JagPow + .52);
				} else {
					JagMan.set(.52);
					JagPow -= 0.0001;
				}
			} else if (stick.getRawButton(2)) {
				JagMan.set(.3);
			} else {
				JagMan.set(0);
				JagPow = 0;
				SmartDashboard.putString("Stage:", "0");
			}

			SmartDashboard.putNumber("Encoder Rate", AVG);

			// pwmLED.setRaw(PWMSheez);
			// if(stick.getRawButton(10)){
			// exampleRelay.set(Relay.Value.kForward);
			// }
			// else{
			// exampleRelay.set(Relay.Value.kReverse);
			//
			// if (stick.getRawButton(10)) {
			// // && (System.currentTimeMillis() - time) > 250) {
			// if (step == 0) {
			// lightOn = !lightOn;
			// time = System.currentTimeMillis();
			// step = 1;
			// }
			// } else {
			// step = 0;
			// }
			lightOn = true;
			if (lightOn) {
				exampleRelay.set(Relay.Value.kReverse);
			} else {
				exampleRelay.set(Relay.Value.kOff);
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
