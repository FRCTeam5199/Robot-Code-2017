package org.usfirst.frc.team5199.robot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Talon;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.*;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive class.
 */
@SuppressWarnings("unused") // Suppress warning for unused imports
public class Robot extends SampleRobot {
	NewPixy pixyCam = new NewPixy(); // Pixy.java is Kevin's, NewPixy.java is Kyle's.
	PowerDistributionPanel pdp;
	Talon talon;
	DoubleSolenoid piston0;
	DoubleSolenoid piston1;
	Solenoid cylinder1,cylinder2,cylinder4,cylinder5;
	Ultrasonic leftultra = new Ultrasonic(0,1);
	Ultrasonic rightultra = new Ultrasonic(2,3);
	double leftrange, rightrange;
	DigitalInput HallEffectTest;
	// TODO New Code
	Talon ballMotor, testarino;
	Solenoid cylinder3;
	ADXRS450_Gyro gyro;
	long millis, millis2, timePassed, millis3; 
	RobotDrive robotDrive;
	Joystick stick, stick2, stickRacingWheel;
	boolean value, pos1 = true,pos2,pos3,pos4,pos5,pos6;
	int auto_counter, framenum = 0;
	//AutoFunctions auto = new AutoFunctions();
	static SmartDashboard board;
	Compressor compressor;
	Talon leftMotors, rightMotors, frontRight, frontLeft, backRight, backLeft, Right, Left;
	Victor port4;
	Servo servo1,servo2;
	BufferedImage blue = null;
	BufferedImage red = null;
	// Channels for the wheels
	final int frontLeftChannel	= 0;
	final int rearLeftChannel	= 0;
	final int frontRightChannel	= 1;
	final int rearRightChannel	= 1;
	double DriveSpeed = -.5;
	double MainCylTime;
	boolean mainCylDir;
	boolean mainCylOn;
	boolean squaring;
	boolean firstRunThroughTeleop = true;
	int YBA, YBA2;
	double sliderJoy;
	int camUp = 90, CamX = 90;
	boolean alternateAlert = true;
	boolean fast180 = true;
	int testNum;
	int frameCnt = 4;
	
	double calRot = 15;
	
	String codePos = "";

	int integers =0;
	
	// The channel on the driver station that the joystick is connected to
	final int joystickChannel	= 1;

