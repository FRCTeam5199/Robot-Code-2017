package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Talon;

public class robotDrive {
	public static Talon rightMotor;
	public static Talon leftMotor;

	public robotDrive(Talon right, Talon left) {
		rightMotor = right;
		leftMotor = left;
	}

	public static void drive(double Y, double X, double driveMod){
		leftMotor.set((((Y*-1)* driveMod)+X));
		rightMotor.set(((Y* driveMod)+X));
	}

	public static void deadTurn(double X, double driveMod) {
		leftMotor.set(driveMod * (X));
		rightMotor.set(driveMod * X);

	}
}
