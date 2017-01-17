package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.I2C;

public class NewPixy {
	
	private I2C pixyBus;
	private int PIXY_ADDRESS = 0x51;
	private short currentWord;
	private short lastWord;
	
	public NewPixy() {
		pixyBus = new I2C(I2C.Port.kOnboard, PIXY_ADDRESS);
		currentWord = 0x0000;
		lastWord = 0x0000;
	}
	
	public boolean isFrameUpdate() {
		lastWord = currentWord;
		currentWord = getWord();
		if((currentWord == 0xAA55 || currentWord == 0xAA56) && (lastWord == 0xAA55 || lastWord == 0xAA56)) {
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
	
	public Block getDataBlock() {
		byte[] buffer = new byte[Block.sizeOfObjectBlock];
		pixyBus.readOnly(buffer, Block.sizeOfObjectBlock);
		Block theBlock = new Block();
		theBlock.sync 		= (short) ((buffer[1] << 8) | (buffer[0]));
		theBlock.checksum	= (short) ((buffer[3] << 8) | (buffer[2]));
		theBlock.signature	= (short) ((buffer[5] << 8) | (buffer[4]));
		theBlock.x			= (short) ((buffer[7] << 8) | (buffer[6]));
		theBlock.y			= (short) ((buffer[9] << 8) | (buffer[8]));
		theBlock.width		= (short) ((buffer[11] << 8) | (buffer[10]));
		theBlock.height		= (short) ((buffer[13] << 8) | (buffer[12]));
		return theBlock;
	}
	
	public void soutBlockData(Block block) {
		System.out.println("Sync:\t" + Block.asHex(block.sync));
		System.out.println("Checksum:\t" + Block.asHex(block.checksum));
		System.out.println("Signature:\t" + Block.asHex(block.signature));
		System.out.println("X:\t" + Block.asHex(block.x));
		System.out.println("Y:\t" + Block.asHex(block.y));
		System.out.println("Width:\t" + Block.asHex(block.width));
		System.out.println("Height:\t" + Block.asHex(block.height));
	}
	
	public String printBlockData(Block block) {
		return ("Sync: " + Block.asHex(block.sync) + "\t") +
			   ("Checksum: " + Block.asHex(block.checksum) + "\t") +
			   ("Signature: " + Block.asHex(block.signature) + "\t") +
			   ("X: " + block.x + "\t") +
			   ("Y: " + block.y + "\t") +
			   ("Width: " + Block.asHex(block.width) + "\t") +
			   ("Height: " + Block.asHex(block.height) + "\t");
	}
}