	double time1;
	CameraServer server;
	
	
	public Robot() {
		
		
		
		firstRunThroughTeleop = true;


		int camUp = 90, CamX = 90;
		

		//camera = new AxisCamera("169.254.2.6");
		//frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

		servo1 = new Servo(4);	//top camera servo
		servo2 = new Servo(5);	//bottom camera servo
		auto_counter = 0;

		board = new SmartDashboard();
		//talon = new Talon(1);
		//port4 = new Victor(4);
		//Right = new Talon(1);
		//Left = new Talon(0);
		

		//piston0= new DoubleSolenoid(4,0,1);
		//piston1= new DoubleSolenoid(4,2,3);
		cylinder1 = new Solenoid(4,0);
		cylinder2 = new Solenoid(4,1);
		cylinder3 = new Solenoid(4,2);
		cylinder4 = new Solenoid(4,3);
		
		// WE NEED DA DOUBLE SOLENOID TO MAKE DA DANK MEMEZZZZZZZZ
		// TODO New Code
		//cylinder3 = new DoubleSolenoid(4, 0, 1);
		cylinder4 = new Solenoid(4,4);
		cylinder5 = new Solenoid(4,5);


		//piston0.set(DoubleSolenoid.Value.kOff); 
		//piston1.set(DoubleSolenoid.Value.kOff); 
		//cylinder1.set(false);
		//cylinder2.set(false);
		//cylinder3.set(false);
		//cylinder4.set(false);


		//compressor = new Compressor(4);
		//Temp Robot Drive in lieu of Kevin Drive - Spencer Kammerman 2/14/16
		//robotDrive = new RobotDrive(0,1);
		//robotDrive.setExpiration(0.1);

		//robotDrive.setInvertedMotor(MotorType.kFrontLeft, true);	// invert the left side motors
		//robotDrive.setInvertedMotor(MotorType.kRearLeft, true);		// you may need to change or remove this to match your robot
		//robotDrive.setExpiration(0.1);
		leftMotors = new Talon(0);
		rightMotors = new Talon(1);

		stick = new Joystick(0);
		stick2 = new Joystick(1);
		//stickRacingWheel = new Joystick(1);

		leftultra.setAutomaticMode(true);

		rightultra.setAutomaticMode(true);// turns on automatic mode
		
		//HallEffectTest = new DigitalInput(4); //KONO DIO DA. this is the hall sensor digital input for the cylinder
		
		gyro = new ADXRS450_Gyro();
		
		//gyro.reset();
	
		pdp = new PowerDistributionPanel();
		
		testarino = new Talon(1);
//		testarino.changeControlMode(TalonControlMode.Speed);
//		testarino.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
//		testarino.setF(0.04833);
//		testarino.setP(0.001);
//		testarino.setI(0);
//		testarino.setD(0);
//		testarino.configMaxOutputVoltage(12);
//		testarino.configNominalOutputVoltage(0.0f, 0.0f);
//		testarino.configPeakOutputVoltage(12.0f, -12.0f);
//		testNum = 100000;
		
		//SmartDashboard.putString("MainCylTimeY", "500");
		//SmartDashboard.putString("MainCylTimeB", "1000");
		//SmartDashboard.putString("MainCylTimeA", "3000");
		//SmartDashboard.putString("Code Pos", codePos);
		SmartDashboard.putString("Calibrate Rotation", String.valueOf(calRot));
		//Gets time at the beginning of match
	}


	/**
	 * Runs the motors with Mecanum drive.
	 */

