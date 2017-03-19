package org.usfirst.frc.team5199.robot;

import org.opencv.core.Mat;

import com.ctre.CANTalon;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
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
	
	// Basic initialization
	private Joystick xBox, stick;
	private PowerDistributionPanel pdp;
	private Compressor compressor;
	private DoubleSolenoid solenoid;
	
	// Motor initialization
	private Spark leftMotor, rightMotor, climber, transport, sweeper;
	private CANTalon turret, shooter;
	private RobotDrive robotDriver;
	
	// Pixy and LED Relay init
	private Pixy pixyGear, pixyShooter;
	private Relay relay;
	private PixyProcess pixyGearProc, pixyShooterProc;
	private PixyFunctions pixyGearFunc, pixyShooterFunc;

	// Ultra init
	private UltrasonicData ultraData;
	private UltrasonicFunctions ultraFunctions;

	// Encoder init
	private EncoderDriveFunctions driveEncoders;
	private EncoderShooterFunctions shooterEncoders;
	private Encoder shooterEncoder;
	
	// Gyro init
	private GyroFunctions gyro;

	// Servo init
	private Servo servo, servo2;
	private ServoDude shooterServo, servoBoy;
	
	//data init
	DataBank data;

	// -----------------------------------------------------------
	// Number/Constant init zone
	
	private static int step;
	private static int shooterMode;
	private static int lightStep;
	private static double AVG;
	public static double shootPow;
	private static double startTime;
	private double currAngle;
	private String autoMode;
	private int autoStep;
	//TODO keep those two lines only and initilalize everything in robot init
	private static double straightMod = RobotMap.straightMod, turnMod = RobotMap.turnMod, driveMod = RobotMap.driveMod;

	private static double position = RobotMap.positionInitial, increment =RobotMap.incrementInitial;

	// Number/Constant init zone
	// ------------------------------------------------------------

	// ************************************************************
	// Boolean Zone
	public static boolean spinUp;
	public static boolean lightOn;
	public static boolean left;
	// Boolean Zone
	// *************************************************************

	public Robot() {
	}

	@Override
	public void robotInit() {

		pdp = new PowerDistributionPanel();
		
		//motor initialization
		rightMotor = new Spark(RobotMap.rightMotor);
		leftMotor = new Spark(RobotMap.leftMotor);
		shooter = new CANTalon(RobotMap.shooter);
		sweeper = new Spark(RobotMap.intake);
		transport = new Spark(RobotMap.transport);
		climber = new Spark(RobotMap.climber);
		turret = new CANTalon(RobotMap.turret);
		servo = new Servo(RobotMap.shooterServo);
		servo2 = new Servo(RobotMap.shooterServo2);
		
		//servos
		shooterServo = new ServoDude(servo);
		servoBoy = new ServoDude(servo2);
		
		//drive initialization
		robotDriver = new RobotDrive(rightMotor, leftMotor);
		stick = new Joystick(RobotMap.joyStickPort);
		xBox = new Joystick(RobotMap.xBoxPort);
		
		//encoders initialization
		driveEncoders = new EncoderDriveFunctions(rightMotor, leftMotor, shooter);
		EncoderDriveFunctions.resetDrive();
		EncoderDriveFunctions.initEncoders();
		
		data = new DataBank();
		
		//ultrasonic stuff
		ultraData = new UltrasonicData(RobotMap.ultraRightEcho, RobotMap.ultraRightPing, RobotMap.ultraLeftEcho,
				RobotMap.ultraLeftPing);
		ultraFunctions = new UltrasonicFunctions(ultraData, rightMotor, leftMotor);
		
		//gyro initialization
		gyro = new GyroFunctions(rightMotor, leftMotor);
		GyroFunctions.gyro.calibrate();
		
		//pixy initalization
		pixyGear = new Pixy(RobotMap.pixyGear);
		pixyGearProc = new PixyProcess(pixyGear);
		pixyShooter = new Pixy(RobotMap.pixyShoot);
		pixyShooterProc = new PixyProcess(pixyShooter);
		pixyGearFunc = new PixyFunctions(pixyGear, ultraFunctions, driveEncoders, robotDriver);
		pixyShooterFunc = new PixyFunctions(pixyShooter, turret);
		
		 //relay initialization
		relay = new Relay(RobotMap.relayPort);
		
		//solenoid initialization
		solenoid = new DoubleSolenoid(RobotMap.solenoidLeft, RobotMap.solenoidRight);
	
		//number initialization
		step = RobotMap.stepInitial;
		shootPow = RobotMap.shootPowInitial;
		startTime = RobotMap.startTimeInitial;
		shooterMode = RobotMap.shooterModeInitial;
		 lightStep = RobotMap.lightStepInitial;
		
		
		//camera init
		CameraServer.getInstance().startAutomaticCapture(RobotMap.usbCamera1);
		
		//getting stuff ready for auton
		SmartDashboard.putString("Autonomous Mode", "Enter Value");
		autoStep = RobotMap.autoStepInitial;
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
		autoMode = SmartDashboard.getString("Autonomous Mode");
		if (autoMode.equals("Enter Value")) {
			autoMode = "S";
		}
		relay.set(Relay.Value.kForward);
		spinUp = RobotMap.spinUpInitial;
		AVG = RobotMap.AVGInitial;
		autoStep = RobotMap.autoStepInitial;
		startTime = System.currentTimeMillis();
		EncoderDriveFunctions.initEncoders();

		while (isAutonomous() && isEnabled()) {
			SmartDashboard.putNumber("Auto Step", autoStep);
			if (autoMode.equals("e") || autoMode.equals("E")) {
				AVG = data.shooterRPM();
				if (spinUp) {
					if (AVG < 1000) {
						shooter.set(1);
					} else if (AVG < 2000) {
						shooter.set(.85);
					} else if (AVG < 2500) {
						shooter.set(.7);
					} else if (AVG < 2700) {
						shooter.set(.57);
						//TODO change this cannot be looking at joystick during auton need to figure out the correct value
					} else if (AVG < 3455 - (50 * stick.getRawAxis(3))) {
						shootPow += 0.0003;
						shooter.set(shootPow + .57);
					} else {
						shooter.set(.57);
						shootPow -= 0.0001;
					}
				}
				if (autoStep == 0) {
					solenoid.set(DoubleSolenoid.Value.kForward);
					EncoderDriveFunctions.initEncoders();
					startTime = System.currentTimeMillis();
					autoStep = 1;
				}
				if (autoStep == 1) {
					if (System.currentTimeMillis() - startTime > 1000) {
						solenoid.set(DoubleSolenoid.Value.kOff);
					}
					if (EncoderDriveFunctions.driveStraightAuton(50)) {
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					currAngle = GyroFunctions.getAngle();
					EncoderDriveFunctions.initEncoders();
					robotDriver.stop();
					autoStep = 3;
					Timer.delay(.5);
				}
				if (autoStep == 3) {
					//this only works for right turns, need to do right or left depeneding on which alliance we are on
					if (EncoderDriveFunctions.autonSixtyDegreeTurn(currAngle, false)) {
						robotDriver.stop();
						EncoderDriveFunctions.initEncoders();
						Timer.delay(1);
						autoStep = 4;
					}
				}
				if (autoStep == 4) {
					if (EncoderDriveFunctions.driveStraightAuton(40)) {
						autoStep = 5;
					}
				}
				if (autoStep == 5) {
					//TODO change this to whatever we find experimentally works
					if((pixyGearFunc.turnAndGoStraightAuton() && UltrasonicData.distanceLeft() < 8)){
					autoStep = 6;
					}
				}
				if (autoStep == 6) {
					if (UltrasonicFunctions.driveFowardAutonSDR(4)) {
						autoStep = 7;
					}
				}
				if (autoStep == 7) {
					robotDriver.stop();
				}
				EncoderDriveFunctions.displayDistance();
			} else if (autoMode.equals("C") || autoMode.equals("c")) {

				AVG = data.shooterRPM();
				SmartDashboard.putNumber("AVG Auton RPM", AVG);
				SmartDashboard.putBoolean("Spin up", spinUp);
				SmartDashboard.putNumber("Auto Step", autoStep);
				if (spinUp) {
					// For the center gear position
					if (AVG < 2000) {
						shooter.set(1);
					} else if (AVG < 2500) {
						shooter.set(.9);
					} else if (AVG < 3000) {
						shooter.set(.8);
					} else if (AVG < 3300) {
						shooter.set(.6);
					} else if (AVG < 3675) {
						shootPow += 0.0004;
						shooter.set(shootPow + .6);
					} else {
						shooter.set(.6);
						shootPow -= 0.0001;
					}
				}
				if (autoStep == 0) {
					solenoid.set(DoubleSolenoid.Value.kForward);
					EncoderDriveFunctions.initEncoders();
					autoStep = 1;
				}

				if (autoStep == 1) {
					spinUp = true;
					if (System.currentTimeMillis() - startTime > 1000) {
						solenoid.set(DoubleSolenoid.Value.kOff);
					}
					// if (driveEncoders.driveStraightAuton(38)) {
					if (EncoderDriveFunctions.driveStraightAuton(50)) {
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					// if (pixyFunctionsGear.turnAndGoStraightAuton() &&
					// ultraData.distanceLeft() < 8) {
					if (UltrasonicFunctions.driveFowardAutonSDR(6)) {
						autoStep = 3;
					}
					// }
				}
				if (autoStep == 3) {
					robotDriver.stop();
				}
				if (autoStep == 3 || System.currentTimeMillis() - startTime > 7000) {
					if (PixyProcess.shooterData()[0] != -1) {
						if (PixyFunctions.alignShooterX()) {
							transport.set(-1);
							sweeper.set(-1);
						}
					} else {
						transport.set(-1);
						sweeper.set(-1);
					}
				} else {
					transport.set(0);
					sweeper.set(0);
				}
			} else if (autoMode.equals("S") || autoMode.equals("s")) {
				AVG = data.shooterRPM();
				if (spinUp) {
					if (AVG < 1000) {
						shooter.set(1);
					} else if (AVG < 2000) {
						shooter.set(.85);
					} else if (AVG < 2500) {
						shooter.set(.7);
					} else if (AVG < 2700) {
						shooter.set(.57);
					} else if (AVG < 3455 - (50 * stick.getRawAxis(3))) {
						shootPow += 0.0003;
						shooter.set(shootPow + .57);
					} else {
						shooter.set(.57);
						shootPow -= 0.0001;
					}
				}

				if (autoStep == 0) {
					solenoid.set(DoubleSolenoid.Value.kForward);
					EncoderDriveFunctions.initEncoders();
					autoStep = 1;
				}

				if (autoStep == 1) {
					spinUp = true;
					if (System.currentTimeMillis() - startTime > 1000) {
						solenoid.set(DoubleSolenoid.Value.kOff);
					}
					// if (driveEncoders.driveStraightAuton(38)) {
					if (EncoderDriveFunctions.driveStraightAuton(90)) {
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
				robotDriver.stop();
				}
				if (autoStep == 2 || System.currentTimeMillis() - startTime > 7000) {
					if (PixyProcess.shooterData()[0] != -1) {
						if (PixyFunctions.alignShooterX()) {
							transport.set(-1);
							sweeper.set(-1);
						}
					} else {
						transport.set(-1);
						sweeper.set(-1);
					}
				} else {
					transport.set(0);
					sweeper.set(0);
				}
			} 
		}
	}

	/**
	 * Runs the motors with arcade steering.
	 */
	@Override
	public void operatorControl() {
		// encoder.initEncoders();
		PixyProcess.pixyTestReset();
		PixyProcess.pixyTestReset();
		GyroFunctions.resetGyro();
		EncoderDriveFunctions.initEncoders();
		lightOn = RobotMap.lightOnInitial;
		while (isOperatorControl() && isEnabled()) {
			
			//gets the speed of the flywheel
			AVG = data.shooterRPM();
			
			//changes the status of the light
			if (stick.getRawButton(8) || xBox.getRawButton(10)) {
				if (lightStep == 0) {
					lightOn = !lightOn;
					lightStep = 1;
				}
			} else {
				lightStep = 0;
			}
			
			//actually turns the light on or off
			if (lightOn) {
				relay.set(Relay.Value.kForward);
			} else {
				relay.set(Relay.Value.kOff);
			}
			SmartDashboard.putBoolean("Light On", lightOn);

			//changes what speed the driver would like to shoot
			if (stick.getRawButton(7)) {
				shooterMode = 1;
			} else if (stick.getRawButton(9)) {
				shooterMode = 2;
			} else if (stick.getRawButton(11)) {
				shooterMode = 3;
			}
			SmartDashboard.putNumber("Shooter Mode", shooterMode);

			// Flywheel activated when thumb button is pressed
			// if (stick.getRawButton(2) || stick.getRawButton(1)) {
			// if (shooterMode == 2) {
			if (stick.getRawButton(9)) {
				// For the close gear peg position
				if (AVG < 1000) {
					shooter.set(1);
				} else if (AVG < 2000) {
					shooter.set(.85);
				} else if (AVG < 2500) {
					shooter.set(.7);
				} else if (AVG < 2700) {
					shooter.set(.57);
				} else if (AVG < 3455 - (50 * stick.getRawAxis(3))) {
					shootPow += 0.0003;
					shooter.set(shootPow + .57);
				} else {
					shooter.set(.57);
					shootPow -= 0.0001;
				}
				// } else if (shooterMode == 1) {
			} else if (stick.getRawButton(7)) {
				// For the boiler hopper position
				if (AVG < 1000) {
					shooter.set(1);
				} else if (AVG < 1500) {
					shooter.set(.85);
				} else if (AVG < 2000) {
					shooter.set(.7);
				} else if (AVG < 2500) {
					shooter.set(.5);
				} else if (AVG < 3155 - (50 * stick.getRawAxis(3))) {
					shootPow += 0.0003;
					shooter.set(shootPow + .5);
				} else {
					shooter.set(.5);
					shootPow -= 0.0001;
				}
				// } else if (shooterMode == 3) {
			} else if (stick.getRawButton(11)) {
				// For the center gear position
				if (AVG < 2000) {
					shooter.set(1);
				} else if (AVG < 2500) {
					shooter.set(.9);
				} else if (AVG < 3000) {
					shooter.set(.8);
				} else if (AVG < 3300) {
					shooter.set(.6);
				} else if (AVG < 3675 - (50 * stick.getRawAxis(3))) {
					shootPow += 0.0004;
					shooter.set(shootPow + .6);
				} else {
					shooter.set(.6);
					shootPow -= 0.0001;
				}
			} else {
				shooter.set(0);
				shootPow = 0;
			}

			SmartDashboard.putNumber("Encoder Rate", AVG);
			SmartDashboard.putNumber("Motor", shooter.get());
			// Activate shooter for set positions with buttons 7,9,11

			// Stick button to enable the sweeper.
			// Also triggers with XBox right trigger
			if (stick.getRawButton(1) || xBox.getRawAxis(3) != 0 || stick.getRawButton(3)) {
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

			// Reads "twist" of the joystick to set the Azimuth of shooter
			// if (stick.getRawButton(2) || stick.getRawButton(1)) {
			// pixyFunctionsShooter.alignShooterX();
			// } else
			// if (Math.abs(stick.getRawAxis(1)) > .15) {
			// turret.set(stick.getRawAxis(1) / 6);
			// }
			// else {
			// turret.set(0);
			// }
			if (stick.getRawButton(4)) {
				PixyFunctions.alignShooterX();
			} else {
				turret.set(stick.getRawAxis(2) / 6);
			}
			SmartDashboard.putNumber("Stick Twist", stick.getRawAxis(2) / 6);
			// TODO: We've been having problems where we can only move one servo
			// at a time
			// All servo inputs maniplate just one servo
			// Need to get to the bottom of that and find out how we can move
			// two servos separately

			// Manipulates the servo up or down
			if (stick.getPOV(0) == 0) {
				shooterServo.increment(-1);
			} else if (stick.getPOV(0) == 180) {
				shooterServo.increment(1);
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

				if (PixyFunctions.turnAndGoStraightAuton() && UltrasonicData.distanceLeft() < 8) {

					UltrasonicFunctions.driveFowardAutonSDR(4);
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
						currAngle = GyroFunctions.getAngle();
						EncoderDriveFunctions.initEncoders();
						step = 1;
					}
					if (step == 1) {
						if (xBox.getRawAxis(RobotMap.loaderTurnAxis) > .25) {
							left = true;
						} else if (xBox.getRawAxis(RobotMap.loaderTurnAxis) < -.25) {
							left = false;
						}
						// if (driveEncoders.loaderTurn(currAngle, left)){
						if (EncoderDriveFunctions.autonSixtyDegreeTurn(currAngle, !left)) {
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
				SmartDashboard.putString("Encoder Test", "unused");
				GyroFunctions.resetGyro();
				step = 0;
				RobotDrive.drive(xBox.getRawAxis(1) * straightMod, xBox.getRawAxis(4) * turnMod, driveMod);
			}

			// Watkins conditioning apparatus
			if (stick.getRawButton(12) && stick.getRawButton(10)) {
				xBox.setRumble(RumbleType.kRightRumble, 1);
				xBox.setRumble(RumbleType.kLeftRumble, 1);
			} else {
				xBox.setRumble(RumbleType.kRightRumble, 0);
				xBox.setRumble(RumbleType.kLeftRumble, 0);
			}

			// Activates the climber, giving the Operator stick priority
			if (stick.getRawButton(6) || stick.getRawButton(5)) {
				climber.set(1);
			} else if (xBox.getRawAxis(2) != 0) {
				climber.set(xBox.getRawAxis(2));
			} else {
				climber.set(0);
			}
			// if(stick.getRawButton(6)|| stick.getRawButton(5)){
			// if(climbTime == 0){
			// climbTime = System.currentTimeMillis();
			// }
			// if(System.currentTimeMillis() - climbTime > )
			// }
			// else{
			// climbTime = 0;
			// }
			SmartDashboard.putNumber("Current5", pdp.getCurrent(5));

			if (xBox.getRawButton(8)) {
				solenoid.set(DoubleSolenoid.Value.kForward);
			} else {
				solenoid.set(DoubleSolenoid.Value.kOff);
			}
		}

	}

	/**
	 * Runs during test mode
	 */
	@Override
	public void test() {

		relay.set(Relay.Value.kForward);

	}

}
