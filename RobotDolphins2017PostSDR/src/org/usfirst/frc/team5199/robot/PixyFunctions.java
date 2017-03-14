package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PixyFunctions {
	public static PixyProcess pixyProc, pixyProcShooter;
	public static UltrasonicFunctions ultraFunctions;
	public static Pixy gearPixy = new Pixy(0x51);
	public static Pixy shooterPixy = new Pixy(0x53);
	public static EncoderDriveFunctions encoder;
	public static EncoderShooterFunctions encoderShooter;
	public static double turnPower;
	public static RobotDrive robot;
	public static CANTalon turretMotor;

	public PixyFunctions(Pixy pixy, UltrasonicFunctions ultra, EncoderDriveFunctions encoderF, RobotDrive driver) {
		pixyProc = new PixyProcess(gearPixy);
		ultraFunctions = ultra;
		encoder = encoderF;
		robot = driver;
		turretMotor = new CANTalon(1);
	}

	public PixyFunctions(Pixy pixy,CANTalon turret) {
		pixyProcShooter = new PixyProcess(shooterPixy);
		turretMotor = turret;
	}

	public static boolean turnAndGoStraightAuton() {
		if (pixyProc.averageData(0, false, gearPixy)[0] != -1) {
			double distance = pixyProc.compensatedGearPixyData();
			double distanceOff = distance - 100;
			SmartDashboard.putNumber("Distance Off", distanceOff);
			if ((Math.abs(distanceOff) > RobotMap.pixyGearDataBuffer)) {
				int sign = (distanceOff >= 160) ? -1 : 1;
				SmartDashboard.putNumber("Pixy Turn Sign", sign);
				turnPower = ((distanceOff / 20) * .06) * sign;
				SmartDashboard.putNumber("Turn value", turnPower);
				robot.drive(-.4, turnPower, 1);
				return false;
			} else {
				robot.drive(-.4, turnPower, 1);
				return true;
			}
		}else{
			robot.stop();
			SmartDashboard.putString("Pixy Drive Status", "Failed");
		}
		return false;
	}

	public static boolean checkIfAlignedGear() {

		double xAdjustment = pixyProc.compensatedGearPixyData();
		if (Math.abs(xAdjustment - 160) < RobotMap.pixyGearDataBuffer) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkIfAlignedShooter() {
		double xAdjustment = pixyProc.ShooterPixyData()[0];
		double yAdjustment = pixyProc.ShooterPixyData()[1];
		if (Math.abs(xAdjustment - 160) < RobotMap.pixyShooterDataBuffer
				&& Math.abs(yAdjustment - 160) < RobotMap.pixyShooterDataBuffer) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean alignShooterX() {
		// checks to see if the turret is lined up with the boiler
		// if it is not aligned, turret centers on target
		if (pixyProc.shooterData()[0] != -1) {
			double distance = pixyProc.shooterData()[0];
			double distanceOff = distance - 166;
			SmartDashboard.putNumber("Distance Off", distanceOff);
			if ((Math.abs(distanceOff) > RobotMap.pixyShooterDataBuffer)) {
				int sign = (distanceOff >= 0) ? 1 : -1;
				SmartDashboard.putNumber("Pixy Turn sign", sign);
				turnPower = ((Math.abs(distanceOff) / 450) * sign);
				if(Math.abs(turnPower) > .5){
					turnPower = .5*sign;
				}else if(Math.abs(turnPower) < .06){
					turnPower = .06*sign;
				}
//				}else if(turnPower < -.5){
//					turnPower = -.5;
//				}else if(turnPower < ){
//					turnPower = -.5;
//				}
				SmartDashboard.putNumber("Turn value", turnPower);
				SmartDashboard.putString("QPU Status", "Not aligned");
				turretMotor.set(turnPower);
				return false;
			} else {
				turretMotor.set(0);
				SmartDashboard.putString("QPU Status", "QPU ALIGNED!11!!!!");
				return true;
			}
		} else {
			turretMotor.set(0);
			SmartDashboard.putString("QPU Status", "No data");
			return false;
		}
	}
}
