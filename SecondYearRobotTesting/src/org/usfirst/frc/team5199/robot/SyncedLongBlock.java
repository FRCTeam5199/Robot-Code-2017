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
			return (short) ((rawData[1] << 8) | (rawData[0]));
		} else {
			// Hardcoded indexes for speed.
			return (short) ((rawData[15] << 8) | (rawData[14]));
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
			return (short) ((rawData[3] << 8) | (rawData[2]));
		} else {
			return (short) ((rawData[17] << 8) | (rawData[16])); 
		}
	}
	
	
	// X VALUE METHODS ----------------------------------------------------
	public short getX(int blockNumber) {
		if(blockNumber == 0) {
			return (short) ((rawData[5] << 8) | (rawData[4]));
		} else {
			return (short) ((rawData[19] << 8) | (rawData[18])); 
		}
	}
	
	public short getAvgX() {
		if((getChecksum(0)==getSum(0))&&(getChecksum(1)==getSum(1))&&(getX(1)!=0)){
			return (short) ((getX(0) + getX(1)) / 2);
		}else{
			return 0;
		}
	}
	
	// Y VALUE METHODS ----------------------------------------------------
	public short getY(int blockNumber) {
		if(blockNumber == 0) {
			return (short) ((rawData[7] << 8) | (rawData[6]));
		} else {
			return (short) ((rawData[21] << 8) | (rawData[20]));
		}
	}
	
	public short getAvgY() {
		if((getChecksum(0)==getSum(0))&&(getChecksum(1)==getSum(1))&&(getY(1)!=0)){
				return (short) ((getY(0) + getY(1)) / 2);
			}else{
				return 0;
			}
	}
	
	// WIDTH VALUE METHODS ------------------------------------------------
	public short getWidth(int blockNumber) {
		if(blockNumber == 0) {
			return (short) ((rawData[9] << 8) | (rawData[8]));
		} else {
			return (short) ((rawData[23] << 8) | (rawData[22]));
		}
	}
	
	public short getAvgWidth() {
		return (short) ((getWidth(0) + getWidth(1)) / 2);
	}
	
	// HEIGHT VALUE METHODS -----------------------------------------------
	public short getHeight(int blockNumber) {
		if(blockNumber == 0) {
			return (short) ((rawData[11] << 8) | (rawData[10]));
		} else {
			return (short) ((rawData[25] << 8) | (rawData[24]));
		}
	}
	
	public short getAvgHeight() {
		return (short) ((getHeight(0) + getHeight(1)) / 2);
	}
	
}
