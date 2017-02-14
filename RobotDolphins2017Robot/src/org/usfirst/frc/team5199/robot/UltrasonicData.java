package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class UltrasonicData {
	public static int counterRight = 0;
	public static int counterLeft = 0;
	public static int echoRight, echoLeft;
	public static final int ultrasonicArraySize = 10;
	public static boolean firstBufferRight = true;
	public static double sumBufferRight = 0;
	public static boolean firstBufferLeft = true;
	public static double sumBufferLeft = 0;
	// TODO change constant on actual robot
	public static final double distanceAwayConstant = RobotMap.ultraDistanceOff;
	public static Double[] distanceArrayRight, distanceArrayLeft;
	public static Ultrasonic ultraRight, ultraLeft;

	public UltrasonicData(int pingChannelRight, int echoChannelRight, int pingChannelLeft, int echoChannelLeft) {

		ultraRight = new Ultrasonic(pingChannelRight, echoChannelRight);
		ultraLeft = new Ultrasonic(pingChannelLeft, echoChannelLeft);
		ultraRight.setAutomaticMode(true);
		ultraLeft.setAutomaticMode(true);
		distanceArrayRight = new Double[ultrasonicArraySize];
		distanceArrayLeft = new Double[ultrasonicArraySize];
		for (int i = 0; i < ultrasonicArraySize; i++) {
			distanceArrayRight[i] = 0.0;
		}
		for (int i = 0; i < ultrasonicArraySize; i++) {
			distanceArrayLeft[i] = 0.0;
		}
	}
	
	public static double distanceRight() {
		double range = ultraRight.getRangeInches();
		double result;

		sumBufferRight += range - distanceArrayRight[counterRight];

		distanceArrayRight[counterRight++] = range;

		if (counterRight == ultrasonicArraySize) {
			firstBufferRight = false;
			counterRight = 0;
		}

		if (firstBufferRight) {
			result = sumBufferRight / counterRight;

		} else {
			result = sumBufferRight / ultrasonicArraySize;
		}
		SmartDashboard.putNumber("Echo Channel Right Distance", (result - distanceAwayConstant));
		return (result - distanceAwayConstant);
	}

	public static double distanceLeft() {
		double range = ultraLeft.getRangeInches();
		double result;

		sumBufferLeft += range - distanceArrayLeft[counterLeft];

		distanceArrayLeft[counterLeft++] = range;

		if (counterLeft == ultrasonicArraySize) {
			firstBufferLeft = false;
			counterLeft = 0;
		}

		if (firstBufferLeft) {
			result = sumBufferLeft / counterLeft;

		} else {
			result = sumBufferLeft / ultrasonicArraySize;
		}
		SmartDashboard.putNumber("Echo Channel Left Distance", (result - distanceAwayConstant));
		return (result - distanceAwayConstant);
	}
	public static void leftUltraTest(){
		SmartDashboard.putNumber("Left Ultra", ultraLeft.getRangeInches());
	}
	public static void rightUltraTest(){
		SmartDashboard.putNumber("Right Ultra", ultraRight.getRangeInches());
	}
	public static double ultraAverage(){
		return (distanceRight()+distanceLeft())/2;
	}
}

