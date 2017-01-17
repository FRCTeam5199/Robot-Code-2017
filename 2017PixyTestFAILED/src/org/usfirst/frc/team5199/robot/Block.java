package org.usfirst.frc.team5199.robot;

public class Block {
	
	// Documentation states that the size of an object block is 13 bytes.
	static int sizeOfObjectBlock = 14;
	
	short sync;			// Bytes 0..1		0xAA55 or 0xAA56
	short checksum;		// Bytes 2..3		Sum of bytes 4..13
	short signature;	// Bytes 4..5		???
	short x;			// Bytes 6..7		X Center of Object [0, 320]
	short y;			// Bytes 8..9		Y Center of Object [0, 200]
	short width;		// Bytes 10..11		Width of the Object
	short height;		// Bytes 12..13		Height of the Object
	//int sum;
	//boolean valid;		// Return value of i2c (success/failure)
	
	static String asHex(short value) {
		return String.format("0x%04X", value);
	}
	
	// When we get the data using readOnly(), it doesn't always give us the block starting at sync.
	// It could give us some random numbers, but we don't know if the "start" of a packet is at block[0].
	// This code will check if it is a valid packet by checking if sync == 0xAA55 || 0xAA56.
	static boolean isValidPacket(Block block) {
		if(block.sync == (short) 0xAA55 || block.sync == (short) 0xAA56) {
			return true;
		} else {
			return false;
		}
	}

}
