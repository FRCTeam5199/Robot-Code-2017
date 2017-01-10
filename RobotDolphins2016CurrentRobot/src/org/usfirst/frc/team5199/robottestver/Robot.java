package org.usfirst.frc.team5199.robottestver;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotDrive myRobot;
	RobotDrive myRobot2;
	Joystick stick;
	Solenoid mainDir,mainFlow, footDir, footFlow, hookCyl;
	DoubleSolenoid ballCyl;
	Compressor compressor;
	Joystick stick2;
	Ultrasonic leftultra, rightultra;
	Image frame;
	int YBA;
	long millis;
	long millisBall;
	long millisDrive;
	long autonomousMillis = 0;
	double leftrange, rightrange;
	double DriveSpeed;
	boolean ballCylToggle, CamToggle, endMatch;
	AxisCamera camera;
	Servo CamServo;
	Victor winch,ball1,ball2;
	Talon leftFront, leftBack, rightFront, rightBack;
	int autoLoopCounter,CamX; 
	int autonNum;
	final int frontLeftChannel	= 0;
	final int rearLeftChannel	= 1;
	final int frontRightChannel	= 2;
	final int rearRightChannel	= 3;
	final int channel4= 4;
	CameraServer server;
	boolean autoSthap;
	DigitalInput HallEffect;
	ADXRS450_Gyro gyro;
	PowerDistributionPanel pdp;
	boolean teleBegin;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		server = CameraServer.getInstance();
		server.setQuality(50);
		//server.startAutomaticCapture("cam0");
		server.startAutomaticCapture("cam2");
		camera = new AxisCamera("192.168.0.42");
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

		ballCylToggle=true;
		CamToggle = true;
		DriveSpeed = 1;
		CamServo = new Servo(6);// camera servo
		
		mainDir = new Solenoid(1,0);
		footDir = new Solenoid(1,2); 
		mainFlow = new Solenoid(1,1);
		footFlow = new Solenoid(1,3);
		ballCyl = new DoubleSolenoid(1,5,6);
		hookCyl = new Solenoid(1,4);
		compressor = new Compressor(1);
		
		stick = new Joystick(0);
		stick2 = new Joystick(1);
		
		//myRobot = new RobotDrive(frontRightChannel,rearRightChannel , rearLeftChannel, frontLeftChannel);
		//		myRobot.setInvertedMotor(MotorType.kFrontLeft, true);	// invert the left side motors
		//		myRobot.setInvertedMotor(MotorType.kRearLeft, true);		// you may need to change or remove this to match your robot
		//myRobot.setExpiration(0.1);
		
		leftFront = new Talon(0);
		leftBack = new Talon(1);
		rightFront = new Talon(2);
		rightBack = new Talon(3);
		ball1= new Victor(4);
		winch= new Victor(5);
		ball2= new Victor(7);

		//gyro = new ADXRS450_Gyro();
		
		//gyro.reset();
	
		pdp = new PowerDistributionPanel();

		millisBall  = 10;
		
		autoSthap=false;
		
		//HallEffect = new DigitalInput(4);
		/*
		leftultra = new Ultrasonic(0,1);
		leftultra.setAutomaticMode(true);
		rightultra = new Ultrasonic(2,3);
		rightultra.setAutomaticMode(true);// turns on automatic mode
		 */
	}

	/**
	 * This function is run once each time the robot enters autonomous mode
	 */
	public void autonomousInit() {
		SmartDashboard.putInt("Autonmous Routine Selection", 0);
		autoSthap=false;

	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		compressor.setClosedLoopControl(true);
		if (autoSthap==false && SmartDashboard.getInt("Autonomous Routine Selection", 0) == 0){
			ballCyl.set(DoubleSolenoid.Value.kReverse);
			Timer.delay(1.5);
			leftBack.set(1);
			rightBack.set(-1);
			leftFront.set(1);
			rightFront.set(-1);
			Timer.delay(2.25);
			leftBack.set(-.1);
			rightBack.set(.1);
			leftFront.set(-.1);
			rightFront.set(.1);
			autoSthap=true;
		}
		else if(autoSthap==false && SmartDashboard.getInt("Autonomous Routine Selection", 0) == 0){
			ObstacleFunctions.RoughTerrainAuto(leftBack, leftFront, rightBack, rightFront, ballCyl);
		}
		else{
			//kys
		}
			
		}
	


	/**
	 * This function is called once each time the robot enters teleoperated mode
	 */
	public void teleopInit(){
		SmartDashboard.putString("Front Arms Position", "Up");
		SmartDashboard.putString("Main Arm Position", "Down");
		SmartDashboard.putString("Ball Box Position", "Up");
		SmartDashboard.putDouble("Winch Current",0);
		SmartDashboard.putDouble("Gyro",0);
		hookCyl.set(false);
		endMatch = false;
		teleBegin = true;

	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		if (teleBegin == true){
			//gyro.reset();
			teleBegin = false;
		}
		
		if(endMatch == false){
		compressor.setClosedLoopControl(true);
		SmartDashboard.putString("Hook Solenoid Status", "Secured");
		
		}
		
		//SmartDashboard.putDouble("Gyro",gyro.getAngle());
		SmartDashboard.putDouble("Winch Current",pdp.getCurrent(0));
		
	//SmartDashboard.putBoolean("Hall Effect Sensor", HallEffect.get());	For later use

		if(stick.getRawButton(2) && stick.getRawAxis(3) > .5){
			ObstacleFunctions.Drawbridge(leftBack, leftFront, rightBack, rightFront, footFlow, footDir, mainFlow, mainDir);
		}
		
		if(stick.getRawButton(1) && stick.getRawAxis(3) > .5){
			ObstacleFunctions.ChivalDeFrise(leftBack, leftFront, rightBack, rightFront, footFlow, footDir);
		} 
		/*leftrange = leftultra.getRangeInches();
		rightrange = rightultra.getRangeInches();
		SmartDashboard.putDouble("Distance Left", leftrange);
		SmartDashboard.putDouble("Distance Right", rightrange);
		*/
		//Squaring function mapped to XBox A button
		//Spencer Kammerman 2/14/16
		/*if(stick.getRawButton(1)){
			if((leftrange - rightrange) > .5){
				myRobot.arcadeDrive(0, .3);
			}
			else if((leftrange - rightrange) < -.5){
				myRobot.arcadeDrive(0, -.3);
			}
		}*/
		//Camera servo control mapped to flight stick POV pad
		//Spencer Kammerman 2/14/16
		/*
		if (stick2.getPOV()==0){
			CamY++;
		}
		else if (stick2.getPOV()==180){
			CamY--;
		}
		*/
		//servo1.setAngle(CamY);
		

		
		if(stick.getRawButton(6)){
			if(CamToggle ==false){
			CamServo.setAngle(180);
			CamToggle= true;
			}
			if(CamToggle){
				CamServo.set(0);
				CamToggle = false;
			}
		}
		
		if (stick.getPOV()== 0){
			CamX = 0;
		}
		else if (stick.getPOV() == 180){
			CamX = 270;
		}
		
		CamServo.setAngle(CamX);
		SmartDashboard.putInt("Cam Degrees", CamX);

		//Main Arm Control
		//Spencer Kammerman and Kevin Jung 2/14/16
//		if(mainFlow.get()==false) { 
//			if(stick2.getRawButton(8)) {     			// TODO: Add Button name       		
//				mainDir.set(true);
//				mainFlow.set(true);
//			} else if(stick2.getRawButton(7)) {       	// TODO: Add Button name
//				mainDir.set(false);
//				mainFlow.set(true);
//				YBA = 2;
//				millis  = System.currentTimeMillis();
//			} else if(stick2.getRawButton(9)) {			// TODO: Add Button name
//				mainDir.set(false);
//				mainFlow.set(true);
//				YBA = 1;
//				millis  = System.currentTimeMillis();
//			} else if(stick2.getRawButton(11)) {		// TODO: Add Button name
//				mainDir.set(false);
//				mainFlow.set(true);
//				YBA = 0;
//				millis  = System.currentTimeMillis();
//			}
//		} else if(YBA == 0){ // Either both of the buttons or none of them are depressed.
//			if(System.currentTimeMillis()-millis > mainCylTimeY){
//				mainFlow.set(false);
//				millis  = System.currentTimeMillis();
//			}
//		} else if(YBA == 1) { // Either both of the buttons or none of them are depressed.
//			if(System.currentTimeMillis()-millis > mainCylTimeB){
//				mainFlow.set(false);
//				millis  = System.currentTimeMillis();
//			}
//		} else if(YBA == 2) { // Either both of the buttons or none of them are depressed.
//			if(System.currentTimeMillis()-millis > mainCylTimeA){
//				mainFlow.set(false);
//				millis  = System.currentTimeMillis();
//			}
//		}
		/*if(stick2.getRawButton(9)) {     			// TODO: Add Button name       		
			compressor.stop();
		} else {
			//mainDir.set(false);
			compressor.start();
		}
		*/
		

		if(stick2.getRawButton(1)) {     			// TODO: Add Button name       		
			mainDir.set(true);
			mainFlow.set(true);
			SmartDashboard.putString("Main Arm Position", "Up");
		} else if(stick2.getRawButton(2)) {       	// TODO: Add Button name
			mainDir.set(false);
			mainFlow.set(true);
			SmartDashboard.putString("Main Arm Position", "Down");

		} else {
			//mainDir.set(false);
			mainFlow.set(false);
		}
		//Smaller Arm Control
		//Spencer Kamerman and Kevin Jung 2/14/16
		if(stick2.getRawButton(8)) {     			// TODO: Add Button name       		
			footDir.set(false);
			footFlow.set(true);
			SmartDashboard.putString("Front Arms Position", "Up");

		} else if(stick2.getRawButton(9)) {       	// TODO: Add Button name
			footDir.set(true);
			footFlow.set(true);
			SmartDashboard.putString("Front Arms Position", "Down");
		} else {
			//footDir.set(false);
			footFlow.set(false);
		}
		
		if(stick.getRawButton(2) && ((System.currentTimeMillis() - millisDrive) > 150)){
			if(DriveSpeed == 1){
				DriveSpeed = .5;
				millisDrive  = System.currentTimeMillis();
			}else if(DriveSpeed == .5){
				DriveSpeed = 1;
				millisDrive  = System.currentTimeMillis();

			}
		}
		
		//Kevin Drive 
		leftBack.set((((stick.getY()*-1* DriveSpeed))+stick.getRawAxis(4)));
		rightBack.set(((stick.getY()* DriveSpeed)+stick.getRawAxis(4)));
		leftFront.set((((stick.getY()*-1)* DriveSpeed)+stick.getRawAxis(4)));
		rightFront.set(((stick.getY()* DriveSpeed)+stick.getRawAxis(4)));

	
		//Spencer Kammerman - idk what this is lmao. Maybe a variation of Kevin Drive^Tm
		//	public void arcadeDrive(double throttleV, double turnV){
		//double leftMtr;
		///double rightMtr;
		//leftMtr = stick.getX() + stick.getY();
		//rghtMtr = stick.getX() - stick.getY();
		//	}
		
		//myRobot.tankDrive(stick.getRawAxis(1), stick.getRawAxis(5));


		
		if(stick2.getRawButton(4)){ // Winch program (02-13-16) - Kyle Watkins
			winch.set(1);
		}else if(stick2.getRawButton(3)){
			winch.set(-1);
		}else{
			winch.set(0);
		}							// End Winch Program (02-13-16) - Kyle Watkins
		
		if(stick2.getRawButton(7)){ // Ball Box Program (02-13-16) - Kyle Watkins
			ball1.set(1);
			ball2.set(-1);
		}else if(stick2.getRawButton(5)){
			ball1.set(-1);
			ball2.set(1);
		}else{
			ball1.set(0);  
			ball2.set(0);
		}							// End Ball Box Program (02-13-16) - Kyle Watkins
		
		//Ball Box Cylinder Toggle Code
		if (stick2.getRawButton(6) && ((System.currentTimeMillis() - millisBall) > 300)){			
			if(ballCylToggle == true){
				ballCylToggle = false;
				SmartDashboard.putString("Ball Box Position", "Down");
				millisBall  = System.currentTimeMillis();
			}
			else if (ballCylToggle == false){
				ballCylToggle = true;
				SmartDashboard.putString("Ball Box Position", "Up");
				millisBall  = System.currentTimeMillis();
			}
			if(ballCylToggle){
				ballCyl.set(DoubleSolenoid.Value.kReverse);
			}else{
				ballCyl.set(DoubleSolenoid.Value.kForward);
			}
			
		}
		
		
		if(stick.getRawButton(5) && stick2.getRawButton(6)){
			hookCyl.set(true); 
			compressor.setClosedLoopControl(false);
			endMatch = true;
			SmartDashboard.putString("Hook Solenoid Status", "RELEASED");
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}
}