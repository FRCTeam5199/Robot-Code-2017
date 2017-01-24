package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class gyroData {
	public static ADXRS450_Gyro gyro;
	public static robotDrive robot;
	public gyroData( Talon right, Talon left){
		gyro = new ADXRS450_Gyro();
		gyro.reset();
		gyro.calibrate();
		
		robot = new robotDrive(right,left);
		
	}
	public static void moveDegrees(double angle){
//		double angleFacingInitial = gyro.getAngle();
//		double seperation = 0;
//		SmartDashboard.putNumber("Angle", gyro.getAngle());
//		do{
//			
//			robot.deadTurn(.25, 1);
//			seperation = gyro.getAngle()-angleFacingInitial;
//		}while(Math.abs(seperation-angle)<1);
		//angle = Double.parseDouble(SmartDashboard.getString("Calibrate Rotation"));
		double initAngle = gyro.getAngle();
		while(gyro.getAngle() < angle + initAngle){
			robot.deadTurn(.25, 1);			
			SmartDashboard.putDouble("Gyro",gyro.getAngle());
		}
		
			
		robot.stop();
	}
		
		
	
	public static double rateOfMotion(){
		double rateOfMotion = gyro.getRate();
		SmartDashboard.putNumber("Rate of Motion: ", rateOfMotion);
		return rateOfMotion;
	}
	
	
}