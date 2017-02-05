package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class encoderFunctions {
	public static Encoder encoderLeft, encoderRight;
	
	public static double motorRight=-.25, motorLeft=-.25;
	public encoderFunctions(){
		encoderRight = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
		encoderRight.setMaxPeriod(.1);
		encoderRight.setMinRate(10);
		encoderRight.setDistancePerPulse(5);
		encoderRight.setReverseDirection(true);
		encoderRight.setSamplesToAverage(7);
		
		
		
	}
	public static void putData(){
		SmartDashboard.putNumber("Rate", encoderRight.getRate());
		SmartDashboard.putNumber("Distance", encoderRight.getDistance());
	}
	public static void forward(){
		
		while(encoderRight.getRate()>encoderLeft.getRate()+2){
			motorRight +=.01;
		}
		while(encoderLeft.getRate()>encoderRight.getRate()+2){
			motorLeft +=.01;
		}
		if(motorRight<.2&&motorLeft<.2){
			motorRight=-.25;
			motorLeft=-.25;
		}
		
	}
}
