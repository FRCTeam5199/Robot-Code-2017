package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
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

	private static Joystick modeSelector; // the Ti Launchpad thing

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
	private static Servo shooterServo;
	private static ServoDude hood;

	Compressor compressor = new Compressor();

	DoubleSolenoid solenoid;

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
		// "What do you mean we're not painting Fink silver?"
		// - The rest of the team
	}

	@Override
	public void robotInit() {
		// Camera Creation
		UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
		camera1.setResolution(640, 360);
		camera1.setFPS(15);

		// Still no guitar hero :(

		// Commented because the modeSelector takes up USB port 0 in the
		// DriverStation
		xBox = new Joystick(0);
		modeSelector = new Joystick(2);
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
		hood.set(0);

		// Using the beta capsule, Hayata becomes.... ULTRAMAN
		ultraFunctions = new UltrasonicFunctions(bank.ultraData, right, left);

		// I CAN SEE, I CAN FIGHT!
		pixyGearFunc = new PixyFunctions(bank.pixyGear, ultraFunctions, bank.driveEncoders, Driver);
		pixyShooterFunc = new PixyFunctions(bank.pixyShooter, turret, bank);

		// Relay race go
		relay = new Relay(0);

		solenoid = new DoubleSolenoid(4, 5);

		bank.timeReset();

		// Highly experimental autonomous selection
		// Maybe if this doesn't work, we can attach a cell phone to a microwave
		// autoChooser = new SendableChooser();
		// autoChooser.addDefault("Do nothing pls no pick this one fam", "jack
		// shit");
		// autoChooser.addObject("Center", "center");
		// autoChooser.addObject("Left", "left");
		// autoChooser.addObject("Right", "right");
		//
		// SmartDashboard.putData("Autonomous Selection", autoChooser); // Maybe
		// we
		// display
		// this
		// shit?

	}

	@Override
	public void autonomous() {

		autoStep = 0;
		String auto = null;
		boolean left, center, right;
		center = false;
		right = true;
		left = false;
		driveFunc.initEncoders();

		while (isAutonomous() && isEnabled()) {
			SmartDashboard.putNumber("Encoder Rate", bank.shooterRPM());
			relay.set(Relay.Value.kForward);
			solenoid.set(DoubleSolenoid.Value.kForward);

			// TODO
			// Add super smooth Ultra numbers that I found out before,
			// Somewhere on the A button

			// "Will you cancel your auton?"
			// "NO NO NO"
			// "Are you going to miss placing the gear?"
			// "NO NO NO"
			// "Are you going to do the side auton?"
			// "YES YES YES"
			// "Are you going to do the shooting thing?"
			// "YES YES YES! YES!"

			// autoMode = (int) SmartDashboard.getNumber("Auto Mode:");
			if (modeSelector.getRawButton(1)) {
				// Right Auto w/ Shooting
				if (autoStep >= 1) {
					pixyShooterFunc.alignShooterX();
				}

				shooterFunc.boilerPegShoot();

				if (autoStep == 0) {
					if (driveFunc.driveStraightAuton(45)) {
						autoStep = 1;
					}
				}
				if (autoStep == 1) {
					solenoid.set(DoubleSolenoid.Value.kOff);
					if (!(Math.abs(bank.pixyGearXPos() - 160) < 7)) {
						Driver.drive(0, -.38, 1);
					} else {
						Driver.stop();
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					if (bank.ultraDistanceLeft() > 8 && bank.ultraDistanceRight() > 8) {
						if (pixyGearFunc.turnAndGoStraightAuton()) {
							Driver.drive(-0.45, 0, 1);
						}
					} else {
						if (ultraFunctions.driveFowardAuton(6)) {
							autoStep = 3;
						}
					}
				}
				if (autoStep == 3) {
					Driver.stop();
					if (bank.shooterRPM() > 3100) {
						sweeper.set(-1);
						transport.set(-1);
					}
				}
			} else if (modeSelector.getRawButton(2)) {
				// Right Auto No shooting
				// if (autoStep >= 1) {
				// pixyShooterFunc.alignShooterX();
				// }
				//
				// shooterFunc.boilerPegShoot();
				if (autoStep == 0) {
					if (driveFunc.driveStraightAuton(82)) {
						autoStep = 1;
					}
				}
				if (autoStep == 1) {
					if (!(Math.abs(bank.pixyGearXPos() - 160) < 7)) {
						Driver.deadTurn(-.42, 1);
					} else {
						Driver.stop();
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					if (bank.ultraDistanceLeft() > 8 && bank.ultraDistanceRight() > 8) {
						if (pixyGearFunc.turnAndGoStraightAuton()) {
							Driver.drive(-0.35, 0, 1);
						}
					} else {
						if (ultraFunctions.driveFowardAuton(6)) {
							autoStep = 3;
						}
					}
				}
				if (autoStep == 3) {
					Driver.stop();
					if (bank.shooterRPM() > 3100) {
						sweeper.set(-1);
						transport.set(-1);
					}
				}
			} else if (modeSelector.getRawButton(5)) {
				// Left auto w/ shooting

				if (autoStep >= 1) {
					pixyShooterFunc.alignShooterX();
				}

				shooterFunc.boilerPegShoot();

				if (autoStep == 0) {
					if (driveFunc.driveStraightAuton(53)) {
						autoStep = 1;
					}
				}
				if (autoStep == 1) {
					if (!(Math.abs(bank.pixyGearXPos() - 160) < 7)) {
						Driver.drive(0, .38, 1);
					} else {
						Driver.stop();
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					if (bank.ultraDistanceLeft() > 8 && bank.ultraDistanceRight() > 8) {
						if (pixyGearFunc.turnAndGoStraightAuton()) {
							Driver.drive(-0.45, 0, 1);
						}
					} else {
						if (ultraFunctions.driveFowardAuton(4)) {
							autoStep = 3;
						}
					}
				}
				if (autoStep == 3) {
					Driver.stop();
					sweeper.set(-1);
					transport.set(-1);
				}
			} else if (modeSelector.getRawButton(4)) {
				// Left auto no shooting

				if (autoStep == 0) {
					if (driveFunc.driveStraightAuton(82)) {
						autoStep = 1;
					}
				}
				if (autoStep == 1) {
					if (!(Math.abs(bank.pixyGearXPos() - 160) < 7)) {
						Driver.drive(0, .4, 1);
					} else {
						Driver.stop();
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					if (bank.ultraDistanceLeft() > 8 && bank.ultraDistanceRight() > 8) {
						if (pixyGearFunc.turnAndGoStraightAuton()) {
							Driver.drive(-0.35, 0, 1);
						}
					} else {
						if (ultraFunctions.driveFowardAuton(4)) {
							autoStep = 3;
						}
					}
				}
				if (autoStep == 3) {
					Driver.stop();
				}
			} else if (modeSelector.getRawButton(3)) {
				// Center Auto
				// "Yo did he just walk up, slowly, and down smash?????"
				// - Smash brothers man

				if (autoStep >= 1) {
					pixyShooterFunc.alignShooterXCenter();
				}
				shooterFunc.centerPegShoot();

				if (autoStep == 0) {
					// if (driveFunc.driveStraightAuton(38)) {
					// autoStep = 1;
					// }
					autoStep = 1;
				}

				if (autoStep == 1) {
					if (bank.ultraDistanceLeft() > 18 && bank.ultraDistanceRight() > 18) {
						if (pixyGearFunc.turnAndGoStraightAuton()) {
							Driver.drive(-0.35, 0, 1);
						}
					} else {
						if (ultraFunctions.driveFowardAuton(10)) {
							autoStep = 2;
						}
					}
				}

				if (autoStep == 2) {
					Driver.drive(.05, 0, 1);
					Timer.delay(.5);
					transport.set(-1);
					sweeper.set(-1);
				}
			} else {
				if (autoStep == 0) {
					if (driveFunc.driveStraightAuton(49)) {
						autoStep = 1;
					}
				}
				if (autoStep == 1) {
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

		driveFunc.initEncoders();

		bank.time = System.currentTimeMillis();

		bank.pixyShooterProc.pixyTestReset();
		bank.pixyGearProc.pixyTestReset();
		
		solenoid.set(DoubleSolenoid.Value.kOff);

		while (isOperatorControl() && isEnabled()) {
			// All systems go
			// You may raise dongers when ready

			// Display info so we can accurately measure our E-Peen
			SmartDashboard.putNumber("ServoDude", hood.position);
			SmartDashboard.putNumber("Encoder Rate", bank.shooterRPM());
			SmartDashboard.putNumber("Turret Encoder", turret.getPosition());
			SmartDashboard.putNumber("Left Ultra", bank.ultraDistanceLeft());
			SmartDashboard.putNumber("Right Ultra", bank.ultraDistanceRight());
			SmartDashboard.putNumber("Shooter X AVG", bank.shooterX());
			driveFunc.displayDistance();

			bank.pixyShooterProc.pixyShooterI2CTest();

			bank.pixyShooterProc.pixyShooterTest();
			bank.pixyGearProc.pixyGearI2CTest();
			bank.pixyGearProc.pixyGearTest();

			// Toggle for light sphincters
			if (stick.getRawButton(8)) {
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
				if (bank.ultraDistanceLeft() > 16 && bank.ultraDistanceRight() > 16) {
					if (pixyGearFunc.turnAndGoStraightAuton()) {
						Driver.drive(-0.25, 0, 1);
					}
				} else {
					ultraFunctions.driveFowardAuton(8);
				}
				// THIS CODE FUCKIN WORKS FOR THE PIXY CENTERING
				// HOLY SHIT WE DID IT BOYS WE REALLY FUCKIN DID IT
				// Testing Button
//				if (step == 0) {
//					driveFunc.initEncoders();
//					step = 1;
//				} else {
//					driveFunc.driveStraightAuton(40);
//				}
			} else {
				step = 0;
				Driver.drive(xBox.getRawAxis(1), xBox.getRawAxis(4) * .6, 1);
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
			} else {

				autoTrack = false;
				shooter.set(0);
				// shooterFunc.PIDstop();
			}

			// MULTI TRACK SHOOTING
			if (autoTrack||stick.getRawButton(10)) {
				if (shootMode == 1) {
					pixyShooterFunc.alignShooterXCenter();
				} else {
					pixyShooterFunc.alignShooterXCenter();
				}
			} else if (Math.abs(stick.getZ()) > .15) {
				turret.set(stick.getZ() / 3.5);
			} else if (stick.getRawButton(12) || stick.getRawButton(5) || stick.getRawButton(6)
					|| xBox.getRawAxis(2) != 0) {
				shooterFunc.turretCenter();
			} else {
				turret.set(0);
			}

			// Activate ball fondlers to fill ourselves to maximum capacity
			// "But s-senpai... I can't possibly fit any more..."
			// - Bot-chan
			if (stick.getRawButton(1) || xBox.getRawAxis(3) != 0 || stick.getRawButton(3) || stick.getRawButton(4)) {
				sweeper.set(-1);
			} else {
				sweeper.set(0);
			}

			// ORAORAORAORAORAORAORAORA BALL SHOOTING
			if (stick.getRawButton(1)) {
				transport.set(-1);
			} else {
				transport.set(0);
			}

			// GOTTA KEEP ON RISING TO THE TOP, ALL MY MOTHERFUCKERS RISING TO
			// THE TOP
			// PRESS THESE BUTTONS TO PIERCE THE HEAVENS AND SECURE VICTORY
			// AKA hang ourselves
			if (stick.getRawButton(6) || stick.getRawButton(5)) {
				if (Math.abs(turret.getPosition()) < 2500) {
					climber.set(1);
				} else {
					climber.set(0);
				}
			} else if (xBox.getRawAxis(2) != 0) {
				// climber.set(xBox.getRawAxis(2));
			} else {
				climber.set(0);
			}

			// After 10,000 years of pain, the servo finally works
			if (stick.getPOV() == 180) {
				hood.increment(.1);
			} else if (stick.getPOV() == 0) {
				hood.increment(-.1);
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
