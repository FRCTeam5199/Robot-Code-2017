package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderFunctions {
	private static CANTalon encoder;
	private static Encoder encoderPwm;
	private static RobotDrive robot;
	public EncoderFunctions(Talon right, Talon left){
		encoderPwm  =new Encoder(RobotMap.encoderPwmA, RobotMap.encoderPwmB, false, Encoder.EncodingType.k4X);
		encoderPwm.reset();
		encoderPwm.setDistancePerPulse(RobotMap.clicksPerInch);
		encoder = new CANTalon(RobotMap.canTalonPort);
		encoder.reset();
		encoder.reverseSensor(false);
		encoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
//		encoder.configEncoderCodesPerRev(360);
		encoder.configEncoderCodesPerRev(1024);
		encoder.setPosition(0);
		robot = new RobotDrive(right, left);
	}
	public static void RPMs(){
		
		double encoder775rpm = encoder.getSpeed();
//		encoder775rpm = Math.abs((encoder775rpm +21600)/4);
//		//encoder775rpm = ((encoder775rpm)/4);

		SmartDashboard.putNumber("Distance Traveled", encoderPwm.getDistance());
		SmartDashboard.putNumber("Rate", encoderPwm.getRate());
		SmartDashboard.putNumber("Period", encoderPwm.getPeriod());
		SmartDashboard.putNumber("Current Speed:", encoder.getSpeed());
		SmartDashboard.putNumber("Current Speed (Possibly RPMs", encoder.getSpeed()/4);
		SmartDashboard.putNumber("Current Velocity", encoder.getEncVelocity());
		SmartDashboard.putNumber("Current Position", encoder.getEncPosition());
		
	}
	public static void turnClicks(double numberOfClicks, boolean right){
			encoderPwm.reset();
			do{
				if(right){
					robot.deadTurn(-.25, 1);
				}else{
					robot.deadTurn(.25, 1);
				}
			}while(//experiment);
			robot.stop();
		}
	public static void driveForward(double distanceForward){
		encoderPwm.reset();
		do{
			robot.drive(.25, 0, 1);
		}while(encoderPwm.getDistance()<distanceForward);
		robot.stop();
	}
}
