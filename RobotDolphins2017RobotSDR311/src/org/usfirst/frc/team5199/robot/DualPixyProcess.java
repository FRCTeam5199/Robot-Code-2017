package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DualPixyProcess {
	private static final int debugLevel = 1; // edit depending on future needs
	public static Pixy pixyGear,pixyShooter;
	public static final int pixyBuffer = 5;// size of the ring buffer for
											// averaging
	public static short[] averageDataValueArrayX;
	public static short[] averageDataValueArrayY;
	public static short[] averageDataValueArrayBetweenX;
	public static int sumOfBufferX = 0;
	public static int sumOfBufferY = 0;
	public static int sumOfBufferBetweenX = 0;
	public static int counter = 0;
	public static boolean firstRun = true;
	public static int loops = 0;
	public static int blockCount = 0;
	public static int gearBlockCount = 0;
	public static int shooterBlockCount =0;
	public static double timeStart = 0;
	public static double elapsedTime = 0;
	public static int byteCount = 0;
	public static int gearByteCount = 0;
	public static int shooterByteCount =0;
	public static double oldAverage = 0;
	public static double oldAverageShooter = 0;
	public static double oldX1 = 0;
	public static double oldX2 = 0;
	public static double[] gearData = { -1, -1, -1 };
	public static double[] result = { -1, -1, -1 };

	public DualPixyProcess() {
		pixyShooter = new Pixy(0x53);
		pixyGear = new Pixy(0x51);
		averageDataValueArrayX = new short[pixyBuffer];
		for (int i = 0; i < averageDataValueArrayX.length; i++) {
			averageDataValueArrayX[i] = (short) 0x0000;
		}
		averageDataValueArrayY = new short[pixyBuffer];
		for (int i = 0; i < averageDataValueArrayY.length; i++) {
			averageDataValueArrayY[i] = (short) 0x0000;
		}
		averageDataValueArrayBetweenX = new short[pixyBuffer];
		for (int i = 0; i < averageDataValueArrayBetweenX.length; i++) {
			averageDataValueArrayBetweenX[i] = (short) 0x0000;
		}

	}

	public static void pixyTest(Pixy pixyCam) {
		loops++;
		if (timeStart == 0) {
			timeStart = System.currentTimeMillis();
		}
		elapsedTime = (System.currentTimeMillis() - timeStart) / 1000;
		SmartDashboard.putNumber("Elapsed Time", elapsedTime);
		SmartDashboard.putNumber("Loops", loops);
		if (pixyGear.getStartOfData() == 1) {
			blockCount++;
			SmartDashboard.putString("Pixy Data", "Reading");
		} else {
			SmartDashboard.putString("Pixy Data", "Failed");
		}
		SmartDashboard.putNumber("PixyBlocks" + pixyCam.address, blockCount);
		SmartDashboard.putNumber("Loops per second"+ pixyCam.address, loops / elapsedTime);
		SmartDashboard.putNumber("PixyBlocks per second"+ pixyCam.address, blockCount / elapsedTime);
		SmartDashboard.putNumber("Avg X"+ pixyCam.address, averageData(0, false, pixyCam)[0]);
		SmartDashboard.putNumber("Distance off"+ pixyCam.address, averageData(0, false, pixyCam)[0] - 160);

	}
	
	public static void doublePixyTest() {
		loops++;
		if (timeStart == 0) {
			timeStart = System.currentTimeMillis();
		}
		elapsedTime = (System.currentTimeMillis() - timeStart) / 1000;
		SmartDashboard.putNumber("Elapsed Time", elapsedTime);
		SmartDashboard.putNumber("Loops", loops);
		if (pixyGear.getStartOfData() == 1) {
			gearBlockCount++;
			SmartDashboard.putString("Pixy Gear Data", "Reading");
		} else {
			SmartDashboard.putString("Pixy Gear Data", "Failed");
		}
		if (pixyShooter.getStartOfData() == 1) {
			shooterBlockCount++;
			SmartDashboard.putString("Pixy Shooter Data", "Reading");
		} else {
			SmartDashboard.putString("Pixy Shooter Data", "Failed");
		}
		SmartDashboard.putNumber("PixyBlocks Gear", gearBlockCount);
		SmartDashboard.putNumber("Loops per second", loops / elapsedTime);
		SmartDashboard.putNumber("PixyBlocks per second Gear", gearBlockCount / elapsedTime);
		SmartDashboard.putNumber("PixyBlocks Shooter" , shooterBlockCount);
		SmartDashboard.putNumber("PixyBlocks per second Shooter", shooterBlockCount / elapsedTime);
//		SmartDashboard.putNumber("Avg X", averageData(0, false)[0]);
//		SmartDashboard.putNumber("Distance off", averageData(0, false)[0] - 160);

	}

	public static void pixyI2CTest() {
		if (gearByteCount == 0) {
			SmartDashboard.putString("I2C Test Status Gear" , "I2C not receiving data, check address");
			SmartDashboard.putString("No Data", "WE NEED DATA");
		}
		if (pixyGear.getByte() > 0) {
			gearByteCount++;
			SmartDashboard.putNumber("I2C Test Byte Count Gear", byteCount);
			SmartDashboard.putString("I2C Test Status Gear" , "I2C  receiving data");
		}
		if (shooterByteCount == 0) {
			SmartDashboard.putString("I2C Test Status Shooter" , "I2C not receiving data, check address");
			SmartDashboard.putString("No Data", "WE NEED DATA");
		}
		if (pixyShooter.getByte() > 0) {
			shooterByteCount++;
			SmartDashboard.putNumber("I2C Test Byte Count Shooter" , shooterByteCount);
			SmartDashboard.putString("I2C Test Status Shooter", "I2C  receiving data");
		}

	}

	public static void pixyTestReset() {
		loops = 0;
		timeStart = 0;
		elapsedTime = 0;
		blockCount = 0;
		byteCount = 0;
		shooterByteCount = 0;
		gearByteCount = 0;
	}

	public static double compensatedGearPixyData() {
		// The pixycam for the gear is offset, so it the value needs to be
		// compensated
		// This gets the average value in the x direction and the difference
		// between
		// the right and left blocks to compensate.
		// The difference between is multiplied by the distance they are apart
		// in inches
		// and the value is divided by the number of inches away the pixy cam is
		// from the center
		// This is to convert the inches offset into pixels to compensate.
		// On our robot, we have the line as:
		double[] pixyValues = averageData(2, false, pixyGear);
		return pixyValues[0] + ((8.5 * (pixyValues[2] / 8.75)));
		// For the blue blur
		// return pixyValues[0]+((8*(pixyValues[2]/15)));
	}

	public static double[] ShooterPixyData() {
		return averageData(1, false, pixyShooter);
	}

	public static double xPosShooter() {
		if (pixyGear.getStartOfData() == 1) {
			byte[] syncedBufferWithoutSync = new byte[26];
			syncedBufferWithoutSync = pixyGear.getVariableSizeBuffer(26);

			if ((syncedBufferWithoutSync[12]) == 85 && (syncedBufferWithoutSync[13] == -86))// Looking
																							// 0x55
																							// and
																							// 0xaa
			{
				SyncedLongBlock block = new SyncedLongBlock(syncedBufferWithoutSync);
				oldAverageShooter = block.getAvgX();
				return oldAverageShooter;
			}
		}
		return oldAverageShooter;
	}

	public static double[] averageData(int mode, boolean displayResults, Pixy pixyCam) {
		// mode = 0: return average x value
		// mode = 1: return average x and average y
		// mode = 2; return average x value, and average distance between x

		if (pixyCam.getStartOfData() == 1) {

			short sig;
			int avgX, avgY;
			byte[] syncedBufferWithoutSync = new byte[26];
			syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(26);
			if (debugLevel == 2) {
				SmartDashboard.putNumber("Buffer 12", syncedBufferWithoutSync[12]);
				SmartDashboard.putNumber("Buffer 13", syncedBufferWithoutSync[13]);
			}
			if ((syncedBufferWithoutSync[12]) == 85 && (syncedBufferWithoutSync[13] == -86))// Looking
																							// 0x55
																							// and
																							// 0xaa
			{

				SyncedLongBlock block = new SyncedLongBlock(syncedBufferWithoutSync);

				if (debugLevel > 0) {
					if (debugLevel == 2) {
						SmartDashboard.putNumber("X Pos 1", block.getX(0));
						SmartDashboard.putNumber("X Pos 2", block.getX(1));
					}
					SmartDashboard.putNumber("X Pos Avg", block.getAvgX());
				}

				avgX = block.getAvgX();
				if (mode == 1) {
					avgY = block.getAvgY();
				}

				sumOfBufferX += block.getAvgX() - averageDataValueArrayX[counter];
				if (mode == 1) {
					sumOfBufferY += block.getAvgY() - averageDataValueArrayY[counter];
				}
				if (mode == 2) {
					sumOfBufferBetweenX += (Math.abs(block.getX(0) - block.getX(1)))
							- averageDataValueArrayBetweenX[counter];
				}

				averageDataValueArrayX[counter] = block.getAvgX();
				if (mode == 1) {
					averageDataValueArrayY[counter] = block.getAvgY();
				}
				if (mode == 2) {
					averageDataValueArrayBetweenX[counter] = (short) Math.abs(block.getX(0) - block.getX(1));
				}
				counter++;
				SmartDashboard.putNumber("Counter", counter);

				if (counter == pixyBuffer) {
					firstRun = false;
					counter = 0;
				}

				if (firstRun) {
					result[0] = (sumOfBufferX / counter);
					if (mode == 1) {
						result[1] = (sumOfBufferY / counter);
					}
					if (mode == 2) {
						result[2] = (sumOfBufferBetweenX / counter);
					}
				} else {
					result[0] = (sumOfBufferX / pixyBuffer);
					if (mode == 1) {
						result[1] = (sumOfBufferY / pixyBuffer);
					}
					if (mode == 2) {
						result[2] = (sumOfBufferBetweenX / pixyBuffer);
					}
				}

			} else {
				if (displayResults) {
					SmartDashboard.putNumber("Avg X", result[0]);
					SmartDashboard.putNumber("PixyBlocks", blockCount);
					SmartDashboard.putNumber("Loops per second", loops / elapsedTime);
					SmartDashboard.putNumber("PixyBlocks per second + ", blockCount / elapsedTime);
				}
				return result;
			}

		}

		if (displayResults) {
			SmartDashboard.putNumber("Avg X", result[0]);
			SmartDashboard.putNumber("PixyBlocks", blockCount);
			SmartDashboard.putNumber("Loops per second", loops / elapsedTime);
			SmartDashboard.putNumber("PixyBlocks per second", blockCount / elapsedTime);
		}
		return result;
	}

	public static double[] shooterData() {
		if (pixyShooter.getStartOfData() == 1) {

			short sig;
			int avgX, avgY;
			byte[] syncedBufferWithoutSync = new byte[14];
			syncedBufferWithoutSync = pixyShooter.getVariableSizeBuffer(14);
			SyncedLongBlock block = new SyncedLongBlock(syncedBufferWithoutSync);
			sumOfBufferX += block.getX(0) - averageDataValueArrayX[counter];
			averageDataValueArrayX[counter] = block.getX(0);
			counter++;
			SmartDashboard.putNumber("Counter", counter);

			if (counter == pixyBuffer) {
				firstRun = false;
				counter = 0;
			}
			if (firstRun) {
				result[0] = (sumOfBufferX / counter);
			} else {
				result[0] = (sumOfBufferX / pixyBuffer);
			}

		}
		return result;

	}

}
