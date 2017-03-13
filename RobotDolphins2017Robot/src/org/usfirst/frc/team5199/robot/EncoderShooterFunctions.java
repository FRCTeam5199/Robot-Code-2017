package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderShooterFunctions {
	private static Encoder encoderDIOShooter;
	private static CANTalon shooterMotor, turretMotor;
	private static PIDController controller;
	public EncoderShooterFunctions(CANTalon shooter, CANTalon turret){
		
		shooterMotor = shooter;
		turretMotor = turret;
		
		 turretMotor.reset();
		 turretMotor.reverseSensor(false);
		 turretMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		 turretMotor.configEncoderCodesPerRev(1024);
//		 turretMotor.setPosition(0);
			

	}
	//MUST be called on robot startup
	public static void initShooterEncoders(){
//		turretMotor.setPosition(0);
		encoderDIOShooter.reset();
		encoderDIOShooter.setDistancePerPulse(RobotMap.inchesPerRotationShooter);
	}
	public static void resetShooter(){
		encoderDIOShooter.reset();
		encoderDIOShooter.setDistancePerPulse(RobotMap.inchesPerRotationShooter);
	}
	public static void displayRpmShooterInfor(){
		SmartDashboard.putNumber("Shooter Data",encoderDIOShooter.getDistance());
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