	@SuppressWarnings("deprecation")
	public void operatorControl() {
		
		SmartDashboard.putString("End of Game", "");
		//robotDrive.setSafetyEnabled(true);
		int cameraY = 90, cameraX = 90;
		//NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);
		//double MainCylTimeY = Double.parseDouble(SmartDashboard.getString("MainCylTimeY"));
		//double MainCylTimeB = Double.parseDouble(SmartDashboard.getString("MainCylTimeB"));
		//double MainCylTimeA = Double.parseDouble(SmartDashboard.getString("MainCylTimeA"));
		//gyro.calibrate();
		calRot = Double.parseDouble(SmartDashboard.getString("Calibrate Rotation"));

		while (isOperatorControl() && isEnabled()) {
			
		
			//Block currentData = pixyCam.getDataBlock();
			if (!pixyCam.isFrameUpdate()){
				frameCnt++;
			}
			SmartDashboard.putNumber("Pixy?", frameCnt);
			//if(Block.isValidPacket(currentData)) {
				// Only update the Smart Dashboard if a valid packet is supplied.
			//SmartDashboard.putString("Pixy Cam Data", pixyCam.printBlockData(currentData));
				    
			

				//SmartDashboard.putBoolean("isFrameUpdate", pixyCam.isFrameUpdate());
			//}
		
			
			if(firstRunThroughTeleop)
				millis2= System.currentTimeMillis();
				SmartDashboard.putString("End of Game", "");
				firstRunThroughTeleop = false;
			}	
			
			testarino.set(0.8);
			
//			if(pdp.getCurrent(13) < 55 ){
//			if(stick2.getRawButton(5)){
//				testarino.set(1);
//			}
//			else if(stick2.getRawButton(7)){
//				testarino.set(-1);
//			}
//			else{
//				testarino.set(0);
//			}
//			System.out.println(testNum + " Speed: " + testarino.getSpeed() + " Voltage: " + testarino.getOutputVoltage());
//			}
			
			
//			SmartDashboard.putBoolean("Hall Effect Sensor", HallEffectTest.get());

			//compressor.setClosedLoopControl(true);

			leftrange = leftultra.getRangeInches();
			rightrange = rightultra.getRangeInches();
			timePassed = System.currentTimeMillis()-millis2;
			//SendableChooser chooser = new SendableChooser();
		    //chooser.addDefault("Command 1", 1);
		    //chooser.addObject("Command 2", 2);
			if(timePassed/1000>119&&timePassed/1000<129){
				
			
				if(alternateAlert == true){
					SmartDashboard.putString("End of Game", "#############################################################");
				}
				if(System.currentTimeMillis()-millis3>250&&alternateAlert==false){
					millis3= System.currentTimeMillis();
					alternateAlert= true;
				}else if(System.currentTimeMillis()-millis3>250&&alternateAlert==true){
					millis3= System.currentTimeMillis();
					alternateAlert = false;
				}
				else if(alternateAlert == false){
					SmartDashboard.putString("End of Game", "");
					
			}
			}else if(timePassed/1000>129){
				SmartDashboard.putString("End of Game", "SCALE!!!");
			}
			
			
			SmartDashboard.putNumber("Distance Left", leftrange);
			SmartDashboard.putNumber("Distance Right", rightrange);
			SmartDashboard.putNumber("Motor 0 Current",pdp.getCurrent(0));
			SmartDashboard.putNumber("Motor 13 Current",pdp.getCurrent(13));
			//SmartDashboard.putNumber("Motor 13 RPM",testarino.getEncVelocity());
			
			if(stick2.getRawButton(5)){
				//ballMotor.set(1);
			} else if(stick2.getRawButton(7)){
				//ballMotor.set(-1);
			}else {
			//ballMotor.set(0);
			}
			
			
			
			
			//gyro.reset();
			
			fast180 = true;
			
			//Gyro Upside Down
			while(stick.getRawButton(4) == true){	

				if (gyro.getAngle() > -180 + calRot && fast180==true){
					leftMotors.set(.4);
					rightMotors.set(.4);
					SmartDashboard.putDouble("Gyro",gyro.getAngle());
				}else if (gyro.getAngle() < -180 + calRot){
					fast180=false;
					leftMotors.set(-.15);
					rightMotors.set(-.15);
					SmartDashboard.putDouble("Gyro",gyro.getAngle());
				}else if (gyro.getAngle() > -180 + calRot && fast180==false){
					leftMotors.set(.15);
					rightMotors.set(.15);
					SmartDashboard.putDouble("Gyro",gyro.getAngle());
				}
			}
			
			
			// while(stick2.getRawButton(1)){
			//	if(550 > (leftrange - rightrange) && (leftrange - rightrange) > 1){
			//		leftMotors.set(-.3);
			//		rightMotors.set(-.3);
			//	}
			//	else if((rightrange - leftrange) > 1){
			//		leftMotors.set(.3);
			//		rightMotors.set(.3);

			//	}
//			if(stick2.getRawButton(2)){
//				if((leftrange - rightrange) > 500){
//					leftMotors.set(-0.3);
//					rightMotors.set(-0.3);
//					codePos = "R500";
//				}
//				else if((leftrange - rightrange) > 5){
//					leftMotors.set(-0.2);
//					rightMotors.set(-0.2);
//					codePos = "R20";
//				}
//				else if ((leftrange - rightrange) > 1){
//					leftMotors.set(-0.15);
//					rightMotors.set(-0.15);
//					codePos = "R1";
//				} 
//				else if((rightrange - leftrange) > 500){
//					leftMotors.set(.3);
//					rightMotors.set(.3);
//					codePos = "L500";
//				}
//				else if((rightrange - leftrange) > 5){
//					leftMotors.set(.2);
//					rightMotors.set(.2);
//					codePos = "L20";
//				}
//				else if ((rightrange - leftrange) > 1){
//					leftMotors.set(.15);
//					rightMotors.set(.15);
//					codePos = "L1";
//					}
//				else {
//					leftMotors.stopMotor();
//					rightMotors.stopMotor();
//					codePos = "00";
//				}
//				leftMotors.stopMotor();
//				rightMotors.stopMotor();
//			}
//			
//			if(stick2.getRawButton(1)){
//				leftMotors.stopMotor();
//				rightMotors.stopMotor();
//				codePos = "E-Stop";
//			}
				/*camera.getImage(frame);
            NIVision.imaqDrawShapeOnImage(frame, frame, rect,
                    DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);

            CameraServer.getInstance().setImage(frame);

            if(stick2.getRawButton(7)){
            	pos1 = true;
            	pos2=false;
            	pos3 =false;
            	pos4=false;
            	pos5=false;
            	pos6=false;
            }
            else if(stick2.getRawButton(8)){
            	pos1=false;
            	pos2=true;
            	pos3=false;
            	pos4=false;
            	pos5=false;
            	pos6=false;
            }
            else if(stick2.getRawButton(9)){
            	pos1=false;
            	pos2=false;
            	pos3=true;
            	pos4=false;
            	pos5=false;
            	pos6=false;
            }
            else if(stick2.getRawButton(10)){
            	pos1=false;
            	pos2=false;
            	pos3=false;
            	pos4=true;
            	pos5=false;
            	pos6=false;
            }
            else if(stick2.getRawButton(11)){
            	pos1=false;
            	pos2=false;
            	pos3=false;
            	pos4=false;
            	pos5=true;
            	pos6=false;
            }
            else if(stick2.getRawButton(12)){
            	pos1=false;
            	pos2=false;
            	pos3=false;
            	pos4=false;
            	pos5=false;
            	pos6=true;
            }
            if(pos1){
            	Servo1.setAngle(80);
                Servo2.setAngle(180);
            }
            else if(pos2){
            	Servo1.setAngle(80);
                Servo2.setAngle(0);
            }
            else if(pos3){
            	Servo1.setAngle(135);
                Servo2.setAngle(180);
            }
            else if(pos4){
            	Servo1.setAngle(135);
                Servo2.setAngle(0);
            }
            else if(pos5){
            	Servo1.setAngle(0);
            	Servo2.setAngle(180);
            }
            else if(pos6){
            	Servo1.setAngle(0);
            	Servo2.setAngle(0);
            }
				 */



				/*
        	talon.set(stick2.getY()*.5);

			while(!switch1.get() && stick2.getY()>0){
        		talon.set(0);		
        	}
        	while(!switch2.get() && stick2.getY()<0){
        		talon.set(0);		
				 */
				// Use the joystick X axis for lateral movement, Y axis for forward movement, and Z axis for rotation.
				// This sample does not use field-oriented drive, so the gyro input is set to zero.
				//while (stick.getRawButton(6) == true){   // RB
					//DriveSpeed = .2;
					//robotDrive.mecanumDrive_Cartesian(stick.getY()*DriveSpeed,
					//		0, stick.getY()*DriveSpeed, 0);
				//}
				//while (stick.getRawButton(5) == true){
					//DriveSpeed = .4;
					//robotDrive.mecanumDrive_Cartesian(stick.getX()*DriveSpeed,
					//		stick.getRawAxis(4)*DriveSpeed, stick.getX()*DriveSpeed, 0);
				//}
				//DriveSpeed = .01;

				//leftMotors.set(stick2.getY()-stick2.getX());
				//rightMotors.set(stick2.getY()*-1+stick2.getX());

				//robotDrive.mecanumDrive_Cartesian(stick.getY()*DriveSpeed,
				//	stick.getY()*DriveSpeed, 0,0);

				//robotDrive.mecanumDrive_Cartesian(stick.getX()*DriveSpeed,
				//stick.getX()*DriveSpeed, 0,0);

				if (stick2.getPOV()==0){
					camUp++;
				}
				else if (stick2.getPOV()==180){
					camUp--;
				}
				

				servo1.setAngle(camUp);
				
				if (stick2.getPOV()==90){
					CamX++;
				}
				else if (stick2.getPOV()==270){
					CamX--;
				}
				servo2.setAngle(CamX);
				
				//360 Spin
				
				
				
				//Kevin Drive Function
				
				
//				leftMotors.set((((stick.getY()*-1))+stick.getRawAxis(4)*.5));
//				rightMotors.set(((stick.getY())*1+stick.getRawAxis(4)*.5));
				
//	if (stick2.getRawButton(8)==true){
//				leftMotors.set(.5);
//				rightMotors.set(-.5);
//				}else if(stick2.getRawButton(9)){
//					leftMotors.set(-.5);
//					rightMotors.set(.5);
//				}else if(stick2.getRawButton(6)){
//					leftMotors.set(1);
//					rightMotors.set(-1);
//				}
//				else{
//					leftMotors.set(0);
//					rightMotors.set(0);
//				}
//		if(stick2.getRawButton(5)){
//			leftMotors.set(.5);
//			rightMotors.set(.5);
//		}else if(stick2.getRawButton(7)){
//			leftMotors.set(-.5);
//			rightMotors.set(-.5);
//		}
				
				//Temporary Testing Arcade Drive - Spencer Kammerman 2/14/16
				




				// New Racing Wheel Code

				//			leftMotors.set(stickRacingWheel.getRawAxis(3)-(stickRacingWheel.getRawAxis(0)*-.5)+(-1*stickRacingWheel.getRawAxis(2)));
				//			rightMotors.set(-1*stickRacingWheel.getRawAxis(3)-(stickRacingWheel.getRawAxis(0)*-.5)+(stickRacingWheel.getRawAxis(2)));
				//			
				Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles
				





				//while(stick.getRawButton(2)){
				//robotDrive.mecanumDrive_Cartesian(3, 0, 0, 0);
				//}

				/**
				 * DANK PROPRIETARY CODEZ DO NOT DISTRIBUTE XDDDDDDDDDDD
				 * COPYRIGHT 6420 ROBOT DOLPHINS BLAZING IN OUTER SPACE
				 */
				//		SmartDashboard.putDouble("Y", stick2.getY());
				//		SmartDashboard.putDouble("X", stick2.getX());
				//	SmartDashboard.putDouble("SliderJoystick", sliderJoy);
				//	SmartDashboard.putDouble("POV", stick2.getPOV());

				if(cylinder2.get()==false) { // If one of the buttons are pressed but not both.
					if(stick.getRawButton(3)) {            		
						cylinder1.set(true);
						cylinder2.set(true);
					} else if(stick.getRawButton(1)) {            		
						cylinder1.set(false);
						cylinder2.set(true);
						YBA = 2;
						millis  = System.currentTimeMillis();
					} else if(stick.getRawButton(2)) {
						cylinder1.set(false);
						cylinder2.set(true);
						YBA = 1;
						millis  = System.currentTimeMillis();
					} else if(stick.getRawButton(4)) {
						cylinder1.set(false);
						cylinder2.set(true);
						YBA = 0;
						millis  = System.currentTimeMillis();
					}
//				} else if(YBA == 0){ // Either both of the buttons or none of them are depressed.
//					if(System.currentTimeMillis()-millis > MainCylTimeY){
//						cylinder2.set(false);
//						millis  = System.currentTimeMillis();
//					}
//				} else if(YBA == 1) { // Either both of the buttons or none of them are depressed.
//					if(System.currentTimeMillis()-millis > MainCylTimeB){
//						cylinder2.set(false);
//						millis  = System.currentTimeMillis();
//					}
//				} else if(YBA == 2) { // Either both of the buttons or none of them are depressed.
//					if(System.currentTimeMillis()-millis > MainCylTimeA){
//						cylinder2.set(false);
//						millis  = System.currentTimeMillis();
//					}
				}
				
				
				if(cylinder4.get()==false) { // If one of the buttons are pressed but not both.
					if(stick2.getRawButton(4)) {            		
						cylinder3.set(true);
						cylinder4.set(true);
					} else if(stick2.getRawButton(6)) {            		
						cylinder3.set(false);
						cylinder4.set(true);
						YBA2 = 2;
						millis  = System.currentTimeMillis();
					} else if(stick2.getRawButton(7)) {
						cylinder3.set(false);
						cylinder4.set(true);
						YBA2 = 1;
						millis  = System.currentTimeMillis();
					} else if(stick2.getRawButton(8)) {
						cylinder3.set(false);
						cylinder4.set(true);
						YBA2 = 0;
						millis  = System.currentTimeMillis();
					}
//				} else if(YBA2 == 0){ // Either both of the buttons or none of them are depressed.
//					if(System.currentTimeMillis()-millis > MainCylTimeY){
//						cylinder4.set(false);
//						millis  = System.currentTimeMillis();
//					}
//				} else if(YBA2 == 1) { // Either both of the buttons or none of them are depressed.
//					if(System.currentTimeMillis()-millis > MainCylTimeB){
//						cylinder4.set(false);
//						millis  = System.currentTimeMillis();
//					}
//				} else if(YBA2 == 2) { // Either both of the buttons or none of them are depressed.
//					if(System.currentTimeMillis()-millis > MainCylTimeA){
//						cylinder4.set(false);
//						millis  = System.currentTimeMillis();
//					}
				}
				
			}


