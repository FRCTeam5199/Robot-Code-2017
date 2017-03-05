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
		
		encoderDIOShooter = new Encoder(RobotMap.encoderShooterDIOA, RobotMap.encoderShooterDIOB, false,
				Encoder.EncodingType.k4X);
		encoderDIOShooter.reset();
		encoderDIOShooter.setDistancePerPulse(RobotMap.inchesPerRotationShooter);
		
		 turretMotor.reset();
		 turretMotor.reverseSensor(false);
		 turretMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		 turretMotor.configEncoderCodesPerRev(1024);
		 turretMotor.setPosition(0);
		 shooterMotor.changeControlMode(TalonControlMode.Speed);
			controller = new PIDController(0.1, 0.001, 0, encoderDIOShooter, shooterMotor);
			controller.setInputRange(0, 12000);
			encoderDIOShooter.setPIDSourceType(PIDSourceType.kRate);
			controller.enable();

	}
	//MUST be called on robot startup
	public static void initShooterEncoders(){
		turretMotor.setPosition(0);
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
	public static boolean setShooterSpeed(int rpms) {
		// TODO change the value of 50 to another value
		double rate = encoderDIOShooter.getRate() * 60;
		if (Math.abs(rpms - rate) < 50) {
			return true;
		} else {
			double power = shooterMotor.get();
			if (Math.abs(power) != 1) {
				if ((rpms - rate) > 0) {
					shooterMotor.set(shooterMotor.get() + .01);
				} else {
					shooterMotor.set(shooterMotor.get() - .01);
				}
				return false;
			}
			return true;
		}
	}
	public static void setShooterSpeed2(int rpms) {
		// TODO change the value of 50 to another value
		//replace with PID constants found
		//maximum change in volts per second
		
		controller.setSetpoint(rpms);
	}
	public static void zeroTurret() {
		while (Math.abs(turretMotor.getPosition()) > 500) {
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
		}
		turretMotor.set(0);
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
