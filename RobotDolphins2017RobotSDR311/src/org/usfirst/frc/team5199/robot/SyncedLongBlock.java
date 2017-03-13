package org.usfirst.frc.team5199.robot;

public class SyncedLongBlock {

	// The size of two Blocks with an extra two bytes.
	static final short sizeOfSyncedLongBlock = 26;
	byte[] rawData = new byte[sizeOfSyncedLongBlock];
	
	// CONSTRUCTOR METHODS -----------------------------------------------
	public SyncedLongBlock(byte[] byteBuffer) {
		rawData = byteBuffer;
	}
	
	// UTILITY METOHDS ---------------------------------------------------
	public static short byte2Unsigned(byte b1, byte b2) {
		return (short) ((b2 << 8) | (b1 & (short)(0x00FF)));
	}
	
	public static String getHexRepresentation(byte b1, byte b2) {
		return String.format("%02X", b2) + " " + String.format("%02X", b1);
	}
	
	// CHECKSUM METHODS ---------------------------------------------------
	public short getChecksum(int blockNumber) {
		if(blockNumber == 0) {
			return (short) byte2Unsigned(rawData[0],rawData[1]);
		} else {
			// Hardcoded indexes for speed.
			return (short) byte2Unsigned(rawData[14],rawData[15]);
		}
	}
	
	public short getSum(int blockNumber) {
		return (short)(getSignature(blockNumber) + 
					   getX(blockNumber) + 
					   getY(blockNumber) + 
					   getWidth(blockNumber) + 
					   getHeight(blockNumber));
	}
	
	// SIGNATURE METHODS --------------------------------------------------
	public short getSignature(int blockNumber) {
		if(blockNumber == 0) {
			return (short) byte2Unsigned(rawData[2],rawData[3]);
		} else {
			return (short) byte2Unsigned(rawData[16],rawData[17]);
		}
	}
	
	
	// X VALUE METHODS ----------------------------------------------------
	public short getX(int blockNumber) {
		if(blockNumber == 0) {
			return (short) byte2Unsigned(rawData[4],rawData[5]);
		} else {
			return (short) byte2Unsigned(rawData[18],rawData[19]);
		}
	}
	
	public short getAvgX() {
		//returns the average x value of the two blocks. 
		if((getChecksum(0)==getSum(0))&&(getChecksum(1)==getSum(1))&&(getX(1)!=0)){
			return (short) ((getX(0) + getX(1)) / 2);
		}else{
			return 0;
		}
	}
	
	// Y VALUE METHODS ----------------------------------------------------
	public short getY(int blockNumber) {
		if(blockNumber == 0) {
			return (short) byte2Unsigned(rawData[6],rawData[7]);
		} else {
			return (short) byte2Unsigned(rawData[20],rawData[21]);
		}
	}
	
	public short getAvgY() {
		//returns the average y value of the two blocks. 
		if((getChecksum(0)==getSum(0))&&(getChecksum(1)==getSum(1))&&(getY(1)!=0)){
				return (short) ((getY(0) + getY(1)) / 2);
			}else{
				return 0;
			}
	}
	
	// WIDTH VALUE METHODS ------------------------------------------------
	public short getWidth(int blockNumber) {
		if(blockNumber == 0) {
			return (short) byte2Unsigned(rawData[8],rawData[9]);
		} else {
			return (short) byte2Unsigned(rawData[22],rawData[23]);
		}
	}
	
	
	// HEIGHT VALUE METHODS -----------------------------------------------
	public short getHeight(int blockNumber) {
		if(blockNumber == 0) {
			return (short) byte2Unsigned(rawData[10],rawData[11]);
		} else {
			return (short) byte2Unsigned(rawData[24],rawData[25]);
		}
	}
	
}
