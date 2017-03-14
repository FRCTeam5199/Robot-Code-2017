package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderShooterFunctions {
	private static Encoder shooterEncoder;
	private static CANTalon shooterMotor, turretMotor;
	private static PIDController controller;
	// ------------------------------------------------------------
	// Fink Averaging Zone
	private static int encoderAVGArraySize = 85;
	private static boolean firstBufferRight = true;
	private static double sumBufferRight = 0;
	private static int counterRight = 0;
	private static Double[] distanceArrayRight;
	// Fink Averaging Zone
	// ------------------------------------------------------------

	public EncoderShooterFunctions(CANTalon shooter, CANTalon turret) {

		shooterMotor = shooter;
		turretMotor = turret;

		turretMotor.reset();
		turretMotor.reverseSensor(false);
		turretMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		turretMotor.configEncoderCodesPerRev(1024);
		
		shooterEncoder = new Encoder(RobotMap.encoderShooterDIOA, RobotMap.encoderShooterDIOB, false,
				Encoder.EncodingType.k4X);
		
		distanceArrayRight = new Double[encoderAVGArraySize];
		for (int i = 0; i < encoderAVGArraySize; i++) {
			distanceArrayRight[i] = 0.0;
		}

	}

	// MUST be called on robot startup
	public static void initShooterEncoders() {
		// turretMotor.setPosition(0);
		shooterEncoder.reset();
		shooterEncoder.setDistancePerPulse(RobotMap.inchesPerRotationShooter);
	}

	public static void resetShooter() {
		shooterEncoder.reset();
		shooterEncoder.setDistancePerPulse(RobotMap.inchesPerRotationShooter);
	}

	public static void displayRpmShooterInfor() {
		SmartDashboard.putNumber("Shooter Data", shooterEncoder.getDistance());
	}

	public double EncoderAVG() {

		double range = shooterEncoder.getRate();
		double result;

		sumBufferRight += range - distanceArrayRight[counterRight];

		distanceArrayRight[counterRight++] = range;

		if (counterRight == encoderAVGArraySize) {
			firstBufferRight = false;
			counterRight = 0;
		}

		if (firstBufferRight) {
			result = sumBufferRight / counterRight;

		} else {
			result = sumBufferRight / encoderAVGArraySize;
		}

		return (result);
	}

	public static boolean zeroTurret() {
		if (Math.abs(turretMotor.getPosition()) > 500) {
			if (turretMotor.getPosition() > 0) {
				if (turretMotor.getPosition() > 40000) {
					turretMotor.set(-.5);
				} else if (turretMotor.getPosition() > 10000) {
					turretMotor.set(-.25);
				} else {
					turretMotor.set(-.15);
				}
			} else {
				if (turretMotor.getPosition() < -40000) {
					turretMotor.set(-.5);
				} else if (turretMotor.getPosition() < -10000) {
					turretMotor.set(-.25);
				} else {
					turretMotor.set(-.15);
				}
			}
			return false;
		}
		turretMotor.set(0);
		return true;
	}

	public static boolean checkLimits() {
		// TODO determine values for the limits
		if (Math.abs(turretMotor.getPosition()) > 1000000) {
			return true;
		} else {
			return false;
		}
	}
}
