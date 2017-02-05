package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Pixy {

	private I2C pixyBus;
	private short currentWord;
	private short lastWord;

	public Pixy(int PIXY_ADDRESS) {
		pixyBus = new I2C(I2C.Port.kOnboard, PIXY_ADDRESS);
		currentWord = (short) 0x0000;
		lastWord = (short) 0x0000;
	}

//	public boolean isFrameUpdate() {
//		lastWord = currentWord;
//		currentWord = getWord();
//		if ((currentWord == (short) 0xAA55 || currentWord == (short) 0xAA56)
//				&& (lastWord == (short) 0xAA55 || lastWord == (short) 0xAA56)) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//TODO keep these commented out do not delete
	public byte getByte() {
		byte[] buffer = new byte[1];
		pixyBus.readOnly(buffer, 1);
		return buffer[0];
	}
//
//	public short getWord() {
//		byte[] buffer = new byte[2];
//		pixyBus.readOnly(buffer, 2);
//		return (short) ((buffer[1] << 8) | buffer[0]);
//	}

	public byte[] getVariableSizeBuffer(int sizeOfBuffer) {
		byte[] buffer = new byte[sizeOfBuffer];
		pixyBus.readOnly(buffer, sizeOfBuffer);
		return buffer;
	}

//	public Block getDataBlock() {
//		byte[] buffer = new byte[Block.sizeOfBlockObject];
//		pixyBus.readOnly(buffer, Block.sizeOfBlockObject);
//		Block theBlock = new Block(buffer);
//		return theBlock;
//	}
//
	public void soutBlockData(SyncedLongBlock block, int i) {
		SmartDashboard.putNumber("Checksum " + i  +":",  	block.getChecksum(i));
		SmartDashboard.putNumber("Signature" + i  +":",  block.getSignature(i));
		SmartDashboard.putNumber("X" + i  +":",  		block.getX(i));
		SmartDashboard.putNumber("Y" + i  +":",  		block.getY(i));
		SmartDashboard.putNumber("Width" + i  +":",  	block.getWidth(i));
		SmartDashboard.putNumber("Height" + i  +":",  	block.getHeight(i));
	}
//
//	public String printBlockData(Block block) {
//		return ("Sync: " + Block.shortAsHex(block.getSync()) + "\t")
//				+ ("Checksum: " + Block.shortAsHex(block.getChecksum()) + "\t")
//				+ ("Signature: " + Block.shortAsHex(block.getSignature()) + "\t") + ("X: " + block.getX() + "\t")
//				+ ("Y: " + block.getY() + "\t") + ("Width: " + Block.shortAsHex(block.getWidth()) + "\t")
//				+ ("Height: " + Block.shortAsHex(block.getHeight()) + "\t");
//	}
	


//	public int getStartOfData() {
//		
//		byte[] word = new byte[2];
//		byte[] lastWord = { (byte) 0xFF, (byte) 0xFF };
//		
//		while (true) {
//			word = getVariableSizeBuffer(2);
//			if        ((word[0] == (byte) 0x00) && (word[1] == (byte) 0x00) && (lastWord[0] == (byte) 0x00)
//					&& (lastWord[1] == (byte) 0x00)) {
//				// In I2C, this means no data so return immediately.
//				return 0;
//			} else if ((word[0] == (byte) 0x55) && (word[1] == (byte) 0xAA) && (lastWord[0] == (byte) 0x55)
//					&& (lastWord[1] == (byte) 0xAA)) {
//				// Found normal block.
//				return 1;
//			} else if ((word[0] == (byte) 0x56) && (word[1] == (byte) 0xAA) && (lastWord[0] == (byte) 0x55)
//					&& (lastWord[1] == (byte) 0xAA)) {
//				// Found color code block.
//				return 2;
//			} else if ((word[0] == (byte) 0xAA) && (word[1] == (byte) 0x55)) {
//				// This is important, we might be byte reversed, or at a sync.
//				getByte();
//			}
//			lastWord[0] = word[0];
//			lastWord[1] = word[1];
//		}
//	}
	public int getStartOfData() {
		// The PixyCam returns data every video frame.  The data starts with an extended sync 
		// followed by 12 bytes of block data, then a standard sync, followed by 12 more bytes and 
		// so on.  
		// At the start of the block data for each frame, the PixyCam returns 0xAA55 in little 
		// endian order (i.e. 0x55, then 0xAA).  The block data immediately follows this.  
		// Each block is marked with one of two specific start words:
		//     0xAA55 = Normal Block      (Word[0]=0x55, Word[1]=0xAA)
		//     0xAA56 = Color Code Object (Word[0]=0x56, Word[1]=0xAA)
		//
		//  The purpose of this routine is to identify the start marker for each video frame.  This 
		//  means that we are looking for the following byte sequences:
		//       0x55, then 0xAA, then 0x55, then 0xAA for a normal block.
		//       0x55, then 0xAA, then 0x56, then 0xAA for a color code block.
		//  The expectation is that the block data will be read in a different routine for processing.
		
		byte[] word1 = new byte[2];
		byte[] tmp   = new byte[1];

		word1 = getVariableSizeBuffer(2);

		// Check to see if any data was returned.
		if ((word1[0] == (byte) 0x00) && (word1[1] == (byte) 0x00))
			return 0;	// In I2C, this means no data so return immediately.

		// Check to see if we found a sync marker
		if ((word1[0] == (byte) 0x55) && (word1[1] == (byte) 0xAA))
		{
			//  By reaching this point, we have found a sync word.  Now we need to look for the 
			//  next to see if it is the start of a block.
			word1 = getVariableSizeBuffer(2);
			if ((word1[0] == (byte) 0x55) && (word1[1] == (byte) 0xAA))
				return 1;	// Found normal block.
			
			if ((word1[0] == (byte) 0x56) && (word1[1] == (byte) 0xAA))
				return 2;	// Found a color code block
			
			return 0; // We did not find the start of a frame's block data.
		}

		// the rest of the routine searches for special cases where the sync word might be offset...
		if ((word1[0] == (byte) 0x00) && (word1[1] == (byte) 0x55))
		{
			// Check to see if this could be an offset sync word by looking at the next byte.
			tmp = getVariableSizeBuffer(1);
			if (tmp[0]!=(byte)0xAA )
				return 0;// Nope.
			
			//  By reaching this point, we have found a sync word.  Now we need to look for the 
			//  next to see if it is the start of a block.
			word1 = getVariableSizeBuffer(2);
			if ((word1[0] == (byte) 0x55) && (word1[1] == (byte) 0xAA))
				return 1;	// Found normal block.
			
			if ((word1[0] == (byte) 0x56) && (word1[1] == (byte) 0xAA))
				return 2;	// Found a color code block
			
			return 0; // We did not find the start of a frame's block data.
			
		}

		if ((word1[0] == (byte) 0xAA) && (word1[1] == (byte) 0x55))
		{
			// Check to see if this could be an offset sync word by looking at the next byte.
			tmp = getVariableSizeBuffer(1);
			if (tmp[0]==(byte)0xAA )
				return 1;// Found a normal block
			else
				return 0;// Nope
		}

		if ((word1[0] == (byte) 0xAA) && (word1[1] == (byte) 0x56))
		{
			// Check to see if this could be an offset sync word by looking at the next byte.
			tmp = getVariableSizeBuffer(1);
			if (tmp[0]==(byte)0xAA )
				return 2;// Found a Color code block
			else
				return 0;// Nope
		}

		return 0;// Nope
	}
}
