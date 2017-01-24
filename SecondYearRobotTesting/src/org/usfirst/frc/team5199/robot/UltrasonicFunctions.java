package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicFunctions {
	public static robotDrive robot;
	public static UltrasonicData ultraData;
	public static Joystick stick;
	public static final double buffer = .75; // in inches
	public static final double tooClosePixy = 12; // inches
	public static final double distanceFromPeg =26; // inches

	public UltrasonicFunctions(UltrasonicData ultraData, Talon right, Talon left) {
		robot = new robotDrive(right, left);
		stick = new Joystick(0);
		// TODO Auto-generated constructor stub
	}

	public static void selfStraight() {
		double seperation = ultraData.distanceRight() - ultraData.distanceLeft();

		if (seperation >= buffer) {
			while (ultraData.distanceRight() - ultraData.distanceLeft() >= buffer && stick.getRawButton(2)) {

				robot.deadTurn(.35, 1);
			}

		} else if (seperation <= ((-1) * buffer)) {
			while (ultraData.distanceLeft() - ultraData.distanceRight() >= buffer && stick.getRawButton(2)) {

				robot.deadTurn(-.35, 1);
			}

		}
	}

	public static void pixySelfStraight() {
		double seperation = ultraData.distanceRight() - ultraData.distanceLeft();
		do {
			seperation = ultraData.distanceRight() - ultraData.distanceLeft();

			if (seperation >= buffer) {
				while (ultraData.distanceRight() - ultraData.distanceLeft() >= buffer && stick.getRawButton(3)) {

					robot.deadTurn(.35, 1);
				}

			} else if (seperation <= ((-1) * buffer)) {
				while (ultraData.distanceLeft() - ultraData.distanceRight() >= buffer && stick.getRawButton(3)) {

					robot.deadTurn(-.35, 1);
				}
				robot.stop();

			}
		} while (seperation >= buffer || seperation <= (-1 * buffer));
	}

	public static void goBackTooClosePixy() {

		if (ultraData.distanceRight() < tooClosePixy || ultraData.distanceRight() < tooClosePixy) {
			do {
				robot.drive(-.35, 0, 1);
			} while (ultraData.distanceRight() < (tooClosePixy + 4) || ultraData.distanceRight() < (tooClosePixy + 4));

		}
	}

	public static void driveFowardUntil() {
		if (ultraData.distanceLeft() > distanceFromPeg) {
			while (ultraData.distanceLeft() > (distanceFromPeg + 3)) {
				robot.drive(-.25, 0, 1);
				
			}
			if (ultraData.distanceLeft() < (distanceFromPeg + 3)) {
				while (ultraData.distanceLeft() > (distanceFromPeg)) {
					robot.drive(-.15, 0, 1);

				}
			}
		}

	}
}
