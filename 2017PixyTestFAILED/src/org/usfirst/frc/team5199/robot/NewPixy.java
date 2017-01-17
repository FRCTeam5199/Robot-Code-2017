package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.I2C;

public class NewPixy {

	// private final int PIXY_ARRAYSIZE = 100;
	// private final int PIXY_START_WORD = 0x0aa55;
	// private final int PIXY_START_WORD_CC = 0x0aa56;
	// private final int PIXY_START_WORDX = 0x055aa;
	// // #define PIXY_SERVO_SYNC 0xff
	// // #define PIXY_CAM_BRIGHTNESS_SYNC 0xfe
	// // #define PIXY_LED_SYNC 0xfd
	// // #define PIXY_OUTBUF_SIZE 64
	// // #define PIXY_SYNC_BYTE 0x5a
	// // #define PIXY_SYNC_BYTE_DATA 0x5b
	// private int blockType;
	// private static boolean skipStart = false;

	private I2C pixyBus;
	private final int PIXY_ADDRESS = 0x51;
	private short currentWord;
	private short lastWord;

	static Block[] blocks;

	public NewPixy() {
		pixyBus = new I2C(I2C.Port.kOnboard, PIXY_ADDRESS);
		currentWord = (short) 0x0000;
		lastWord = (short) 0x0000;
		// blocks = new Block[PIXY_ARRAYSIZE];
	}

	public boolean isFrameUpdate() {
		lastWord = currentWord;
		currentWord = getWord();
		if ((currentWord == (short) 0xAA55 || currentWord == (short) 0xAA56) && (lastWord == (short) 0xAA55 || lastWord == (short) 0xAA56)) {
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

	public String syncTest() {
		byte[] buffer = new byte[200];
		
		// In case we read the first byte and it's in the middle of a packet, flush the buffer.
		buffer[0] = (byte)0xff;
		while (buffer[0] != 0) {
			pixyBus.readOnly(buffer, 1);
			
		}
		
		// Read until we find a non-zero byte...
		while(buffer[0] == 0) {
			pixyBus.readOnly(buffer, 1);
			
		}
		String outString= String.format("0x%02X", buffer[0]);
//		pixyBus.readOnly(buffer, 20);
//
//			for(int i=0;i<20;i++){
//				outString = outString + " " + String.format("0x%02X", buffer[i]);
//			}
			return outString;
			
//			if (((short) ((buffer[1] << 8) | buffer[0])) == ((short) 0xaa55)) {
//				return 1;
//			} else if (((short) ((buffer[1] << 8) | buffer[0])) == ((short) 0x0000)) {
//				return 0;
//			} else {
//				return -1;
//			}
//		
	}

	// public int getStart() {
	// int w, lastw;
	//
	// lastw = 0x0ffff;
	//
	// while (true) {
	// w = getWord();
	// if (w == 0 && lastw == 0)
	// return 0; // no start code
	// else if (w == PIXY_START_WORD && lastw == PIXY_START_WORD) {
	// blockType = 1; // NORMAL_BLOCK
	// return 1; // code found!
	// } else if (w == PIXY_START_WORD_CC && lastw == PIXY_START_WORD) {
	// blockType = 2; // CC_BLOCK // found color code block
	// return 1;
	// } else if (w == PIXY_START_WORDX){
	// getByte(); // we're out of sync! (backwards)
	// }
	// lastw = w;
	// }
	// }
	//
	// public int getBlocks(int maxBlocks){
	// int w, blockCount, checksum;
	//
	//
	// if (!skipStart)
	// {
	// if (getStart()==0)
	// return 0;
	// }
	// else
	// skipStart = false;
	//
	// for(blockCount=0; blockCount<maxBlocks && blockCount<PIXY_ARRAYSIZE;)
	// {
	// checksum = getWord();
	// if (checksum==PIXY_START_WORD) // we've reached the beginning of the next
	// frame
	// {
	// skipStart = true;
	// blockType = 1; //normal
	// return blockCount;
	// }
	// else if (checksum==PIXY_START_WORD_CC)
	// {
	// skipStart = true;
	// blockType = 2; //Color Code Block
	// return blockCount;
	// }
	// else if (checksum==0)
	// return blockCount;
	//
	// blocks[blockCount] = getDataBlock();
	//
	// // check checksum
	// if (blocks[blockCount].checksum==blocks[blockCount].sum){
	// blockCount++;
	// }
	// //else
	// //printf("checksum error!\n");
	//
	// w = getWord();
	// if (w==PIXY_START_WORD)
	// blockType = 1;
	// else if (w==PIXY_START_WORD_CC)
	// blockType = 2;
	// else
	// return blockCount;
	// }
	// return blockCount;
	// }

	public Block getDataBlock() {
		byte[] buffer = new byte[Block.sizeOfObjectBlock];
		Block theBlock = new Block();
		/* theBlock.valid = ! */pixyBus.readOnly(buffer, Block.sizeOfObjectBlock);
		theBlock.sync = (short) ((buffer[1] << 8) | (buffer[0]));
		theBlock.checksum = (short) ((buffer[3] << 8) | (buffer[2]));
		theBlock.signature = (short) ((buffer[5] << 8) | (buffer[4]));
		theBlock.x = (short) ((buffer[7] << 8) | (buffer[6]));
		theBlock.y = (short) ((buffer[9] << 8) | (buffer[8]));
		theBlock.width = (short) ((buffer[11] << 8) | (buffer[10]));
		theBlock.height = (short) ((buffer[13] << 8) | (buffer[12]));
		// int sum = 0;
		// for(int i = 4; i<= 13; i++){
		// sum += buffer[i];
		// }
		// theBlock.sum = sum;
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
		// if (block.valid) {
		return ("Sync: " + Block.asHex(block.sync) + "\t") + ("Checksum: " + Block.asHex(block.checksum) + "\t")
				+ ("Signature: " + Block.asHex(block.signature) + "\t") + ("X: " + block.x + "\t")
				+ ("Y: " + block.y + "\t") + ("Width: " + Block.asHex(block.width) + "\t")
				+ ("Height: " + Block.asHex(block.height) + "\t");
		// } else {
		// return ("No new data");
		// }
	}
}
