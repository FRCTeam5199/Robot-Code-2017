package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicFunctions {
	public static RobotDrive robot;
	public static UltrasonicData ultraData;
	public static Joystick stick;
	public static final double buffer = .75; // in inches
	public static final double tooClosePixy = 12; // inches
	public static final double distanceFromPeg =26; // inches

	public UltrasonicFunctions(UltrasonicData ultraData, Talon right, Talon left) {
		robot = new RobotDrive(right, left);
		stick = new Joystick(0);
		// TODO Auto-generated constructor stub
	}

	public static void selfStraight() {
		double seperation = ultraData.distanceRight() - ultraData.distanceLeft();

		if (seperation >= buffer) {
			while (ultraData.distanceRight() - ultraData.distanceLeft() >= buffer && stick.getRawButton(2)) {

				robot.deadTurn(.35, 1);
			}

		} else if (seperation <= ((-1) * buffer)) {
			while (ultraData.distanceLeft() - ultraData.distanceRight() >= buffer && stick.getRawButton(2)) {

				robot.deadTurn(-.35, 1);
			}

		}
	}

	public static void goBackTooClosePixy() {

		if (ultraData.distanceLeft() < tooClosePixy || ultraData.distanceRight() < tooClosePixy) {
			do {
				robot.drive(-.35, 0, 1);
			} while (ultraData.distanceLeft() < (tooClosePixy + 4) || ultraData.distanceLeft() < (tooClosePixy + 4));

		}
	}
	public static void swivelForward(double distance){
		boolean turnRight = true;
		double timeInBetween = System.currentTimeMillis();
		do{
			//Use this if setting the power of the motor controllers is accurate enough
			if(turnRight){
				//allows the robot to travel for a third of a second during swivels, probably needs to be smaller
				if((System.currentTimeMillis()-timeInBetween)>300){
					turnRight 		= false;
					timeInBetween 	= System.currentTimeMillis();
				}
				robot.drive(-.25, -.1, 1);
			}else{
				if((System.currentTimeMillis()-timeInBetween)>300){
					turnRight 		= true;
					timeInBetween 	= System.currentTimeMillis();
				}
				robot.drive(-.25, .1, 1);
			}
		}while(ultraData.distanceLeft()>distance&&ultraData.distanceRight()>distance);
		//stops the robot to make sure that the robot does not impale itself
		robot.stop();
	}

	public static void driveFowardUntil(int distance) {
		if (ultraData.distanceLeft() > distance) {
			while (ultraData.distanceLeft() > (distance + 3)) {
				robot.drive(-.25, 0, 1);
				
			}
			if (ultraData.distanceLeft() < (distance + 3)) {
				while (ultraData.distanceLeft() > (distance)) {
					robot.drive(-.15, 0, 1);

				}
			}
		}else if(ultraData.distanceLeft() > distance -2){
			while (ultraData.distanceLeft() > (distance)) {
				robot.drive(.15, 0, 1);

			}
		}

	}
	public static void turnUltra(int degrees){
		//PRECONDITION: the distance between the ultrasonic sensors must be known
		int deltaX = 30; //in inches
		int angleBuffer = 2; //in degrees
		//if degrees off is negative that means robot is turned right
		//if degrees off is positive that means robot is turned left
		double angleOff = Math.atan(ultraData.distanceLeft()/(deltaX))-Math.atan(ultraData.distanceRight()/(deltaX));
		if(Math.abs(angleOff-degrees)>angleBuffer){
			if((angleOff-degrees)>0){
				//turn right
				do{
				angleOff = Math.atan(ultraData.distanceLeft()/(deltaX))-Math.atan(ultraData.distanceRight()/(deltaX));
				robot.drive(0, -.25, 1);
				}while((angleOff-degrees)>angleBuffer);
			}else{
				//turn left
				do{
					angleOff = Math.atan(ultraData.distanceLeft()/(deltaX))-Math.atan(ultraData.distanceRight()/(deltaX));
					robot.drive(0, -.25, 1);
					}while((angleOff-degrees)<(-1*angleBuffer));
			}
		}
		
			
		}
	}