				/**
				 * If the Y button on the XBOX controller is depressed, the the cylinder will be set to true, which in turn will extend it.
				 * Otherwise, it will stay in its current position? ????? Right cuz false means it stays there?? (Plz verify someone). 
				 * Also we are supposed to have it so if Y is depressed three times it goes back down... somehow 
				 */
				//        if(stick.getRawButton(4)){ // Y Button
				//        	cylinder1.set(false); // Extends
				//        	cylinder2.set(true);
				//        } else if(stick.getRawButton(3)) { // X Button
				//        	cylinder1.set(true); // Recedes
				//        	cylinder2.set(false);
				//        } else {
				//            cylinder1.set(false); // Opposing forces will cancel out doing nothing.
				//        	cylinder2.set(false); // Should use true/true or false/false???
				//        }

			}



			// TODO End



			//MUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDAMUDA
			//Essentially useless autonomous testing
			//        while (isAutonomous() && isEnabled()) {
			//    		while(auto_counter<100){
			//    			auto.Forward();
			//    			auto_counter++;
			//    		}
			//this is the work of an enemy stand
			//    		while(auto_counter>100 && auto_counter < 150){
			//    			auto.RightTurn();
			//    			auto_counter++;
			//    		}
			//    	}
			//  public void autonomousInit() {
			// 	auto_counter = 0;
			//}

			/**
			 * This function is called periodically during autonomous
			 */
			/*    public void autonomousPeriodic() {
    	if(auto_counter < 100) //Check if we've completed 100 loops (approximately 2 seconds)
		{
    		auto.Forward();
			auto_counter++;
			} else {
			auto.Stop(); 	// stop robot
		}
    }
			 */
			//    public void autonomousInit() {
			//    	working = true;
			//     auto_counter = 0;
			//    }
			//     public void autonomousPeriodic() { 
			//    	while (isAutonomous() && isEnabled()) {
			//    		while(auto_counter<100){
			//    			auto.Forward();
			//    			auto_counter++;
			//    		}
			//    		while(auto_counter>100 && auto_counter < 150){
			//    			auto.RightTurn();
			//    			auto_counter++;
			//    		}
			//    	}
			//    }