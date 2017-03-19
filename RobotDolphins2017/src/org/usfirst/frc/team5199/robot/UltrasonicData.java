package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class UltrasonicData {
	
	public static final double distanceAwayConstant = RobotMap.ultraDistanceOff;
	
	private static Ultrasonic ultraRight, ultraLeft;
	private static CircularAverageBuffer distanceDataRight, distanceDataLeft;
	private static double range;
	
	public UltrasonicData(int pingChannelRight, int echoChannelRight, int pingChannelLeft, int echoChannelLeft) {

		ultraRight = new Ultrasonic(pingChannelRight, echoChannelRight);
		ultraLeft = new Ultrasonic(pingChannelLeft, echoChannelLeft);
		ultraRight.setAutomaticMode(true);
		ultraLeft.setAutomaticMode(true);
		distanceDataRight = new CircularAverageBuffer(RobotMap.ultrasonicArraySize);
		distanceDataLeft = new CircularAverageBuffer(RobotMap.ultrasonicArraySize);
	}
	
	public static double distanceRight() {
		//returns the adjusted distance the robot is away from an object
		//it is thrown through a circular buffer to throw out erroneous data
		range = ultraLeft.getRangeInches();
		return (distanceDataLeft.DataAverage(range) - distanceAwayConstant);
	}

	public static double distanceLeft() {
		//returns the adjusted distance the robot is away from an object
		//it is thrown through a circular buffer to throw out erroneous data
		range = ultraRight.getRangeInches();
		return (distanceDataRight.DataAverage(range) - distanceAwayConstant);
	}
	
	public static double ultraAverage(){
		//finds the distance the center of the bot is away from an object
		return (distanceRight()+distanceLeft())/2;
	}
	
	public static void leftUltraTest(){
		//used to test to see if ultrasonics are working
		SmartDashboard.putNumber("Left Ultra", ultraLeft.getRangeInches());
	}
	
	public static void rightUltraTest(){
		//used to test to see if ultras are working
		SmartDashboard.putNumber("Right Ultra", ultraRight.getRangeInches());
	}
}

