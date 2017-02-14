package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderFunctions {
	private static CANTalon encoder;
	private static Encoder encoderDIORight, encoderDIOLeft;
	private static RobotDrive robot;
	private static Spark rightMotor;
	public EncoderFunctions(Spark rightMotor2, Spark leftMotor){
		rightMotor = rightMotor2;
		encoderDIORight  =new Encoder(RobotMap.encoderRightDIOA, RobotMap.encoderRightDIOB, false, Encoder.EncodingType.k4X);
		encoderDIORight.setDistancePerPulse(RobotMap.inchesPerRotation);
		encoderDIORight.reset();
		
//		encoderDIOLeft  =new Encoder(RobotMap.encoderLeftDIOA, RobotMap.encoderLeftDIOB, false, Encoder.EncodingType.k4X);
//		encoderDIOLeft.reset();
//		encoderDIOLeft.setDistancePerPulse(RobotMap.inchesPerRotation);
		encoder = new CANTalon(RobotMap.canTalonPort);
		encoder.reset();
		encoder.reverseSensor(false);
		encoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
//		encoder.configEncoderCodesPerRev(360);
		encoder.configEncoderCodesPerRev(1024);
		encoder.setPosition(0);
		robot = new RobotDrive(rightMotor2, leftMotor);
	}
	public static void initEncoders(){
//		encoder.setPosition(0);
//		encoderDIOLeft.reset();
		encoderDIORight.setDistancePerPulse(RobotMap.inchesPerRotation);
		encoderDIORight.reset();
	}
	public static void RPMs(){
//		encoder.
//		double encoder775rpm = encoder.getSpeed();
//		encoder775rpm = Math.abs((encoder775rpm +21600)/4);
//		//encoder775rpm = ((encoder775rpm)/4);

//		SmartDashboard.putNumber("Distance Traveled", encoderDIO.getDistance());
//		SmartDashboard.putNumber("Rate", encoderDIO.getRate());
//		SmartDashboard.putNumber("Period", encoderDIO.getPeriod());
//		SmartDashboard.putNumber("Current Speed:", encoder.getSpeed()*30);
//		SmartDashboard.putNumber("Current Speed (Possibly RPMs", encoder.getSpeed()/4);
//		SmartDashboard.putNumber("Current Velocity", encoder.getEncVelocity());
//		SmartDashboard.putNumber("Current Position", encoder.getEncPosition());
//		SmartDashboard.putNumber("Ratio", encoder.getEncVelocity()/encoder.getSpeed());
		SmartDashboard.putNumber("Encoder DIO distance", encoderDIORight.getDistance()/120);//distance appears to be degrees, and 1/3 gear ratio
	}
	public static void turnClicks(double numberOfDegrees, boolean right){
			encoderDIORight.reset();
			encoderDIOLeft.reset();
			do{
				if(right){
					robot.deadTurn(.25, 1);
				}else{
					robot.deadTurn(-.25, 1);
				}
				
			}while(Math.abs(encoderDIOLeft.getDistance()/120)<(numberOfDegrees*RobotMap.distanceTurnedPerDegree)||Math.abs(encoderDIORight.getDistance()/120)<(numberOfDegrees*RobotMap.distanceTurnedPerDegree));
			robot.stop();
		}
	public static void driveForward(double distanceForward){
		// distance = inches	
		// 
		// This routine expects setDistancePerPulse(RobotMap.inchesPerRotation)
		// where inchesPerRotation is set to the circumference of the wheel.
		//
		// If the encoder were connected to the axle, it would return (inches * 360 deg).
		// However, the point on the EVO shifter that the encoder is connected is geared 
		// down from the wheel's axle by a factor of 3 (i.e. 3 turns of the wheel = 1 turn of the encoder)
		// ****
		// Therefore, getDistance is actually returning inches * 120 degrees.  This is 
		// why we divide by 120... to get inches traveled.
		// ****
		// 
		encoderDIORight.reset();
		//	encoderDIOLeft.reset();
			do{
				if(Math.abs(encoderDIORight.getDistance()/120)<RobotMap.rampUpDistance[0]){
					//robot.drive(-1*RobotMap.rampUpSpeed[0], 0, 1);
					rightMotor.set(-1*RobotMap.rampUpSpeed[0]);
				} else if(Math.abs(encoderDIORight.getDistance()/120)<RobotMap.rampUpDistance[1]){
					//robot.drive(-1*RobotMap.rampUpSpeed[1], 0, 1);
					rightMotor.set(-1*RobotMap.rampUpSpeed[1]);
				}else if(distanceForward-Math.abs(encoderDIORight.getDistance()/120)<RobotMap.rampDownDistance[2]){
					//robot.drive(-1*RobotMap.rampDownSpeed[2], 0, 1);
					rightMotor.set(-1*RobotMap.rampDownSpeed[2]);
				}else if(distanceForward-Math.abs(encoderDIORight.getDistance()/120)<RobotMap.rampDownDistance[1]){
					//robot.drive(-1*RobotMap.rampDownSpeed[1], 0, 1);
					rightMotor.set(-1*RobotMap.rampDownSpeed[1]);
					}else if(distanceForward-Math.abs(encoderDIORight.getDistance()/120)<RobotMap.rampDownDistance[0]){
						//robot.drive(-1*RobotMap.rampDownSpeed[0], 0, 1);
						rightMotor.set(-1*RobotMap.rampDownSpeed[0]);
					}else{
						//robot.drive(-1*RobotMap.rampUpSpeed[2], 0, 1);
						rightMotor.set(-1*RobotMap.rampUpSpeed[2]);
					}
				
				RPMs();
			//}while(Math.abs(encoderDIOLeft.getDistance()/120)<distanceForward||Math.abs(encoderDIORight.getDistance()/120)<distanceForward);
	}while(Math.abs((encoderDIORight.getDistance()/120))<distanceForward);
			rightMotor.set(0);
			//robot.stop();
		}
	public static void zeroTurret(){
		while(Math.abs(encoder.getPosition())>500){
			if(encoder.getPosition()>0){
				if(encoder.getPosition()>40000){
//					turret.set(-.5);
					}else if(encoder.getPosition()>10000){
						//turret.set(-.25);
					}else{
//						turret.set(-.15);
					}
			}else{
				if(encoder.getPosition()<-40000){
//				turret.set(-.5);
				}else if(encoder.getPosition()<-10000){
					//turret.set(-.25);
				}else{
//					turret.set(-.15);
				}
			}
		}
		//turret.set(0);
	}

	}
