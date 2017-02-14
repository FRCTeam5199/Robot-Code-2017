package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Encoder;

public class EncoderDIO {
	private static Encoder encoderDIO;
	//waiting for encoder to be available
	public EncoderDIO(){	
		encoderDIO  = new Encoder(RobotMap.encoderRightDIOA, RobotMap.encoderRightDIOB, false, Encoder.EncodingType.k4X);
		encoderDIO.reset(); 
		encoderDIO.setDistancePerPulse(RobotMap.inchesPerRotation);//TODO figure out value for constant
		encoderDIO.setMaxPeriod(RobotMap.stoppedDetectionTime);//sets max time to declared robot is stopped
		encoderDIO.setMinRate(RobotMap.minimumRateStopped);    //sets minimum rate until robot is declared stopped
		
		
		
		
	}
//	//The built in functions for this class and what we intend to do with them are:
//	encoderDIO.getDirection(); 	//	boolean; The last direction the encoder value changed.
//	encoderDIO.getDistance();  	//	double; Get the distance the robot has driven since 
//								//	the last reset as scaled by the value from setDistancePerPulse(double).
//	encoderDIO.getRate(); 		//	double; Get the current rate of the encoder. Units are distance per second as 
//								//	scaled by the value from setDistancePerPulse
//	encoderDIO.reset(); 		//	void; Reset the Encoder distance to zero. Resets the current count to 
//								//	zero on the encoder.
//	encoderDIO.getStopped(); 	//	boolean; Determine if the encoder is stopped. Using the MaxPeriod value, 
//								//	a boolean is returned that is true if the encoder is considered stopped 
//								//	and false if it is still moving. A stopped encoder is one where the most 
//								//	recent pulse width exceeds the MaxPeriod.
}
