/*Not used (Pasqualino 2/1/2017)*/
package org.usfirst.frc.team5199.robot;

public class Block {

	// Documentation states that the size of an object block is 14 bytes.
	static final short sizeOfBlockObject = 14;
	
	// The raw bytes of the block that we retrieved.
	byte[] rawData = new byte[14];
	
	private short sync;			// Bytes 0..1		0xAA55 or 0xAA56 to signify ???
	private short checksum;		// Bytes 2..3		Sum of bytes 4..13
	private short signature;	// Bytes 4..5		???
	private short x;			// Bytes 6..7		X Center of Object [0, 320]
	private short y;			// Bytes 8..9		Y Center of Object [0, 200]
	private short width;		// Bytes 10..11		Width of the Object
	private short height;		// Bytes 12..13		Height of the Object
	
	public Block(byte[] fourteenByteBuffer) {
		this.rawData = fourteenByteBuffer;
		this.sync = (short) ((rawData[1] << 8) | (rawData[0]));
		this.checksum = (short) ((rawData[3] << 8) | (rawData[2]));
		this.signature = (short) ((rawData[5] << 8) | (rawData[4]));
		this.x = (short) ((rawData[7] << 8) | (rawData[6]));
		this.y = (short) ((rawData[9] << 8) | (rawData[8]));
		this.width = (short) ((rawData[11] << 8) | (rawData[10]));
		this.height = (short) ((rawData[13] << 8) | (rawData[12]));
	}
	
	static public String blockAsHexString(Block theBlock) {
		String hexString = "";
		for(int i = 0; i < sizeOfBlockObject; i++) {
			hexString += String.format("%02X", theBlock.rawData[i]) + " ";
		}
		return hexString;
	}
	
	public short getSync() {
		return this.sync;
	}
	
	public short getChecksum() {
		return this.checksum;
	}
	
	public short getSignature() {
		return this.signature;
	}
	
	public short getX() {
		return this.x;
	}
	
	public short getY() {
		return this.y;
	}
	
	public short getWidth() {
		return this.width;
	}
	
	public short getHeight() {
		return this.height;
	}
	
	static String shortAsHex(short value) {
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
