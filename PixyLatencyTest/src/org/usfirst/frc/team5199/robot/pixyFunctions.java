package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class pixyFunctions {
	private static final int debugLevel = 1; //edit dependeding on future needs
	public static NewPixy pixyCam;
	public static final int pixyBuffer =5;
	public static short[] averageDataValueArrayX;
	public static int sumOfBufferX =0;
	public static short[] averageDataValueArrayY;
	public static int sumOfBufferY =0;
	public static int counter =0;
	public static boolean firstRun = true;
	public pixyFunctions(NewPixy pixy){
		pixyCam = pixy; 
		averageDataValueArrayX = new short[pixyBuffer];
		for(int i =0; i<averageDataValueArrayX.length; i++){
			averageDataValueArrayX[i] =(short) 0x0000;
		}
		averageDataValueArrayY = new short[pixyBuffer];
		for(int i =0; i<averageDataValueArrayY.length; i++){
			averageDataValueArrayY[i] =(short) 0x0000;
		}
		
		
	}
	public static void showAvgX(){
		byte[] syncedBufferWithoutSync = new byte[26];
		syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(26);
		if((syncedBufferWithoutSync[12])==85 &&(syncedBufferWithoutSync[13]==-86))//Looking 0x55 and 0xaa
		{
		SyncedLongBlock block = new SyncedLongBlock(syncedBufferWithoutSync);
		SmartDashboard.putNumber("X Pos 1", block.getX(0));
		SmartDashboard.putNumber("X Pos 2", block.getX(1));
		}
		
	}
	public static int[] averageData(int mode){
		//mode = 0: return average x value
		//mode = 1: return average x and average y
		int result[] ={0,0};
		
		if (pixyCam.getStartOfData() == 1) {

			short sig;
			int avgX, avgY;
			byte[] syncedBufferWithoutSync = new byte[26];
			syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(26);
			if(debugLevel==2){
				SmartDashboard.putNumber("Buffer 12", syncedBufferWithoutSync[12]);
				SmartDashboard.putNumber("Buffer 13", syncedBufferWithoutSync[13]);
			}
			if((syncedBufferWithoutSync[12])==85 &&(syncedBufferWithoutSync[13]==-86))//Looking 0x55 and 0xaa
	{
				
				SyncedLongBlock block = new SyncedLongBlock(syncedBufferWithoutSync);
				
				if(debugLevel>0){
					if(debugLevel==2){
							SmartDashboard.putNumber("X Pos 1", block.getX(0));
							SmartDashboard.putNumber("X Pos 2", block.getX(1));
					}
					SmartDashboard.putNumber("X Pos Avg", block.getAvgX());
				}
				
								avgX = block.getAvgX();
				if(mode==1){    avgY = block.getAvgY();}
				
								sumOfBufferX += block.getAvgX() -averageDataValueArrayX[counter];
				if (mode==1){ 	sumOfBufferY += block.getAvgY() -averageDataValueArrayY[counter];}
				
				
								averageDataValueArrayX[counter] = block.getAvgX();
				if (mode==1){ 	averageDataValueArrayY[counter] = block.getAvgY();}
				
				counter++;
				SmartDashboard.putNumber("Counter", counter);
				
				if (counter == pixyBuffer) {
					firstRun = false;
					counter = 0;
				}

				if (firstRun) {
									result[0] = sumOfBufferX / counter;
					if (mode==1){ 	result[1] = sumOfBufferY / counter;}

				} else {
									result[0] = sumOfBufferX / pixyBuffer;
				    if (mode==1){ 	result[1] = sumOfBufferY / pixyBuffer;}
				}
				
			}else{
				return result;
			}
			
	}
		return result;
		
	}
	
}

	

