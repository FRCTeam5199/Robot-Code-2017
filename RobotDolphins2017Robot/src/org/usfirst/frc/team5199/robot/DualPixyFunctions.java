package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DualPixyFunctions {
	// public static PixyProcess pixyProc, pixyProcShooter;
	public static DualPixyProcess DualPixyProc;
	public static Pixy gearPixy, shooterPixy;
	public static UltrasonicFunctions ultraFunctions;
	public static EncoderDriveFunctions encoder;
	public static EncoderShooterFunctions encoderShooter;
	public static double turnPower;
	public static RobotDrive robot;
	public static CANTalon turretMotor;

	public DualPixyFunctions(UltrasonicFunctions ultra, EncoderDriveFunctions encoderF, RobotDrive driver) {
		// pixyProc = new PixyProcess(pixy);
		ultraFunctions = ultra;
		encoder = encoderF;
		robot = driver;
		turretMotor = new CANTalon(1);
		DualPixyProc = new DualPixyProcess();
		gearPixy = new Pixy(0x51);
		shooterPixy = new Pixy(0x53);
	}

	public static boolean turnAndGoStraightAuton() {
		if (DualPixyProc.averageData(0, false, gearPixy)[0] != -1) {
			double distance = DualPixyProc.compensatedGearPixyData();
			double distanceOff = distance - 160;
			SmartDashboard.putNumber("Distance Off", distanceOff);
			if ((Math.abs(distanceOff) > RobotMap.pixyGearDataBuffer)) {
				int sign = (distanceOff >= 160) ? -1 : 1;
				SmartDashboard.putNumber("Pixy Turn Sign", sign);
				turnPower = ((distanceOff / 20) * .06) * sign;
				SmartDashboard.putNumber("Turn value", turnPower);
				robot.drive(-.2, turnPower, 1);
				return false;
			} else {
				robot.stop();
				return true;
			}
		}
		return false;
	}

	public static boolean checkIfAlignedGear() {

		double xAdjustment = DualPixyProc.compensatedGearPixyData();
		if (Math.abs(xAdjustment - 160) < RobotMap.pixyGearDataBuffer) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkIfAlignedShooter() {
		double xAdjustment = DualPixyProc.ShooterPixyData()[0];
		double yAdjustment = DualPixyProc.ShooterPixyData()[1];
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

		if (DualPixyProc.shooterData()[0] != -1) {
			double distance = DualPixyProc.shooterData()[0];
			double distanceOff = distance - 152;
			SmartDashboard.putNumber("Distance Off", distanceOff);
			if ((Math.abs(distanceOff) > RobotMap.pixyShooterDataBuffer)) {
				int sign = (distanceOff >= 0) ? 1 : -1;
				SmartDashboard.putNumber("Pixy Turn sign", sign);
				turnPower = ((Math.abs(distanceOff) / 450) * sign);
				if (Math.abs(turnPower) > .5) {
					turnPower = .5 * sign;
				} else if (Math.abs(turnPower) < .13) {
					turnPower = .13 * sign;
				}
				// }else if(turnPower < -.5){
				// turnPower = -.5;
				// }else if(turnPower < ){
				// turnPower = -.5;
				// }
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
