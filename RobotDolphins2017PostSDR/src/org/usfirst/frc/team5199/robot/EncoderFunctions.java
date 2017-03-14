package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EncoderFunctions {
	private static CANTalon encoder;
	private static Encoder encoderDIORight, encoderDIOLeft, encoderDIOShooter;
	private static RobotDrive robot;
	private static Spark rightMotor, leftMotor;
	private static CANTalon shooterMotor;
	public static int distanceRemaining = 0;
	private static boolean EnableDriveStraightCompensation = false;

	public EncoderFunctions(Spark rightMotor2, Spark leftMotor2, CANTalon shooter) {
		rightMotor = rightMotor2;
		leftMotor = leftMotor2;
		shooterMotor = shooter;

		encoderDIORight = new Encoder(RobotMap.encoderRightDIOA, RobotMap.encoderRightDIOB, false,
				Encoder.EncodingType.k4X);
		encoderDIORight.reset();
		encoderDIORight.setDistancePerPulse(RobotMap.inchesPerRotation);

		encoderDIOLeft = new Encoder(RobotMap.encoderLeftDIOA, RobotMap.encoderLeftDIOB, false,
				Encoder.EncodingType.k4X);
		encoderDIOLeft.reset();
		encoderDIOLeft.setDistancePerPulse(RobotMap.inchesPerRotation);

		encoderDIOShooter = new Encoder(RobotMap.encoderShooterDIOA, RobotMap.encoderShooterDIOB, false,
				Encoder.EncodingType.k4X);
		encoderDIOShooter.reset();
		encoderDIOShooter.setDistancePerPulse(RobotMap.inchesPerRotationShooter);

		//
		// encoder = new CANTalon(RobotMap.canTalonPort);
		// encoder.reset();
		// encoder.reverseSensor(false);
		// encoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		// // encoder.configEncoderCodesPerRev(360);
		// encoder.configEncoderCodesPerRev(1024);
		// encoder.setPosition(0);
		robot = new RobotDrive(rightMotor2, leftMotor);
	}

	public static void initEncoders() {
		// encoder.setPosition(0);
		encoderDIOLeft.reset();
		encoderDIOLeft.setDistancePerPulse(RobotMap.inchesPerRotation);
		encoderDIORight.reset();
		encoderDIORight.setDistancePerPulse(RobotMap.inchesPerRotation);
	}

	public static void resetDrive() {
		encoderDIOLeft.reset();
		encoderDIORight.reset();
	}

	public static void RPMs() {
		// encoder.
		// double encoder775rpm = encoder.getSpeed();
		// encoder775rpm = Math.abs((encoder775rpm +21600)/4);
		// //encoder775rpm = ((encoder775rpm)/4);

		// SmartDashboard.putNumber("Distance Traveled",
		// encoderDIO.getDistance());
		// SmartDashboard.putNumber("Rate", encoderDIO.getRate());
		// SmartDashboard.putNumber("Period", encoderDIO.getPeriod());
		// SmartDashboard.putNumber("Current Speed:", encoder.getSpeed()*30);
		// SmartDashboard.putNumber("Current Speed (Possibly RPMs",
		// encoder.getSpeed()/4);
		// SmartDashboard.putNumber("Current Velocity",
		// encoder.getEncVelocity());
		// SmartDashboard.putNumber("Current Position",
		// encoder.getEncPosition());
		// SmartDashboard.putNumber("Ratio",
		// encoder.getEncVelocity()/encoder.getSpeed());
		SmartDashboard.putNumber("Encoder DIO right", -encoderDIORight.getDistance() / 120);// distance
																							// appears
																							// to
																							// be
																							// degrees,
																							// and
																							// 1/3
																							// gear
																							// ratio
		SmartDashboard.putNumber("Encoder DIO left", encoderDIOLeft.getDistance() / 120);
		SmartDashboard.putNumber("Shooter RPM", encoderDIOShooter.getRate());
		SmartDashboard.putNumber("Shooter sheez", encoderDIOShooter.getDistance());

		// SmartDashboard.putNumber("Encoder distance per rotation",
		// encoderDIOShooter.getDistance());
	}

	public static void turnClicks(double numberOfDegrees, boolean right) {
		encoderDIORight.reset();
		encoderDIOLeft.reset();
		do {
			if (right) {
				robot.deadTurn(.25, 1);
			} else {
				robot.deadTurn(-.25, 1);
			}

		} while (Math.abs(encoderDIOLeft.getDistance() / 120) < (numberOfDegrees * RobotMap.distanceTurnedPerDegree)
				|| Math.abs(
						encoderDIORight.getDistance() / 120) < (numberOfDegrees * RobotMap.distanceTurnedPerDegree));
		robot.stop();
	}

	public static void driveForwardFink(double distanceForward) {
		// distance = inches
		//
		// This routine expects setDistancePerPulse(RobotMap.inchesPerRotation)
		// where inchesPerRotation is set to the circumference of the wheel.
		//
		// If the encoder were connected to the axle, it would return (inches *
		// 360 deg).
		// However, the point on the EVO shifter that the encoder is connected
		// is geared
		// down from the wheel's axle by a factor of 3 (i.e. 3 turns of the
		// wheel = 1 turn of the encoder)
		// ****
		// Therefore, getDistance is actually returning inches * 120 degrees.
		// This is
		// why we divide by 120... to get inches traveled.
		// ****
		//
		encoderDIORight.reset();
		// encoderDIOLeft.reset();
		do {
			if (Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampUpDistance[0]) {
				// robot.drive(-1*RobotMap.rampUpSpeed[0], 0, 1);
				rightMotor.set(-1 * RobotMap.rampUpSpeed[0]);
			} else if (Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampUpDistance[1]) {
				// robot.drive(-1*RobotMap.rampUpSpeed[1], 0, 1);
				rightMotor.set(-1 * RobotMap.rampUpSpeed[1]);
			} else if (distanceForward - Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampDownDistance[2]) {
				// robot.drive(-1*RobotMap.rampDownSpeed[2], 0, 1);
				rightMotor.set(-1 * RobotMap.rampDownSpeed[2]);
			} else if (distanceForward - Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampDownDistance[1]) {
				// robot.drive(-1*RobotMap.rampDownSpeed[1], 0, 1);
				rightMotor.set(-1 * RobotMap.rampDownSpeed[1]);
			} else if (distanceForward - Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampDownDistance[0]) {
				// robot.drive(-1*RobotMap.rampDownSpeed[0], 0, 1);
				rightMotor.set(-1 * RobotMap.rampDownSpeed[0]);
			} else {
				// robot.drive(-1*RobotMap.rampUpSpeed[2], 0, 1);
				rightMotor.set(-1 * RobotMap.rampUpSpeed[2]);
			}

			RPMs();
			// }while(Math.abs(encoderDIOLeft.getDistance()/120)<distanceForward||Math.abs(encoderDIORight.getDistance()/120)<distanceForward);
		} while (Math.abs((encoderDIORight.getDistance() / 120)) < distanceForward);
		rightMotor.set(0);
		// robot.stop();
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

	public static boolean driveStraightAuton(double distanceForward) {
		double rightDistance, leftDistance;
		int sign;
		boolean rightComplete = false, leftComplete = false;
		double adjustL = 1, adjustR = 1;
		rightDistance = -encoderDIORight.getDistance() / 120;
		leftDistance = encoderDIOLeft.getDistance() / 120;

		if (EnableDriveStraightCompensation) {

			double delta = leftDistance - rightDistance;

			SmartDashboard.putNumber("Delta", delta);
			if (Math.abs(delta) > .25 && rightMotor.get() != 0) { // .25 is a
																	// random
																	// number to
																	// be
																	// put in
																	// robot
																	// map and
																	// equals
																	// disparity
																	// between r
																	// and
																	// l
				if (delta > 0) {// l>r
					// slow down left motor

					adjustL = 1 - Math.abs(delta) * (.05); // put .05 in map.
															// this
															// means for one
															// inch of
															// disparity power
															// will
															// be decreased by
															// 5%
					if (adjustL < .5) {
						adjustL = .5; // prevents from slowing down too much
					}
				} else { // r>l
							// slow down right motor
					adjustR = 1 - Math.abs(delta) * (.05); // put .05 in map.
															// this
															// means for one
															// inch of
															// disparity power
															// will
															// be decreased by
															// 5%
					if (adjustR < .5) {
						adjustR = .5; // prevents from slowing down too much
					}
				}
			}

			SmartDashboard.putNumber("Adjust Left", adjustL);
			SmartDashboard.putNumber("Adjust Right", adjustR);
		}
		SmartDashboard.putNumber("rightDistance", rightDistance);
		SmartDashboard.putNumber("leftDistance", leftDistance);
		sign = (distanceForward >= 0) ? 1 : -1;

		if (Math.abs(rightDistance) < Math.abs(distanceForward) - (24)) {

			rightMotor.set(-1 * .8 * sign * adjustR);
		} else {
			rightComplete = true;
		}

		if (Math.abs(leftDistance) < Math.abs(distanceForward) - (24)) {
			leftMotor.set(1 * .8 * sign * adjustL);
		} else {
			leftComplete = true;
		}

		if (rightComplete && leftComplete) {
			rightMotor.set(.15);
			leftMotor.set(-.15);
			return true;
		} else {
			return false;
		}

	}
	// public static boolean driveStraightAuton(double distanceForward) {
	// double rightDistance, leftDistance;
	// int sign;
	// boolean rightGood = false, leftGood = false;
	//
	// rightDistance = -encoderDIORight.getDistance() / 120;
	// leftDistance = encoderDIOLeft.getDistance() / 120;
	//
	// SmartDashboard.putNumber("rightDistance", rightDistance);
	// SmartDashboard.putNumber("leftDistance", leftDistance);
	//
	// sign = (distanceForward >= 0) ? 1 : -1;
	//
	// if (Math.abs(rightDistance) < Math.abs(distanceForward)) {
	//
	// rightMotor.set(-1 * .5 * sign);
	// } else {
	// rightGood = true;
	// rightMotor.set(0);
	// }
	//
	// if (Math.abs(leftDistance) < Math.abs(distanceForward)) {
	// leftMotor.set(1.2 * .5 * sign);
	// } else {
	// leftGood = true;
	// leftMotor.set(0);
	// }
	//
	// if (rightGood && leftGood) {
	// return true;
	// } else {
	// return false;
	// }
	//
	// }

	public static boolean driveForwardAutonFink(double distanceForward) {
		// distance = inches
		//
		// This routine expects setDistancePerPulse(RobotMap.inchesPerRotation)
		// where inchesPerRotation is set to the circumference of the wheel.
		//
		// If the encoder were connected to the axle, it would return (inches *
		// 360 deg).
		// However, the point on the EVO shifter that the encoder is connected
		// is geared
		// down from the wheel's axle by a factor of 3 (i.e. 3 turns of the
		// wheel = 1 turn of the encoder)
		// ****
		// Therefore, getDistance is actually returning inches * 120 degrees.
		// This is
		// why we divide by 120... to get inches traveled.
		// ****
		//
		// encoderDIORight.reset();
		// encoderDIOLeft.reset();
		if (Math.abs((encoderDIORight.getDistance() / 120)) < distanceForward) {
			boolean speedSet = false;
			if (Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampUpDistance[0]) {
				// robot.drive(-1*RobotMap.rampUpSpeed[0], 0, 1);
				rightMotor.set(-1 * RobotMap.rampUpSpeed[0]);

				speedSet = true;
			} else if (Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampUpDistance[1]) {
				// robot.drive(-1*RobotMap.rampUpSpeed[1], 0, 1);
				rightMotor.set(-1 * RobotMap.rampUpSpeed[1]);
				speedSet = true;
			}

			if (distanceForward - Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampDownDistance[2]) {
				// robot.drive(-1*RobotMap.rampDownSpeed[2], 0, 1);
				rightMotor.set(-1 * RobotMap.rampDownSpeed[2]);
				speedSet = true;
			} else if (distanceForward - Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampDownDistance[1]) {
				// robot.drive(-1*RobotMap.rampDownSpeed[1], 0, 1);
				rightMotor.set(-1 * RobotMap.rampDownSpeed[1]);
				speedSet = true;
			} else if (distanceForward - Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampDownDistance[0]) {
				// robot.drive(-1*RobotMap.rampDownSpeed[0], 0, 1);
				rightMotor.set(-1 * RobotMap.rampDownSpeed[0]);
				speedSet = true;
			}
			if (!speedSet) {
				// robot.drive(-1*RobotMap.rampUpDistance[2], 0, 1);
				rightMotor.set(-1 * RobotMap.rampDownSpeed[2]);
			}
			RPMs();

			// }while(Math.abs(encoderDIOLeft.getDistance()/120)<distanceForward||Math.abs(encoderDIORight.getDistance()/120)<distanceForward);
		}
		if (Math.abs(encoderDIOLeft.getDistance() / 120) < distanceForward) {
			boolean speedSet = false;
			if (Math.abs(encoderDIOLeft.getDistance() / 120) < RobotMap.rampUpDistance[0]) {
				// robot.drive(-1*RobotMap.rampUpSpeed[0], 0, 1);
				leftMotor.set(RobotMap.rampUpSpeed[0]);

				speedSet = true;
			} else if (Math.abs(encoderDIOLeft.getDistance() / 120) < RobotMap.rampUpDistance[1]) {
				// robot.drive(-1*RobotMap.rampUpSpeed[1], 0, 1);
				leftMotor.set(RobotMap.rampUpSpeed[1]);
				speedSet = true;
			}

			if (distanceForward - Math.abs(encoderDIOLeft.getDistance() / 120) < RobotMap.rampDownDistance[2]) {
				// robot.drive(-1*RobotMap.rampDownSpeed[2], 0, 1);
				leftMotor.set(RobotMap.rampDownSpeed[2]);
				speedSet = true;
			} else if (distanceForward - Math.abs(encoderDIOLeft.getDistance() / 120) < RobotMap.rampDownDistance[1]) {
				// robot.drive(-1*RobotMap.rampDownSpeed[1], 0, 1);
				leftMotor.set(RobotMap.rampDownSpeed[1]);
				speedSet = true;
			} else if (distanceForward - Math.abs(encoderDIOLeft.getDistance() / 120) < RobotMap.rampDownDistance[0]) {
				// robot.drive(-1*RobotMap.rampDownSpeed[0], 0, 1);
				leftMotor.set(RobotMap.rampDownSpeed[0]);
				speedSet = true;
			}
			if (!speedSet) {
				leftMotor.set(-RobotMap.rampDownSpeed[2]);
			}
			return false;
		} else {
			return true;
		}

		// robot.stop();
	}

	public static boolean driveBackwardsAuton(double distanceBackwards) {
		// distance = inches
		//
		// This routine expects setDistancePerPulse(RobotMap.inchesPerRotation)
		// where inchesPerRotation is set to the circumference of the wheel.
		//
		// If the encoder were connected to the axle, it would return (inches *
		// 360 deg).
		// However, the point on the EVO shifter that the encoder is connected
		// is geared
		// down from the wheel's axle by a factor of 3 (i.e. 3 turns of the
		// wheel = 1 turn of the encoder)
		// ****
		// Therefore, getDistance is actually returning inches * 120 degrees.
		// This is
		// why we divide by 120... to get inches traveled.
		// ****
		//
		// encoderDIORight.reset();
		// encoderDIOLeft.reset();
		if (Math.abs((encoderDIORight.getDistance() / 120)) < distanceBackwards) {
			if (Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampUpDistance[0]) {
				// robot.drive(-1*RobotMap.rampUpSpeed[0], 0, 1);
				rightMotor.set(1 * RobotMap.rampUpSpeed[0]);
			} else if (Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampUpDistance[1]) {
				// robot.drive(-1*RobotMap.rampUpSpeed[1], 0, 1);
				rightMotor.set(1 * RobotMap.rampUpSpeed[1]);
			} else if (distanceBackwards
					- Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampDownDistance[2]) {
				// robot.drive(-1*RobotMap.rampDownSpeed[2], 0, 1);
				rightMotor.set(1 * RobotMap.rampDownSpeed[2]);
			} else if (distanceBackwards
					- Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampDownDistance[1]) {
				// robot.drive(-1*RobotMap.rampDownSpeed[1], 0, 1);
				rightMotor.set(1 * RobotMap.rampDownSpeed[1]);
			} else if (distanceBackwards
					- Math.abs(encoderDIORight.getDistance() / 120) < RobotMap.rampDownDistance[0]) {
				// robot.drive(-1*RobotMap.rampDownSpeed[0], 0, 1);
				rightMotor.set(1 * RobotMap.rampDownSpeed[0]);
			} else {
				// robot.drive(-1*RobotMap.rampUpSpeed[2], 0, 1);
				rightMotor.set(1 * RobotMap.rampUpSpeed[2]);
			}

			RPMs();
			return false;
			// }while(Math.abs(encoderDIOLeft.getDistance()/120)<distanceForward||Math.abs(encoderDIORight.getDistance()/120)<distanceForward);
		} else {
			return true;
		}

		// robot.stop();
	}

	public static void zeroTurret() {
		while (Math.abs(encoder.getPosition()) > 500) {
			if (encoder.getPosition() > 0) {
				if (encoder.getPosition() > 40000) {
					// turret.set(-.5);
				} else if (encoder.getPosition() > 10000) {
					// turret.set(-.25);
				} else {
					// turret.set(-.15);
				}
			} else {
				if (encoder.getPosition() < -40000) {
					// turret.set(-.5);
				} else if (encoder.getPosition() < -10000) {
					// turret.set(-.25);
				} else {
					// turret.set(-.15);
				}
			}
		}
		// turret.set(0);
	}

	public static boolean checkLimits() {
		// TODO determine values for the limits
		if (Math.abs(encoder.getPosition()) > 1000000) {
			return true;
		} else {
			return false;
		}
	}

}
