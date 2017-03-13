package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class UltrasonicFunctions {
	public static RobotDrive robot;
	public static UltrasonicData ultraData;
	public static Joystick stick;
	public static final double buffer = .75; // in inches
	public static final double tooClosePixy = 20; // inches
	public static final double distanceFromPeg = 26; // inches

	public UltrasonicFunctions(UltrasonicData ultraData, Spark rightMotor, Spark leftMotor) {
		robot = new RobotDrive(rightMotor, leftMotor);
		stick = new Joystick(0);
		// TODO Auto-generated constructor stub
	}

	public static boolean selfStraight() {
		double seperation = ultraData.distanceRight() - ultraData.distanceLeft();
		if (seperation >= buffer) {
			if (seperation > 10) {
				robot.deadTurn(.5, 1);
			} else if (seperation > 5) {
				robot.deadTurn(.35, 1);
			} else {
				robot.deadTurn(.15, 1);
			}
			return false;
		} else if (seperation <= ((-1) * buffer)) {
			if (seperation < -10) {
				robot.deadTurn(.5, 1);
			} else if (seperation < -5) {
				robot.deadTurn(.35, 1);
			} else {
				robot.deadTurn(.15, 1);
			}
			return false;
		}
		robot.stop();
		return true;
	}

	public static void goBackTooClosePixy() {

		if (ultraData.distanceLeft() < tooClosePixy || ultraData.distanceRight() < tooClosePixy) {
			do {
				robot.drive(-.35, 0, 1);
			} while (ultraData.distanceLeft() < (tooClosePixy + 4) || ultraData.distanceLeft() < (tooClosePixy + 4));
			robot.stop();
		}
	}

	public static void parallelDriveGearLoad(double speed) {
		double distanceRight = ultraData.distanceRight();
		double distanceLeft = ultraData.distanceLeft();
		if (distanceRight > 1 && distanceLeft > 1) {
			double seperation = distanceRight - distanceLeft;
			if (seperation >= buffer) {
				if (seperation > 10) {
					robot.drive(speed, -.5, 1);
				} else if (seperation > 5) {
					robot.drive(speed, -.35, 1);
				} else {
					robot.drive(speed, -.15, 1);
				}
			} else if (seperation <= -1 * buffer) {
				if (seperation < -10) {
					robot.drive(speed, .5, 1);
				} else if (seperation < -5) {
					robot.drive(speed, .35, 1);
				} else {
					robot.drive(speed, .15, 1);
				}
			} else {
				robot.drive(speed, 0, 1);
			}
		}else{
			robot.stop();
		}
	}

	public static boolean goBackTooClosePixyAuton() {

		if (ultraData.distanceLeft() < tooClosePixy || ultraData.distanceRight() < tooClosePixy) {
			if (ultraData.distanceLeft() < (tooClosePixy + 8) || ultraData.distanceLeft() < (tooClosePixy + 8)) {
				robot.drive(-.35, 0, 1);
			}
			return true;
		}
		return false;
	}

	public static void swivelForward(double distance) {
		boolean turnRight = true;
		double timeInBetween = System.currentTimeMillis();
		do {
			// Use this if setting the power of the motor controllers is
			// accurate enough
			if (turnRight) {
				// allows the robot to travel for a third of a second during
				// swivels, probably needs to be smaller
				if ((System.currentTimeMillis() - timeInBetween) > 300) {
					turnRight = false;
					timeInBetween = System.currentTimeMillis();
				}
				robot.drive(-.25, -.1, 1);
			} else {
				if ((System.currentTimeMillis() - timeInBetween) > 300) {
					turnRight = true;
					timeInBetween = System.currentTimeMillis();
				}
				robot.drive(-.25, .1, 1);
			}
		} while (ultraData.distanceLeft() > distance && ultraData.distanceRight() > distance);
		// stops the robot to make sure that the robot does not impale itself
		robot.stop();
	}

	public static void driveFowardUntil(int distance) {
		if (ultraData.distanceLeft() > distance) {
			while (ultraData.distanceLeft() > (distance + 3)) {
				robot.drive(-.25, 0, 1);

			}
			if (ultraData.distanceLeft() < (distance + 6)) {
				while (ultraData.distanceLeft() > (distance)) {
					robot.drive(-.15, 0, 1);

				}
			}
		} else if (ultraData.distanceLeft() > distance - 2) {
			while (ultraData.distanceLeft() > (distance)) {
				robot.drive(.15, 0, 1);

			}
		}
		robot.stop();
	}

	public static boolean driveFowardAuton(int distance) {
		SmartDashboard.putNumber("Distance input", distance);
		SmartDashboard.putNumber("Ultra Average", ultraData.ultraAverage());
		if (ultraData.ultraAverage() > distance) {
			SmartDashboard.putNumber("Distance input", distance);
			if (ultraData.ultraAverage() > (distance + 6)) {
				robot.drive(-.6, 0, 1);
			} else {
				robot.drive(-.35, 0, 1);
			}
		}
		if (ultraData.ultraAverage() < distance) {
			robot.drive(.1, 0, 1);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean driveFowardAutonSDR(int distance) {
		SmartDashboard.putNumber("Distance input", distance);
		SmartDashboard.putNumber("Left Ultra", ultraData.distanceLeft());
		if (ultraData.distanceLeft() > distance) {
			SmartDashboard.putNumber("Distance input", distance);
			if (ultraData.distanceLeft() > (distance + 6)) {
				robot.drive(-.6, 0, 1);
			} else {
				robot.drive(-.35, 0, 1);
			}
		}
		if (ultraData.distanceLeft() < distance) {
			robot.drive(.1, 0, 1);
			return true;
		} else {
			return false;
		}
	}


	public static double ultraAverage() {
		return ultraData.ultraAverage();
	}

	public static void turnUltra(int degrees) {
		// PRECONDITION: the distance between the ultrasonic sensors must be
		// known
		int deltaX = 30; // in inches
		int angleBuffer = 2; // in degrees
		// if degrees off is negative that means robot is turned right
		// if degrees off is positive that means robot is turned left
		double angleOff = Math.atan(ultraData.distanceLeft() / (deltaX))
				- Math.atan(ultraData.distanceRight() / (deltaX));
		if (Math.abs(angleOff - degrees) > angleBuffer) {
			if ((angleOff - degrees) > 0) {
				// turn right
				do {
					angleOff = Math.atan(ultraData.distanceLeft() / (deltaX))
							- Math.atan(ultraData.distanceRight() / (deltaX));
					robot.drive(0, -.25, 1);
				} while ((angleOff - degrees) > angleBuffer);
			} else {
				// turn left
				do {
					angleOff = Math.atan(ultraData.distanceLeft() / (deltaX))
							- Math.atan(ultraData.distanceRight() / (deltaX));
					robot.drive(0, -.25, 1);
				} while ((angleOff - degrees) < (-1 * angleBuffer));
			}
		}

	}
}
