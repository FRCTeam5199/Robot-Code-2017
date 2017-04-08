package org.usfirst.frc.team5199.robot;

import java.nio.ByteBuffer;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Pixy {

	private I2C pixyBus;
	private short currentWord;
	private short lastWord;

	private static final byte[] SetRed  = {0,(byte)0xFD,(byte)0xFF,0,         0}; // 5 elements
	private static final byte[] SetBlue = {0,(byte)0xFD,         0,0,(byte)0xFF}; // 5 elements
	private static final byte[] SetPurple = {0,(byte)0xFD,(byte)0xCF,0,(byte)0xCF};
	public Pixy(int PIXY_ADDRESS) {
		
		if (PIXY_ADDRESS == 83) {
			pixyBus = new I2C(I2C.Port.kMXP, PIXY_ADDRESS);
			pixyBus.writeBulk(SetPurple);
			SmartDashboard.putString("Pixy Init1", "good: Red");
		} else if (PIXY_ADDRESS == 81) {
			pixyBus = new I2C(I2C.Port.kOnboard, PIXY_ADDRESS);
			pixyBus.writeBulk(SetPurple);
			SmartDashboard.putString("Pixy Init2", "good: Blue");
		}else{
			SmartDashboard.putString("Pixy Init1", "failed");
			SmartDashboard.putString("Pixy Init2", "failed");
		}
		currentWord = (short) 0x0000;
		lastWord = (short) 0x0000;
	}

	// public boolean isFrameUpdate() {
	// lastWord = currentWord;
	// currentWord = getWord();
	// if ((currentWord == (short) 0xAA55 || currentWord == (short) 0xAA56)
	// && (lastWord == (short) 0xAA55 || lastWord == (short) 0xAA56)) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	// TODO keep these commented out do not delete
	public byte getByte() {
		byte[] buffer = new byte[1];
		pixyBus.readOnly(buffer, 1);
		return buffer[0];
	}

	public byte[] getVariableSizeBuffer(int sizeOfBuffer) {
		byte[] buffer = new byte[sizeOfBuffer];
		pixyBus.readOnly(buffer, sizeOfBuffer);
		return buffer;
	}

	public void soutBlockData(SyncedLongBlock block, int i) {
		SmartDashboard.putNumber("Checksum " + i + ":", block.getChecksum(i));
		SmartDashboard.putNumber("Signature" + i + ":", block.getSignature(i));
		SmartDashboard.putNumber("X" + i + ":", block.getX(i));
		SmartDashboard.putNumber("Y" + i + ":", block.getY(i));
		SmartDashboard.putNumber("Width" + i + ":", block.getWidth(i));
		SmartDashboard.putNumber("Height" + i + ":", block.getHeight(i));
	}

	public int getStartOfData() {
		// The PixyCam returns data every video frame. The data starts with an
		// extended sync
		// followed by 12 bytes of block data, then a standard sync, followed by
		// 12 more bytes and
		// so on.
		// At the start of the block data for each frame, the PixyCam returns
		// 0xAA55 in little
		// endian order (i.e. 0x55, then 0xAA). The block data immediately
		// follows this.
		// Each block is marked with one of two specific start words:
		// 0xAA55 = Normal Block (Word[0]=0x55, Word[1]=0xAA)
		// 0xAA56 = Color Code Object (Word[0]=0x56, Word[1]=0xAA)
		//
		// The purpose of this routine is to identify the start marker for each
		// video frame. This
		// means that we are looking for the following byte sequences:
		// 0x55, then 0xAA, then 0x55, then 0xAA for a normal block.
		// 0x55, then 0xAA, then 0x56, then 0xAA for a color code block.
		// The expectation is that the block data will be read in a different
		// routine for processing.

		byte[] word1 = new byte[2];
		byte[] tmp = new byte[1];

		word1 = getVariableSizeBuffer(2);

		// Check to see if any data was returned.
		if ((word1[0] == (byte) 0x00) && (word1[1] == (byte) 0x00))
			return 0; // In I2C, this means no data so return immediately.

		// Check to see if we found a sync marker
		if ((word1[0] == (byte) 0x55) && (word1[1] == (byte) 0xAA)) {
			// By reaching this point, we have found a sync word. Now we need to
			// look for the
			// next to see if it is the start of a block.
			word1 = getVariableSizeBuffer(2);
			if ((word1[0] == (byte) 0x55) && (word1[1] == (byte) 0xAA))
				return 1; // Found normal block.

			if ((word1[0] == (byte) 0x56) && (word1[1] == (byte) 0xAA))
				return 2; // Found a color code block

			return 0; // We did not find the start of a frame's block data.
		}

		// the rest of the routine searches for special cases where the sync
		// word might be offset...
		if ((word1[0] == (byte) 0x00) && (word1[1] == (byte) 0x55)) {
			// Check to see if this could be an offset sync word by looking at
			// the next byte.
			tmp = getVariableSizeBuffer(1);
			if (tmp[0] != (byte) 0xAA)
				return 0;// Nope.

			// By reaching this point, we have found a sync word. Now we need to
			// look for the
			// next to see if it is the start of a block.
			word1 = getVariableSizeBuffer(2);
			if ((word1[0] == (byte) 0x55) && (word1[1] == (byte) 0xAA))
				return 1; // Found normal block.

			if ((word1[0] == (byte) 0x56) && (word1[1] == (byte) 0xAA))
				return 2; // Found a color code block

			return 0; // We did not find the start of a frame's block data.

		}

		if ((word1[0] == (byte) 0xAA) && (word1[1] == (byte) 0x55)) {
			// Check to see if this could be an offset sync word by looking at
			// the next byte.
			tmp = getVariableSizeBuffer(1);
			if (tmp[0] == (byte) 0xAA)
				return 1;// Found a normal block
			else
				return 0;// Nope
		}

		if ((word1[0] == (byte) 0xAA) && (word1[1] == (byte) 0x56)) {
			// Check to see if this could be an offset sync word by looking at
			// the next byte.
			tmp = getVariableSizeBuffer(1);
			if (tmp[0] == (byte) 0xAA)
				return 2;// Found a Color code block
			else
				return 0;// Nope
		}

		return 0;// Nope
	}
}
