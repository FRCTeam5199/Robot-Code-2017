package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PixyProcess {
	private static final int debugLevel = 1; // edit depending on future needs
	public static Pixy pixyCam; 
	public static Pixy gearPixy = new Pixy(0x51);  
	public static Pixy shooterPixy = new Pixy(0x53);
	public static final int pixyBuffer = 5;// size of the ring buffer for
											// averaging
	public static int loops = 0;
	public static int blockCount = 0;
	public static int gearByteCount= 0;
	public static int gearBlockCount = 0;
	public static int shooterBlockCount = 0;
	public static int shooterByteCount = 0;
	public static double timeStart = 0;
	public static double elapsedTime = 0;
	public static int byteCount=0;
	public static double oldAverage=0;
	public static double oldAverageShooter = 0;
	public static double oldX1 =0;
	public static double oldX2=0;
	public static double[] gearData= {-1,-1,-1};
	public static double[] result = { -1, -1, -1};
	public static double[] shooterResult = { -1, -1, -1};
	public static CircularAverageBuffer pixyGearDataX, pixyGearDataBetweenBlocks, pixyShooterDataX, pixyShooterDataY;
	public PixyProcess(Pixy pixy) {
		pixyCam = pixy;
		pixyGearDataX = new CircularAverageBuffer(pixyBuffer);
		pixyGearDataBetweenBlocks = new CircularAverageBuffer(pixyBuffer);
		pixyShooterDataX = new CircularAverageBuffer(pixyBuffer);
		pixyShooterDataY= new CircularAverageBuffer(pixyBuffer);
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
			SmartDashboard.putString("Pixy Data", "Reading");
		}
		else{
			SmartDashboard.putString("Pixy Data", "Failed");
		}
		SmartDashboard.putNumber("PixyBlocks", blockCount);
		SmartDashboard.putNumber("Loops per second", loops / elapsedTime);
		SmartDashboard.putNumber("PixyBlocks per second", blockCount / elapsedTime);
		SmartDashboard.putNumber("Avg X", averageData(0, false, pixyCam)[0] );
		SmartDashboard.putNumber("Distance off", averageData(0, false,pixyCam)[0] - 160);
		
	}
	public static void pixyGearTest() {
		loops++;
		if (timeStart == 0) {
			timeStart = System.currentTimeMillis();
		}
		elapsedTime = (System.currentTimeMillis() - timeStart) / 1000;
		SmartDashboard.putNumber("Elapsed Time", elapsedTime);
		SmartDashboard.putNumber("Loops", loops);
		if (gearPixy.getStartOfData() == 1) {
			gearBlockCount++;
			SmartDashboard.putString("Gear Pixy Data", "Reading");
		}
		else{
			SmartDashboard.putString("Gear Pixy Data", "Failed");
		}
		SmartDashboard.putNumber("Gear PixyBlocks", gearBlockCount);
		SmartDashboard.putNumber("Gear Loops per second", loops / elapsedTime);
		SmartDashboard.putNumber("Gear PixyBlocks per second", gearBlockCount / elapsedTime);
		SmartDashboard.putNumber("Gear Avg X", averageData(0, false, gearPixy)[0] );
		SmartDashboard.putNumber("Gear Distance off", averageData(0, false,gearPixy)[0] - 160);
		
	}
	
	public static void pixyShooterTest() {
		loops++;
		if (timeStart == 0) {
			timeStart = System.currentTimeMillis();
		}
		elapsedTime = (System.currentTimeMillis() - timeStart) / 1000;
		SmartDashboard.putNumber("Elapsed Time", elapsedTime);
		SmartDashboard.putNumber("Loops", loops);
		if (shooterPixy.getStartOfData() == 1) {
			shooterBlockCount++;
			SmartDashboard.putString("Shooter Pixy Data", "Reading");
		}
		else{
			SmartDashboard.putString("Shooter Pixy Data", "Failed");
		}
		SmartDashboard.putNumber("Shooter PixyBlocks", shooterBlockCount);
		SmartDashboard.putNumber("Shooter Loops per second", loops / elapsedTime);
		SmartDashboard.putNumber("Shooter PixyBlocks per second", shooterBlockCount / elapsedTime);
		SmartDashboard.putNumber("Shooter Avg X", shooterData()[0] );
		SmartDashboard.putNumber("Shooter Distance off",shooterData()[0] - 160);
		
	}

	public static void pixyI2CTest() {
		if (byteCount==0)
		{
			SmartDashboard.putString("I2C Test  Status", "I2C not receiving data, check address");
			SmartDashboard.putString("No Data", "WE NEED DATA");
			SmartDashboard.putString("ACTUALLY WORKS", "NO NO NO");
			
		}
		if(pixyCam.getByte()>0){
			byteCount++;
			SmartDashboard.putNumber("I2C Test Byte Count", byteCount);
			SmartDashboard.putString("I2C Test  Status", "I2C  receiving data");
			SmartDashboard.putString("ACTUALLY WORKS", "YES YES YES");

		}
		
	}
	
	public static void pixyGearI2CTest() {
		if (gearByteCount==0)
		{
			SmartDashboard.putString("I2C Test Gear Status", "I2C not receiving data, check address");
			SmartDashboard.putString("GEAR ACTUALLY WORKS", "NO NO NO");
			
		}
		if(gearPixy.getByte()>0){
			gearByteCount++;
			SmartDashboard.putNumber("I2C Test Gear Byte Count", byteCount);
			SmartDashboard.putString("I2C Test Gear Status", "I2C  receiving data");
			SmartDashboard.putString("GEAR ACTUALLY WORKS", "YES YES YES");

		}
		
		
	}
	
	public static void pixyShooterI2CTest() {
		if (shooterByteCount==0)
		{
			SmartDashboard.putString("I2C Test Shooter Status", "I2C not receiving data, check address");
			SmartDashboard.putString("Shooter ACTUALLY WORKS", "NO NO NO");
			
		}
		if(shooterPixy.getByte()>0){
			shooterByteCount++;
			SmartDashboard.putNumber("I2C Test Shooter Byte Count", byteCount);
			SmartDashboard.putString("I2C Test Shooter Status", "I2C  receiving data");
			SmartDashboard.putString("Shooter ACTUALLY WORKS", "YES YES YES");

		}
		
		
	}

	public static void pixyTestReset() {
		loops = 0;
		timeStart = 0;
		elapsedTime = 0;
		blockCount = 0;
		byteCount =0;
		gearByteCount = 0;
		shooterByteCount = 0;
		shooterBlockCount = 0;
		gearBlockCount = 0;
	}
	public static double compensatedGearPixyData(){
		//The pixycam for the gear is offset, so it the value needs to be compensated
		//This gets the average value in the x direction and the difference between
		//the right and left blocks to compensate.
		//The difference between is multiplied by the distance they are apart in inches
		//and the value is divided by the number of inches away the pixy cam is from the center
		//This is to convert the inches offset into pixels to compensate.
		//On our robot, we have the line as: normal
		
		//for blue blur
//		return pixyValues[0]+((8*(pixyValues[2]/15)));	
		double[] pixyValues  =averageData(2, false, gearPixy);

		return pixyValues[0]+((8.5*(pixyValues[2]/8.25)));	
	}
	
	public static double[] ShooterPixyData(){
		return averageData(1,false, pixyCam);
	}
	public static double[] averageData(int mode, boolean displayResults, Pixy pixyCam) {
		// mode = 0: return average x value
		// mode = 1: return average x and average y
		// mode = 2; return average x value, and average distance between x
		

		if (gearPixy.getStartOfData() == 1) {

			short sig;
			int avgX, avgY;
			byte[] syncedBufferWithoutSync = new byte[26];
			syncedBufferWithoutSync = gearPixy.getVariableSizeBuffer(26);
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
				result[0] = pixyGearDataX.DataAverage(avgX);
				result[2] = pixyGearDataBetweenBlocks.DataAverage(Math.abs(block.getX(0)-block.getX(1)));
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
		
		if(displayResults){
			SmartDashboard.putNumber("Avg X", result[0]);
			SmartDashboard.putNumber("PixyBlocks", blockCount);
			SmartDashboard.putNumber("Loops per second", loops / elapsedTime);
			SmartDashboard.putNumber("PixyBlocks per second", blockCount / elapsedTime);
		}
		return result;

	}
	public static double[] shooterData() {
		if (shooterPixy.getStartOfData() == 1) {
			short sig;
			int avgX, avgY;
			byte[] syncedBufferWithoutSync = new byte[14];
			syncedBufferWithoutSync = shooterPixy.getVariableSizeBuffer(14);
			SyncedLongBlock block = new SyncedLongBlock(syncedBufferWithoutSync);
			result[0]= pixyShooterDataX.DataAverage(block.getX(0));
			result [1] = pixyShooterDataY.DataAverage(block.getY(0));
;		}
		return shooterResult;

	}

}
