package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class gyroData {
	public static AnalogGyro gyro;
	public gyroData(int analogInputChannel){
		gyro = new AnalogGyro(analogInputChannel);
		gyro.initGyro();
		gyro.calibrate();
		
	}
	public static double getAngle(){
		double angleFacing = gyro.getAngle();
		SmartDashboard.putNumber("Angle Facing: ", angleFacing);
		return angleFacing;
	}
	public static double rateOfMotion(){
		double rateOfMotion = gyro.getRate();
		SmartDashboard.putNumber("Rate of Motion: ", rateOfMotion);
		return rateOfMotion;
	}
	
	
}