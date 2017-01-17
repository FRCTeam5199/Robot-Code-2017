package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.I2C;

public class NewPixy {
	
	private I2C pixyBus;
	private int PIXY_ADDRESS = 0x51;
	private short currentWord;
	private short lastWord;
	
	public NewPixy() {
		pixyBus = new I2C(I2C.Port.kOnboard, PIXY_ADDRESS);
		currentWord = (short) 0x0000;
		lastWord = (short) 0x0000;
	}
	
	public boolean isFrameUpdate() {
		lastWord = currentWord;
		currentWord = getWord();
		if((currentWord == (short) 0xAA55 || currentWord == (short) 0xAA56) && (lastWord == (short) 0xAA55 || lastWord == (short) 0xAA56)) {
			return true;
		} else {
			return false;
		}
	}
	
	public byte getByte() {
		byte[] buffer = new byte[1];
		pixyBus.readOnly(buffer, 1);
		return buffer[0];
	}
	
	public short getWord() {
		byte[] buffer = new byte[2];
		pixyBus.readOnly(buffer, 2);
		return (short) ((buffer[1] << 8) | buffer[0]);
	}
	
	public byte[] getVariableSizeBuffer(int sizeOfBuffer) {
		byte[] buffer = new byte[sizeOfBuffer];
		pixyBus.readOnly(buffer, sizeOfBuffer);
		return buffer;
	}
	
	public Block getDataBlock() {
		byte[] buffer = new byte[Block.sizeOfBlockObject];
		pixyBus.readOnly(buffer, Block.sizeOfBlockObject);
		Block theBlock = new Block(buffer);
		return theBlock;
	}
	
	public void soutBlockData(Block block) {
		System.out.println("Sync:\t" + Block.shortAsHex(block.getSync()));
		System.out.println("Checksum:\t" + Block.shortAsHex(block.getChecksum()));
		System.out.println("Signature:\t" + Block.shortAsHex(block.getSignature()));
		System.out.println("X:\t" + Block.shortAsHex(block.getX()));
		System.out.println("Y:\t" + Block.shortAsHex(block.getY()));
		System.out.println("Width:\t" + Block.shortAsHex(block.getWidth()));
		System.out.println("Height:\t" + Block.shortAsHex(block.getHeight()));
	}
	
	public String printBlockData(Block block) {
		return ("Sync: " + Block.shortAsHex(block.getSync()) + "\t") +
			   ("Checksum: " + Block.shortAsHex(block.getChecksum()) + "\t") +
			   ("Signature: " + Block.shortAsHex(block.getSignature()) + "\t") +
			   ("X: " + block.getX() + "\t") +
			   ("Y: " + block.getY() + "\t") +
			   ("Width: " + Block.shortAsHex(block.getWidth()) + "\t") +
			   ("Height: " + Block.shortAsHex(block.getHeight()) + "\t");
	}
}
