/*Not used (Pasqualino 2/1/2017)*/
package org.usfirst.frc.team5199.robot;

public class SyncedBlock {
	
	// Size of a block without the sync is 12 bytes.
	static final short sizeOfSyncedBlock = 12;
	
	// The raw bytes of the synced block.
	byte[] rawData = new byte[12];
	
	private short checksum;		// Bytes 0..1		Sum of bytes 4..13
	private short signature;	// Bytes 2..3		???
	private short x;			// Bytes 4..5		X Center of Object [0, 320]
	private short y;			// Bytes 6..7		Y Center of Object [0, 200]
	private short width;		// Bytes 8..9		Width of the Object
	private short height;		// Bytes 10..11		Height of the Object
	
	public SyncedBlock(byte[] twelveByteBuffer) {
		this.rawData = twelveByteBuffer;
		this.checksum = (short) ((rawData[1] << 8) | (rawData[0]));
		this.signature = (short) ((rawData[3] << 8) | (rawData[2]));
		this.x = concatByteToShort(rawData[4], rawData[5]);
		this.y = concatByteToShort(rawData[6], rawData[7]);
		this.width = concatByteToShort(rawData[8], rawData[9]);
		this.height = concatByteToShort(rawData[10], rawData[11]);
	}
	
	public short concatByteToShort(byte b1, byte b2) {
		return (short) ((b2 << 8) | (b1 & (short)(0x00FF)));
	}
	
	public static String getHexRepresentation(byte b1, byte b2) {
		return String.format("%02X", b2) + " " + String.format("%02X", b1);
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

}
