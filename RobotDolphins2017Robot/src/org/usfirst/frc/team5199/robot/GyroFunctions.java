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
	public static double slow = .2, fast = .4;
	public static double min = .15;
	public static int StartDecelRamp = 90;
	public static int EndDecelRamp = 10;

	public GyroFunctions(Spark rightMotor, Spark leftMotor) {
		gyro = new ADXRS450_Gyro();
		gyro.reset();
		gyro.calibrate();

		robot = new RobotDrive(rightMotor, leftMotor);

	}

	public static void initGyro() {
		initAngle = gyro.getAngle();
	}

	public static double getAngle() {
		return gyro.getAngle();
	}

	public static void moveDegrees(double angle, double initAngle) {
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
		if (Math.abs(angle) < StartDecelRamp) {
			if (!(Math.abs(gyro.getAngle() + SmartDashboard.getNumber("Gyro Adjust") - (angle + initAngle)) < 1)) {

				if (gyro.getAngle() + SmartDashboard.getNumber("Gyro Adjust") > (angle + initAngle)
						&& !(xBox.getRawButton(1) && xBox.getRawButton(2) && xBox.getRawButton(3)
								&& xBox.getRawButton(4))) {
					SmartDashboard.putDouble("Gyro", gyro.getAngle());
					if ((Math.abs(
							gyro.getAngle() + SmartDashboard.getNumber("Gyro Adjust") - (angle + initAngle)) < 45.0)) {
						robot.deadTurn(-slow, 1);
					} else {
						robot.deadTurn(-fast, 1);
					}

				}
				if (gyro.getAngle() - SmartDashboard.getNumber("Gyro Adjust") < (angle + initAngle)
						&& !(xBox.getRawButton(1) && xBox.getRawButton(2) && xBox.getRawButton(3)
								&& xBox.getRawButton(4))) {
					SmartDashboard.putDouble("Gyro", gyro.getAngle());
					// SmartDashboard.putString("DEUS VULT!1! RECLAIM THE HOLY
					// LAND!!11111!", "turn me right mufucka");

					if ((Math.abs(
							gyro.getAngle() + SmartDashboard.getNumber("Gyro Adjust") - (angle + initAngle)) < 45.0)) {
						robot.deadTurn(slow, 1);
					} else {
						robot.deadTurn(fast, 1);
					}
				}
			}

			//SmartDashboard.putString("DEUS VULT!1! RECLAIM THE HOLY LAND!!11111!","YOU DID IT YEE HAW! HOLY LAND STATUS: SAVED");
		} else {
			if ((angle + initAngle) - (gyro.getAngle() + SmartDashboard.getNumber("Gyro Adjust")) > StartDecelRamp) {
				robot.deadTurn(fast, 1);
			} else if ((angle + initAngle)
					- (gyro.getAngle() + SmartDashboard.getNumber("Gyro Adjust")) < EndDecelRamp) {
				robot.deadTurn(min, 1);
			} else {
				robot.deadTurn(
						(((angle + initAngle)
								- (gyro.getAngle() + SmartDashboard.getNumber("Gyro Adjust")) / StartDecelRamp) * fast),
						1);
			}
		}
	}

	public static boolean moveDegreesAuton(double angle, double initAngle) {
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
			SmartDashboard.putDouble("Gyro", gyro.getAngle());
			return false;
		} else {
			robot.stop();
			return true;
		}
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
		while (gyro.getAngle() > angle + initAngle
				&& !(xBox.getRawButton(1) && xBox.getRawButton(2) && xBox.getRawButton(3) && xBox.getRawButton(4))) {
			SmartDashboard.putDouble("Gyro", gyro.getAngle());
			SmartDashboard.putString("DEUS VULT!1! RECLAIM THE HOLY LAND!!11111!", "turn me left mufucka");
		}
		while (gyro.getAngle() < angle + initAngle
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