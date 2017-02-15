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
	public static Joystick xBox = new Joystick(0);
	public static double initAngle;

	public GyroFunctions(Spark rightMotor, Spark leftMotor) {
		gyro = new ADXRS450_Gyro();
		gyro.reset();
		gyro.calibrate();

		robot = new RobotDrive(rightMotor, leftMotor);

	}

	public static void moveDegrees(double angle) {
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
		double initAngle = gyro.getAngle();
		while (gyro.getAngle() < angle + initAngle
				&& !(xBox.getRawButton(1) && xBox.getRawButton(2) && xBox.getRawButton(3) && xBox.getRawButton(4))) {
			robot.deadTurn(.25, 1);
			SmartDashboard.putDouble("Gyro", gyro.getAngle());
		}

		robot.stop();
	}

	public static void moveDegreesTest(double angle) {
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
		initAngle = gyro.getAngle();
		while (gyro.getAngle() < angle + initAngle
				&& !(xBox.getRawButton(1) && xBox.getRawButton(2) && xBox.getRawButton(3) && xBox.getRawButton(4))) {
			SmartDashboard.putDouble("Gyro", gyro.getAngle());
			SmartDashboard.putString("DEUS VULT!1! RECLAIM THE HOLY LAND!!11111!", "turn me left mufucka");
		}
		while (gyro.getAngle() > angle + initAngle
				&& !(xBox.getRawButton(1) && xBox.getRawButton(2) && xBox.getRawButton(3) && xBox.getRawButton(4))) {
			SmartDashboard.putDouble("Gyro", gyro.getAngle());
			SmartDashboard.putString("DEUS VULT!1! RECLAIM THE HOLY LAND!!11111!", "turn me right mufucka");
		}

		SmartDashboard.putString("DEUS VULT!1! RECLAIM THE HOLY LAND!!11111!",
				"YOU DID IT YEE HAW! HOLY LAND STATUS: SAVED");
		robot.stop();
	}

	public static double rateOfMotion() {
		double rateOfMotion = gyro.getRate();
		SmartDashboard.putNumber("Rate of Motion: ", rateOfMotion);
		return rateOfMotion;
	}

}