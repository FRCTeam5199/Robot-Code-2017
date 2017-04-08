package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderShooterFunctions {
	DataBank bank;
	
	private static Encoder shooterEncoder;
	
	private static CANTalon shooterMotor, turretMotor;

	PIDController shooterPID;
	
	Joystick stick = new Joystick(1);
	
	double AVG = 0;
	double shootPow = 0;
	
	public EncoderShooterFunctions(CANTalon shooter, CANTalon turret, DataBank data) {
		shooterMotor = shooter;
		turretMotor = turret;
		bank = data;
//		shooterPID = new PIDController(.1,.001,0,0,bank.flywheel,shooterMotor);
//		shooterPID.enable();
	}

	// MUST be called on robot startup
	public static void initShooterEncoders() {
		turretMotor.setPosition(0);
		
		shooterEncoder.reset();
		shooterEncoder.setDistancePerPulse(RobotMap.inchesPerRotationShooter);
	}
	

	public static void resetShooter() {
		shooterEncoder.reset();
		shooterEncoder.setDistancePerPulse(RobotMap.inchesPerRotationShooter);
	}

	public void displayRpmShooterInfor() {
		SmartDashboard.putNumber("Shooter Data", shooterEncoder.getDistance());
	}

	public void closePIDshoot(){
//		shooterPID.setSetpoint(.1);
	}
	
	public void PIDstop(){
//		shooterPID.setSetpoint(0);
	}
	
	public void turretCenter(){
		if (turretMotor.getPosition() < -2500) {
			turretMotor.set(.4);
		} else if (turretMotor.getPosition() > 2500) {
			turretMotor.set(-.4);
		} else if (turretMotor.getPosition() < -500) {
			turretMotor.set(.15);
		} else if (turretMotor.getPosition() > 500) {
			turretMotor.set(-.15);
		} else {
			turretMotor.set(0);
		}
	}
	
	public void centerPegShoot() {
		// For the close gear peg position
		AVG = bank.shooterRPM();
		if (AVG < 1500) {
			shooterMotor.set(1);
		} else if (AVG < 1650) {
			shooterMotor.set(.9);
		} else if (AVG < 1800) {
			shooterMotor.set(.85);
		} else if (AVG < 2000) {
			shooterMotor.set(.8);
		} else if (AVG < 3425 - (100 * stick.getThrottle())) {
			shootPow += 0.0005;
			shooterMotor.set(shootPow + .56);
		} else {
			shootPow -= 0.0002;
			shooterMotor.set(.56);
		}
	}

	public void boilerHopperShoot() {
		AVG = bank.shooterRPM();
		if (AVG < 1500) {
			shooterMotor.set(1);
		} else if (AVG < 2000) {
			shooterMotor.set(.85);
		} else if (AVG < 2200) {
			shooterMotor.set(.72);
		} else if (AVG < 2450) {
			shooterMotor.set(.54);
		} else if (AVG < 3000) {
			shootPow += 0.0005;
			shooterMotor.set(shootPow + .5);
		} else {
			shootPow -= 0.0002;
			shooterMotor.set(.5);
		}

	}

	public void boilerPegShoot() {
		AVG = bank.shooterRPM();
		if (AVG < 1500) {
			shooterMotor.set(1);
		} else if (AVG < 2000) {
			shooterMotor.set(.85);
		} else if (AVG < 2150) {
			shooterMotor.set(.8);
		} else if (AVG < 2500) {
			shooterMotor.set(.75);
		} else if (AVG < 3145 - (100 * stick.getThrottle())) {
			shootPow += 0.0005;
			shooterMotor.set(shootPow + .54);
		} else {
			shootPow -= 0.0002;
			shooterMotor.set(.54);
		}
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
