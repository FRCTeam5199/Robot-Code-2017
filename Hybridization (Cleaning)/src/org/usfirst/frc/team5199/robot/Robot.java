package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	// Unlimited Variable Works - the infinite creation of objects

	// DataBank boys
	DataBank bank = new DataBank();

	// Auto Selection stuff
	Command autonomousCommand;
	SendableChooser<String> autoChooser;

	// Controllers
	private static Joystick xBox;
	private static Joystick stick;

	// Motor Zone vrmvrmvrrrrrrrrrrrrrrrrrmmmmm
	// ************************************************************************************
	private static Spark right;
	private static Spark left;
	private static CANTalon turret;
	private static CANTalon shooter;
	private static Spark sweeper;
	private static Spark transport;
	private static Spark climber;
	private static RobotDrive Driver;
	// ***********************************************************************************

	// Ultra functions
	private static UltrasonicFunctions ultraFunctions;

	// Gyro
	private static GyroFunctions gyro;

	// Encoder Functions
	private static EncoderShooterFunctions shooterFunc;
	private static EncoderDriveFunctions driveFunc;

	// Pixy Functions
	private static PixyFunctions pixyGearFunc, pixyShooterFunc;

	// Relay for SUPERBRIGHTLEDs
	private static Relay relay;

	// Servo shit
	private static Servo shooterServo = new Servo(3);
	private static 	ServoDude hood = new ServoDude(shooterServo);
	// why don't you ever fucking work god damnit

	// Bool's realm 64
	// ***************************************************************************
	boolean lightOn = false;
	boolean shooting = false;
	boolean autoTrack = false;
	// ***************************************************************************

	// Important Numbers Zone
	int autoMode = 0;
	int autoStep = 0;
	int step = 0;
	int shootMode = 0;

	public Robot() {
	}

	@Override
	public void robotInit() {
		// Camera Creation
		UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
		camera1.setResolution(640, 360);
		camera1.setFPS(15);

		// Still no guitar hero :(
		xBox = new Joystick(0);
		stick = new Joystick(1);

		// Why don't we just manually turn the wheels instead?
		right = bank.right;
		left = bank.left;
		turret = new CANTalon(1);
		shooter = new CANTalon(0);
		sweeper = new Spark(RobotMap.intake);
		transport = new Spark(RobotMap.transport);
		climber = new Spark(RobotMap.climber);
		Driver = new RobotDrive(right, left);

		// Gyro is crash
		gyro = new GyroFunctions(right, left);

		// Encoders
		shooterFunc = new EncoderShooterFunctions(shooter, turret, bank);
		driveFunc = new EncoderDriveFunctions(right, left, gyro);

		// Mudamudamudamudamudamuda servos
		shooterServo = new Servo(3);
		hood = new ServoDude(shooterServo);

		// Using the beta capsule, Hayata becomes.... ULTRAMAN
		ultraFunctions = new UltrasonicFunctions(bank.ultraData, right, left);

		// I CAN SEE, I CAN FIGHT!
		pixyGearFunc = new PixyFunctions(bank.pixyGear, ultraFunctions, bank.driveEncoders, Driver);
		pixyShooterFunc = new PixyFunctions(bank.pixyShooter, turret);

		// Relay race go
		relay = new Relay(0);

		bank.timeReset();

		// Highly experimental autonomous selection
		// Maybe if this doesn't work, we can attach a cell phone to a microwave
		autoChooser = new SendableChooser();
		autoChooser.addDefault("Do nothing pls no pick this one fam", "jack shit");
		autoChooser.addObject("Center", "center");
		autoChooser.addObject("Left", "left");
		autoChooser.addObject("Right", "right");

		SmartDashboard.putData("Autonomous Selection", autoChooser); // Maybe we
																		// display
																		// this
																		// shit?

	}

	@Override
	public void autonomous() {

		autoStep = 0;
		String auto = null;

		while (isAutonomous() && isEnabled()) {
			// TODO
			// Add super smooth Ultra numbers that I found out before,
			// Somewhere on the A button
			
			auto = autoChooser.getSelected().toString();
			//"Will you cancel your auton?"
			//	"NO NO NO"
			//"Are you going to miss placing the gear?"
			//	"NO NO NO"
			//"Are you going to do the side auton?"
			//	"YES YES YES"
			//"Are you going to do the shooting thing?"
			//	"YES YES YES! YES!"
			
			// autoMode = (int) SmartDashboard.getNumber("Auto Mode:");
			if (auto.equals("right")) {
				if (autoStep >= 1) {
					pixyShooterFunc.alignShooterX();
				}

				shooterFunc.boilerPegShoot();

				if (autoStep == 0) {
					Driver.drive(.42, 0, 1);
					Timer.delay(2.25);
					Driver.stop();
					Timer.delay(.5);
					autoStep = 1;
				}
				if (autoStep == 1) {
					if (!(Math.abs(bank.pixyGearXPos() - 160) < 7)) {
						Driver.drive(0, -.35, 1);
					} else {
						Driver.stop();
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					if (bank.ultraDistanceLeft() > 6) {
						if (pixyGearFunc.turnAndGoStraightAuton()) {
							Driver.drive(0.5, 0, 1);
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

			} else if (auto.equals("left")) {
				// No shooting code
				// TODO

				if (autoStep == 0) {
					Driver.drive(.42, 0, 1);
					Timer.delay(2.25);
					Driver.stop();
					Timer.delay(.5);
					autoStep = 1;
				}
				if (autoStep == 1) {
					if (!(Math.abs(bank.pixyGearXPos() - 160) < 7)) {
						Driver.drive(0, .25, 1);
					} else {
						Driver.stop();
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					if (bank.ultraDistanceLeft() > 6) {
						if (pixyGearFunc.turnAndGoStraightAuton()) {
							Driver.drive(0.5, 0, 1);
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
			} else if (auto.equals("center")) {
				if (autoStep >= 1) {
					pixyShooterFunc.alignShooterXCenter();
				}
				shooterFunc.centerPegShoot();

				if (autoStep == 0) {
					Driver.drive(.3, 0, 1);
					Timer.delay(.75);
					Driver.stop();
					Timer.delay(.25);
					autoStep = 1;
				}

				if (autoStep == 1) {
					if (bank.ultraDistanceLeft() > 6) {
						if (pixyGearFunc.turnAndGoStraightAuton()) {
							Driver.drive(0.4, 0, 1);
						}
					} else {
						if (ultraFunctions.driveFowardAuton(3)) {
							autoStep = 2;
						}
					}
				}

				if (autoStep == 2) {
					Driver.stop();
				}
			}
		}

	}

	/**
	 * Runs the motors with arcade steering.
	 */

	@Override
	public void operatorControl() {
		// Initializing teleop dongers

		int lightStep = 0;
		lightOn = false;

		hood.set(0);

		bank.time = System.currentTimeMillis();

		bank.pixyShooterProc.pixyTestReset();
		bank.pixyGearProc.pixyTestReset();

		while (isOperatorControl() && isEnabled()) {
			// All systems go
			// You may raise dongers when ready

			// Display info so we can accurately measure our E-Peen
			SmartDashboard.putNumber("ServoDude", hood.position);
			SmartDashboard.putNumber("Encoder Rate", bank.shooterRPM());
			SmartDashboard.putNumber("Turret Encoder", turret.getPosition());
			SmartDashboard.putNumber("Left Ultra", bank.ultraDistanceLeft());
			SmartDashboard.putNumber("Right Ultra", bank.ultraDistanceRight());
			bank.pixyShooterProc.pixyShooterI2CTest();
			bank.pixyShooterProc.pixyShooterTest();
			bank.pixyGearProc.pixyGearI2CTest();
			bank.pixyGearProc.pixyGearTest();

			// Toggle for light sphincters
			if (stick.getRawButton(8) || xBox.getRawButton(10)) {
				if (lightStep == 0) {
					lightOn = !lightOn;
					lightStep = 1;
				}
			} else {
				lightStep = 0;
			}

			// Activate offensive blinding mechanism
			if (lightOn || stick.getRawButton(1) || stick.getRawButton(2)) {
				relay.set(Relay.Value.kForward);
			} else {
				relay.set(Relay.Value.kOff);
			}

			// Kyle never even uses the driving functions
			// That's why A is just a test button
			// and the only other thing is the normal drive keepo :>
			if (xBox.getRawButton(1)) {
				// if (bank.ultraDistanceLeft() > 16 &&
				// bank.ultraDistanceRight() > 16) {
				// if (pixyGearFunc.turnAndGoStraightAuton()) {
				// Driver.drive(0.25, 0, 1);
				// }
				// } else {
				// ultraFunctions.driveFowardAuton(8);
				// }
				// THIS CODE FUCKIN WORKS FOR THE PIXY CENTERING
				// HOLY SHIT WE DID IT BOYS WE REALLY FUCKIN DID IT
				// Testing Button
				if (step == 0) {
					driveFunc.initEncoders();
					step = 1;
				} else {
					driveFunc.driveStraightAuton(16);
				}
			} else {
				step = 0;
				Driver.drive(xBox.getRawAxis(1), xBox.getRawAxis(4) * 1, 1);
			}

			// Set shooter mode
			// Try not to shoot up the robotics competition
			if (stick.getRawButton(7)) {
				shootMode = 1;
			} else if (stick.getRawButton(9)) {
				shootMode = 2;
			} else if (stick.getRawButton(11)) {
				shootMode = 3;
			}

			// ACTIVATE HEAVEN PIERCING BALL BLASTER
			// GIGA BALL SHOOTER
			if (stick.getRawButton(1) || stick.getRawButton(2)) {
				autoTrack = true;
				if (shootMode == 1) {
					shooterFunc.centerPegShoot();
				} else if (shootMode == 2) {
					shooterFunc.boilerHopperShoot();
				} else if (shootMode == 3) {
					shooterFunc.boilerPegShoot();
				}
			} else if (stick.getRawButton(10)) {
				shooter.set(.5);
			} else {

				autoTrack = false;
				shooter.set(0);
			}

			// MULTI TRACK SHOOTING
			if (stick.getRawButton(4) || autoTrack) {
				if (shootMode == 1) {
					pixyShooterFunc.alignShooterXCenter();
				} else {
					pixyShooterFunc.alignShooterX();
				}
			} else if (Math.abs(stick.getZ()) > .15) {
				turret.set(stick.getZ() / 3.5);
			} else if (stick.getRawButton(12) || stick.getRawButton(5) || stick.getRawButton(6)
					|| xBox.getRawAxis(2) != 0) {
				shooterFunc.turretCenter();
			} else {
				turret.set(0);
			}

			//Activate ball fondlers to fill ourselves to maximum capacity
			//"But s-senpai... I can't possibly fit any more..."
			// - Bot-chan
			if (stick.getRawButton(1) || xBox.getRawAxis(3) != 0 || stick.getRawButton(3)) {
				sweeper.set(-1);
			} else {
				sweeper.set(0);
			}

			//ORAORAORAORAORAORAORAORA BALL SHOOTING
			if (stick.getRawButton(1)) {
				transport.set(-1);
			} else {
				transport.set(0);
			}

			//GOTTA KEEP ON RISING TO THE TOP, ALL MY MOTHERFUCKERS RISING TO THE TOP
			//PRESS THESE BUTTONS TO PIERCE THE HEAVENS AND SECURE VICTORY
			//AKA hang ourselves
			if (stick.getRawButton(6) || stick.getRawButton(5)) {
				climber.set(1);
			} else if (xBox.getRawAxis(2) != 0) {
				climber.set(xBox.getRawAxis(2));
			} else {
				climber.set(0);
			}
			
			//Servo still doesn't work
			//TODO 
			//get Myles to OHM the wires/triple check the ports
			if (stick.getPOV() == 180) {
				hood.increment(-.1);
			} else if (stick.getPOV() == 0) {
				hood.increment(.1);
			}
		}
		// Robots die when they break
		// - Phantom Katz 2k17
	}

	@Override
	public void test() {
		bank.pixyShooterProc.pixyTestReset();
		bank.pixyGearProc.pixyTestReset();
		while (isTest() && isEnabled()) {
			relay.set(Relay.Value.kForward);
			bank.pixyShooterProc.pixyI2CTest();
			bank.pixyShooterProc.pixyTest();
			bank.pixyGearProc.pixyI2CTest();
			bank.pixyGearProc.pixyTest();
			// SmartDashboard.putNumber("Gyro", gyro.gyro.getAngle());
		}
	}

	public void disabled() {
		bank.pixyShooterProc.pixyTestReset();
		bank.pixyGearProc.pixyTestReset();
	}

	public void TurretReset() {

	}
}
