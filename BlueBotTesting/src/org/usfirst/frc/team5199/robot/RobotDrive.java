package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

public class RobotDrive {
	public static Victor rightMotor;
	public static Victor leftMotor;

	public RobotDrive(Victor right, Victor left) {
		rightMotor = right;
		leftMotor = left;
	}

	// this is the routine that we expect to used to drive the bot in manual
	// mode.
	public static void drive(double Y, double X, double driveMod) {
		leftMotor.set((((Y)* driveMod)+X));
		rightMotor.set(((Y* driveMod)-X));

	}
	// May be useful for auton or gyro turning.
	public static void deadTurn(double X, double driveMod) {
		leftMotor.set(driveMod * (X) * 1.2);
		rightMotor.set(driveMod * X);

	}
	// Useful for autonomous.
	public static void stop() {
		leftMotor.set(0);
		rightMotor.set(0);
	}
}
