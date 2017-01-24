package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class UltrasonicData {
	public static int counterRight =0;
	public static int counterLeft =0;
	public static int echoRight, echoLeft;
	public static final int ultrasonicArraySize = 10;
	public static boolean firstBufferRight = true;
	public static double sumBufferRight=0;
	public static boolean firstBufferLeft= true;
	public static double sumBufferLeft=0;
	//TODO change constant on actual robot
	public static final double distanceAwayConstant =3.5;
	public static Double[] distanceArrayRight, distanceArrayLeft;
	public static Ultrasonic ultraRight, ultraLeft;
	
	public UltrasonicData(int pingChannelRight, int echoChannelRight, int pingChannelLeft, int echoChannelLeft){
		
		ultraRight = new Ultrasonic(pingChannelRight, echoChannelRight);
		ultraLeft = new Ultrasonic(pingChannelLeft, echoChannelLeft);
		ultraRight.setAutomaticMode(true);
		ultraLeft.setAutomaticMode(true);
		distanceArrayRight = new Double[ultrasonicArraySize];
		distanceArrayLeft = new Double[ultrasonicArraySize];
		for(int i =0; i<ultrasonicArraySize; i++){
			distanceArrayRight[i]=0.0;
		}
		for(int i =0; i<ultrasonicArraySize; i++){
			distanceArrayLeft[i]=0.0;
		}
	}
	
	public static double distanceRight(){
		double range = ultraRight.getRangeInches();
		double result;
		
    	sumBufferRight += range-distanceArrayRight[counterRight];
    	
		distanceArrayRight[counterRight++]= range;
		
    	if(counterRight==ultrasonicArraySize){
    		firstBufferRight =false;
    		counterRight =0;
    	}
    	
    	if(firstBufferRight){
    		result = sumBufferRight/counterRight;
        	
    	}else{
    		result= sumBufferRight/ultrasonicArraySize; 	
    	}
    	SmartDashboard.putNumber("Echo Channel Left Distance", (result-distanceAwayConstant));
    	return (result-distanceAwayConstant);
	    }
	public static double distanceLeft(){
		double range = ultraLeft.getRangeInches();
		double result;
		
    	sumBufferLeft += range-distanceArrayLeft[counterLeft];
    	
		distanceArrayLeft[counterLeft++]= range;
		
    	if(counterLeft==ultrasonicArraySize){
    		firstBufferLeft =false;
    		counterLeft =0;
    	}
    	
    	if(firstBufferLeft){
    		result = sumBufferLeft/counterLeft;
        	
    	}else{
    		result= sumBufferLeft/ultrasonicArraySize; 	
    	}
    	SmartDashboard.putNumber("Echo Channel Right Distance", (result-distanceAwayConstant));
    	return (result-distanceAwayConstant);
	    }
	}
	//leftrange = leftultra.getRangeInches();
//	rightrange = rightultra.getRangeInches();
//	SmartDashboard.putNumber("Distance Left", leftrange);
//SmartDashboard.putNumber("Distance Right", rightrange);
	// while(stick2.getRawButton(1)){
	//	if(550 > (leftrange - rightrange) && (leftrange - rightrange) > 1){
	//		leftMotors.set(-.3);
	//		rightMotors.set(-.3);
	//	}
	//	else if((rightrange - leftrange) > 1){
	//		leftMotors.set(.3);
	//		rightMotors.set(.3);

	//	}
//	if(stick2.getRawButton(2)){
//		if((leftrange - rightrange) > 500){
//			leftMotors.set(-0.3);
//			rightMotors.set(-0.3);
//			codePos = "R500";
//		}
//		else if((leftrange - rightrange) > 5){
//			leftMotors.set(-0.2);
//			rightMotors.set(-0.2);
//			codePos = "R20";
//		}
//		else if ((leftrange - rightrange) > 1){
//			leftMotors.set(-0.15);
//			rightMotors.set(-0.15);
//			codePos = "R1";
//		} 
//		else if((rightrange - leftrange) > 500){
//			leftMotors.set(.3);
//			rightMotors.set(.3);
//			codePos = "L500";
//		}
//		else if((rightrange - leftrange) > 5){
//			leftMotors.set(.2);
//			rightMotors.set(.2);
//			codePos = "L20";
//		}
//		else if ((rightrange - leftrange) > 1){
//			leftMotors.set(.15);
//			rightMotors.set(.15);
//			codePos = "L1";
//			}
//		else {
//			leftMotors.stopMotor();
//			rightMotors.stopMotor();
//			codePos = "00";
//		}
//		leftMotors.stopMotor();
//		rightMotors.stopMotor();
//	}
//		Assorted code for the Ultra Squaring System


