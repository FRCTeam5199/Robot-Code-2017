package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GyroFunctions {
	public static ADXRS450_Gyro gyro;
	public static RobotDrive robot;
	public static double initAngle;
	Spark rightMotor, leftMotor;
	public GyroFunctions(Spark right, Spark left) {
		rightMotor = right;
		leftMotor = left;
		gyro = new ADXRS450_Gyro();
		gyro.reset();
		gyro.calibrate();
		robot = new RobotDrive(rightMotor, leftMotor);
	}
	public static void resetGyro(){
		gyro.reset();
	}
	public static void initGyro() {
		initAngle = gyro.getAngle();
	}

	public static double getAngle() {
		return gyro.getAngle();
	}

	
	//DONT USE IN COMPETITION BOT
	public static boolean moveDegreesAuton(double angle, double initAngle) {
		//This routine could be handy for basic testing but is not used on actual robot
		//This has no compensation for differing wheel speeds (by weight and drive differences)
		//Instead use turnWithGyrosAndEncoders in EncoderFunctions class
		// double angleFacingInitial = gyro.getAngle();
		// double seperation = 0;
		// SmartDashboard.putNumber("Angle", gyro.getAngle());
		// do{
		//
		// robot.deadTurn(.25, 1);
		// seperation = gyro.getAngle()-angleFacingInitial;
		// }while(Math.abs(seperation-angle)<1);
		// angle = Double.parseDouble(SmartDashboard.getString("Calibrate
		// Rotation"));
		if (gyro.getAngle() < angle + initAngle) {
			robot.deadTurn(.25, 1);
			//SmartDashboard.putDouble("Gyro", gyro.getAngle());
			return false;
		} else {
			robot.stop();
			return true;
		}
	}
	public static double rateOfMotion() {
		double rateOfMotion = gyro.getRate();
		//SmartDashboard.putNumber("Rate of Motion: ", rateOfMotion);
		return rateOfMotion;
	}

}