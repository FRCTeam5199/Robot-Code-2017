package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderDriveFunctions {
	public static GyroFunctions gyroFunctions;
	private static CANTalon encoder;
	private static Encoder encoderDIORight, encoderDIOLeft, encoderDIOShooter;
	private static RobotDrive robot;
	private static Spark rightMotor, leftMotor;
	public static int distanceRemaining = 0;
	private static boolean EnableDriveStraightCompensation = false;

	public EncoderDriveFunctions(Spark rightMotor2, Spark leftMotor2, GyroFunctions gyro) {
		rightMotor = rightMotor2;
		leftMotor = leftMotor2;
		gyroFunctions = gyro;
		encoderDIORight = new Encoder(RobotMap.encoderRightDIOA, RobotMap.encoderRightDIOB, false,
				Encoder.EncodingType.k4X);
		encoderDIORight.reset();
		encoderDIORight.setDistancePerPulse(RobotMap.inchesPerRotation);

		encoderDIOLeft = new Encoder(RobotMap.encoderLeftDIOA, RobotMap.encoderLeftDIOB, false,
				Encoder.EncodingType.k4X);
		encoderDIOLeft.reset();
		encoderDIOLeft.setDistancePerPulse(RobotMap.inchesPerRotation);

		robot = new RobotDrive(rightMotor, leftMotor);
	}
	//is called every time we use encoders
	public static void initEncoders() {
		encoderDIOLeft.reset();
		encoderDIOLeft.setDistancePerPulse(RobotMap.inchesPerRotation);
		encoderDIORight.reset();
		encoderDIORight.setDistancePerPulse(RobotMap.inchesPerRotation);
	}

	public static void resetDrive() {
		encoderDIOLeft.reset();
		encoderDIORight.reset();
	}
	public static void displayDistance(){
		SmartDashboard.putNumber("Right ENC", encoderDIORight.getDistance()/120);
		SmartDashboard.putNumber("Left ENC", encoderDIOLeft.getDistance()/120);
	}
	public static boolean driveStraightAuton(double distanceForward) {
		double rightDistance, leftDistance;
		int sign;
		boolean rightComplete = false, leftComplete = false;
		double adjustL = 1, adjustR = 1;
		rightDistance = -encoderDIORight.getDistance() / 120;
		leftDistance = encoderDIOLeft.getDistance() / 120;
		SmartDashboard.putNumber("Distance Right Encoder", rightDistance);
		

		if (EnableDriveStraightCompensation) {

			double delta = leftDistance - rightDistance;

			SmartDashboard.putNumber("Delta", delta);
			if (Math.abs(delta) > .25 && rightMotor.get() != 0) { 
				// .25 is a random number to be put in robot map and equals disparity between r and l
				
				if (delta > 0) {// l>r
					// slow down left motor

					adjustL = 1 - Math.abs(delta) * (RobotMap.driveForwardEncoderCompensation); 
					// put .05 in map. this means for one inch of disparity power will be decreased by 5%
					if (adjustL < RobotMap.driveForwardEncoderLimit) {
						adjustL = RobotMap.driveForwardEncoderLimit; // prevents from slowing down too much
					}
				} else { // r>l
							// slow down right motor
					adjustR = 1 - Math.abs(delta) * (RobotMap.driveForwardEncoderCompensation);
					// put .05 in map. this means for one inch of disparity power will be decreased by 5%
					if (adjustR < RobotMap.driveForwardEncoderLimit) {
						adjustR = RobotMap.driveForwardEncoderLimit; // prevents from slowing down too much
					}
				}
			}
		}
		sign = (distanceForward >= 0) ? 1 : -1;
		
		if (Math.abs(rightDistance) < Math.abs(distanceForward) - (8)) {
			rightMotor.set(-1 * .4 * sign * adjustR);
		} else {
			rightComplete = true;
		}

		if (Math.abs(leftDistance) < Math.abs(distanceForward) - (8)) {
			leftMotor.set(1 * .4 * sign * adjustL);
		} else {
			leftComplete = true;
		}
		if (rightComplete && leftComplete) {
			rightMotor.set(.15);
			leftMotor.set(-.15);
			return true;
		} else {
			return false;
		}
	}
	public static boolean loaderTurn(double initialAngle, boolean left){
		//This routine is intended to be used with a button and axis
		//Hold down button to enable loader turn.
		//Stick chooses directions
		double angle;
		if(left){
			angle = -1*RobotMap.loaderTurnAngle;
		}else{
			angle = RobotMap.loaderTurnAngle;
		}
		SmartDashboard.putNumber("Turn Angle From Stick", angle);
		if(turnWithGyrosAndEncoders(angle, initialAngle, RobotMap.loaderTurnOffSet,RobotMap.loaderTurnSpeed)){
			return true;
		}else{
			return false;
		}
	}
	public static boolean autonSixtyDegreeTurn(double initialAngle, boolean left){
		//This routine is intended to be used with a button and axis
		//Hold down button to enable loader turn.
		//Stick chooses directions
		double angle = 43; 
		//changed to 48 for SDR carpet constant
		double offSet;
		//for 60 degree turn and is not in map because it is in function name
		if(left){
			angle = angle;
			offSet = RobotMap.autonSixyTurnOffSetLeft;
		}else{
			angle = -1*angle;
			offSet = RobotMap.autonSixtyTurnOffSetRight;
		}
		SmartDashboard.putNumber("OffSet", offSet);
		if(turnWithGyrosAndEncoders(angle, initialAngle, offSet,RobotMap.autonSixtyTurnSpeed)){
			return true;
		}else{
			return false;
		}
	}
	public static boolean autonNinetyDegreeTurn(double initialAngle, boolean left){
		//This routine is intended to be used with a button and axis
		//Hold down button to enable loader turn.
		//Stick chooses directions
		double angle = 90; 
		double offSet;
		//for 90 degree turn and is not in map because it is in function name
		if(left){
			angle = angle;
			offSet = RobotMap.autonNinetyTurnOffSetLeft;
		}else{
			angle = -1*angle;
			offSet = RobotMap.autonNinetyTurnOffSetRight;
		}
		SmartDashboard.putNumber("OffSet", offSet);
		if(turnWithGyrosAndEncoders(angle, initialAngle, offSet,RobotMap.autonNinetyTurnSpeed)){
			return true;
		}else{
			return false;
		}
	}
	public static boolean turnWithGyrosAndEncoders(double angleTurn, double initialAngle, double offSet, double speed) {
		//******************************************************************
		// Requirement before beginning turn: initEncoders must be called but the calling routine
		//******************************************************************
		//AngleTurn = degrees turned, can be negative
		//InitialAngle is a value read from the gyro by the calling routine before being called
		//offSet is a compensation value. when we turn different amounts, we will over or under rotate based on the gyro.
		//		 This is value is in degrees. It is based on testing, and must be calibrated for every turn amount
		//speed is the speed that the turn takes place, lower speeds require less offset
		double rightDistance, leftDistance;
		double adjustL = 1, adjustR = 1;
		int sign;
		rightDistance = Math.abs(encoderDIORight.getDistance() / 120);
		leftDistance  = Math.abs(encoderDIOLeft.getDistance() / 120);
		double angle  = gyroFunctions.getAngle();
		 SmartDashboard.putNumber("Gyro Angle",angle);
		// SmartDashboard.putNumber("initial angle plus turn amount", Math.abs(initialAngle)+angleTurn);
		
		if (Math.abs(angle)<(Math.abs(Math.abs(initialAngle)+angleTurn ))+ offSet) {
			// the purpose of this if statement is to determine if any significant disparity exists between the left and right drive encoders, and if so, generate a coefficient used to adjust the drive rat of the slower motor to be a little faster
			double delta = leftDistance - rightDistance;

			SmartDashboard.putNumber("Delta", delta);
			if (Math.abs(delta) > .25 && rightMotor.get() != 0) { 
				// .25 is a	random number to be put in robot map and 
				// equals disparity between r and l
				if (delta > 0) {	// l>r
					// speeded up right motor

					adjustR = 1 + Math.abs(delta) * (RobotMap.AdjustRotationCompensation); 
					// In RobatMap, setting AdjustRotationCompensation=.05 in map.
					// this means for one inch of disparity power will be increased by 5%
					if (adjustR > RobotMap.AdjustRotationLimit) {
						adjustR = RobotMap.AdjustRotationLimit; 
						// prevents from speeding up too much
					}
				} else { // r>l
							// speeding up left motor
					adjustL = 1 + Math.abs(delta) * (RobotMap.AdjustRotationCompensation); 
					// In RobatMap, setting AdjustRotationCompensation=.05 in map.
					// this means for one inch of disparity power will be increased by 5%
					if (adjustL >RobotMap.AdjustRotationLimit) {
						adjustL = RobotMap.AdjustRotationLimit; 
						// prevents from speeding up too much
					}
				}
			}
			//SmartDashboard.putNumber("Rotation Adjust Left", adjustL);
			//SmartDashboard.putNumber("Rotation Adjust Right", adjustR);
		
			SmartDashboard.putNumber("Rotation rightDistance", rightDistance);
			SmartDashboard.putNumber("Rotation leftDistance", leftDistance);
			sign = (angleTurn >= 0) ? 1 : -1;

			rightMotor.set(-1 * speed * sign * adjustR);
		
			leftMotor.set(-1 * speed* sign * adjustL);
		
			return false;
		} else {
			return true;
		}

	}
	public static boolean calibrateTurnWithGyrosAndEncoders(double angleTurn, double initialAngle) {
		//------------------------------------------
		// Requirement before beginning turn initEncoders must be called
		//------------------------------------------
		double angleAdjust = SmartDashboard.getNumber("Gyro Adjust");
		double overrideAngle = SmartDashboard.getNumber("Override Angle"); //Testing only
		double speed  = SmartDashboard.getDouble("Turn Speed"); 
		if(turnWithGyrosAndEncoders(overrideAngle, initialAngle, angleAdjust,speed)){
			return true;
		}else{
			return false;
		}
	}
}
