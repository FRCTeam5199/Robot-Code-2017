package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PixyProcess {
	private static final int debugLevel = 1; // edit depending on future needs
	public static Pixy pixyCam;
	public static final int pixyBuffer = 5;// size of the ring buffer for
											// averaging
	public static short[] averageDataValueArrayX;
	public static short[] averageDataValueArrayY;
	public static int sumOfBufferX = 0;
	public static int sumOfBufferY = 0;
	public static int counter = 0;
	public static boolean firstRun = true;
	public static int loops = 0;
	public static int blockCount = 0;
	public static double timeStart = 0;
	public static double elapsedTime = 0;
	public static int byteCount=0;
	public PixyProcess(Pixy pixy) {
		pixyCam = pixy;
		averageDataValueArrayX = new short[pixyBuffer];
		for (int i = 0; i < averageDataValueArrayX.length; i++) {
			averageDataValueArrayX[i] = (short) 0x0000;
		}
		averageDataValueArrayY = new short[pixyBuffer];
		for (int i = 0; i < averageDataValueArrayY.length; i++) {
			averageDataValueArrayY[i] = (short) 0x0000;
		}

	}

	public static void pixyTest() {
		loops++;
		if (timeStart == 0) {
			timeStart = System.currentTimeMillis();
		}
		elapsedTime = (System.currentTimeMillis() - timeStart) / 1000;
		SmartDashboard.putNumber("Elapsed Time", elapsedTime);
		SmartDashboard.putNumber("Loops", loops);
		if (pixyCam.getStartOfData() == 1) {
			blockCount++;

		}
		SmartDashboard.putNumber("PixyBlocks", blockCount);
		SmartDashboard.putNumber("Loops per second", loops / elapsedTime);
		SmartDashboard.putNumber("PixyBlocks per second", blockCount / elapsedTime);
		//SmartDashboard.putNumber("Avg X", averageData(0)[0]);
	}

	public static void pixyI2CTest() {
		if (byteCount==0)
		{
			SmartDashboard.putString("I2C Test  Status", "I2C not receiving data, check address");
		}
		if(pixyCam.getByte()>0){
			byteCount++;
			SmartDashboard.putNumber("I2C Test Byte Count", byteCount);
			SmartDashboard.putString("I2C Test  Status", "I2C  receiving data");
		}
		
		
	}

	public static void pixyTestReset() {
		loops = 0;
		timeStart = 0;
		elapsedTime = 0;
		blockCount = 0;
		byteCount =0;
	}
	public static double adjustDataGear(int pixels){
		//TODO put in exact measurement for inches off the pixy cam is current estimate is 9.5
		return pixels-((9.5*distanceBetweenRightAndLeft())/7.5);
		
	}
	public static int[] averageData(int mode, boolean displayResults) {
		// mode = 0: return average x value
		// mode = 1: return average x and average y
		int result[] = { -1, -1 };

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

				averageDataValueArrayX[counter] = block.getAvgX();
				if (mode == 1) {
					averageDataValueArrayY[counter] = block.getAvgY();
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

				} else {
					result[0] = (sumOfBufferX / pixyBuffer);
					if (mode == 1) {
						result[1] = (sumOfBufferY / pixyBuffer);
					}
				}

			} else {
				if(displayResults){
					SmartDashboard.putNumber("Avg X", result[0]);
					SmartDashboard.putNumber("PixyBlocks", blockCount);
					SmartDashboard.putNumber("Loops per second", loops / elapsedTime);
					SmartDashboard.putNumber("PixyBlocks per second", blockCount / elapsedTime);
				}
				return result;
			}

		}
		if(sumOfBufferX>0){
			result[0] = sumOfBufferX/pixyBuffer;
		}
		if(displayResults){
			SmartDashboard.putNumber("Avg X", result[0]);
			SmartDashboard.putNumber("PixyBlocks", blockCount);
			SmartDashboard.putNumber("Loops per second", loops / elapsedTime);
			SmartDashboard.putNumber("PixyBlocks per second", blockCount / elapsedTime);
		}
		return result;

	}
	public static int distanceBetweenRightAndLeft(){
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
				return Math.abs(block.getX(0)-block.getX(1));
			}
			}else{
				do{
					
				}while(pixyCam.getStartOfData() != 1);
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
					return Math.abs(block.getX(0)-block.getX(1));
				}
			}
		return 0;
	}
	

}
// SyncedBlock ourBlock1 = new SyncedBlock(syncedBufferWithoutSync);
// sig = ourBlock1.getSignature();
// SmartDashboard.putNumber(sig + " Synced Checksum:", ourBlock1.getChecksum());
// SmartDashboard.putString(sig + " Synced X:",
// String.valueOf(ourBlock1.getX()));
// SmartDashboard.putString(sig + " Synced Y:",
// String.valueOf(ourBlock1.getY()));
// SmartDashboard.putString(sig + " Synced Width:",
// String.valueOf(ourBlock1.getWidth()));
// SmartDashboard.putString(sig + " Synced height:",
// String.valueOf(ourBlock1.getHeight()));
//
// byte[] secondBlockSync = new byte[2];
// secondBlockSync = pixyCam.getVariableSizeBuffer(2);
// if ((secondBlockSync[0] == (byte) 0x55) && (secondBlockSync[1] == (byte)
// 0xAA)) {
// syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(12);
// SyncedBlock ourBlock2 = new SyncedBlock(syncedBufferWithoutSync);
// sig = ourBlock2.getSignature();
//
// SmartDashboard.putNumber(sig + " Synced Checksum:", ourBlock2.getChecksum());
// SmartDashboard.putString(sig + " Synced X:",
// String.valueOf(ourBlock2.getX()));
// SmartDashboard.putString(sig + " Synced Y:",
// String.valueOf(ourBlock2.getY()));
// SmartDashboard.putString(sig + " Synced Width:",
// String.valueOf(ourBlock2.getWidth()));
// SmartDashboard.putString(sig + " Synced height:",
// String.valueOf(ourBlock2.getHeight()));
// }
// }
