package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicFunctions {
	public static robotDrive robot;
	public static UltrasonicData ultraData;
	public static Joystick stick;
	public static final double buffer = 1; //in inches
	public UltrasonicFunctions(int pingRight, int echoRight, int pingLeft, int echoLeft, Talon right,
			Talon left) {
		ultraData = new UltrasonicData(pingRight, echoRight, pingLeft, echoLeft);
		robot = new robotDrive(right,left);
		stick = new Joystick(0);
		// TODO Auto-generated constructor stub
	}

	public static void selfStraight(){
		double seperation= ultraData.distanceRight()-ultraData.distanceLeft();
		if(seperation >=buffer){
			while(ultraData.distanceRight()-ultraData.distanceLeft()>=buffer&&stick.getRawButton(2)){
				robot.deadTurn(.35,1);
			}
			
		}else if((int) seperation <=((-1)*buffer)){
			while(ultraData.distanceLeft()-ultraData.distanceRight()>=buffer&&stick.getRawButton(2)){
				robot.deadTurn(-.35,1);
			}
			
		}
	}
}
