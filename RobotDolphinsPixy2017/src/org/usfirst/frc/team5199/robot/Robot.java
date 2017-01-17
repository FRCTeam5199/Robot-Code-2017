package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {

	String test = "START: ";
	int x, y, width, height;
	boolean oneCounter = false;

	NewPixy pixyCam = new NewPixy();

	public Robot() {

	}

//	public void operatorControl() {
//		while (isOperatorControl() && isEnabled()) {
//			Block pixyData = pixyCam.getDataBlock();
//			if(Block.isValidPacket(pixyData)) {
//				SmartDashboard.putString("The Whole Block: ", Block.blockAsHexString(pixyData));
//				SmartDashboard.putString("Sync: ", Block.shortAsHex(pixyData.getSync()));
//				SmartDashboard.putString("Checksum: ", Block.shortAsHex(pixyData.getChecksum()));
//				SmartDashboard.putString("Signautre: ", Block.shortAsHex(pixyData.getSignature()));
//				SmartDashboard.putString("X: ", String.valueOf(pixyData.getX()));
//				SmartDashboard.putString("Y: ", String.valueOf(pixyData.getY()));
//				SmartDashboard.putString("Width: ", String.valueOf(pixyData.getWidth()));
//				SmartDashboard.putString("Height: ", String.valueOf(pixyData.getHeight()));
//			}
//		}
//	}
	
//	public void operatorControl() {
//		while(isOperatorControl() && isEnabled()) {
//			byte buffer[] = new byte[64];
//			buffer = pixyCam.getVariableSizeBuffer(64);
//			String bufferAsHexString = "";
//			for(int i = 0; i < buffer.length; i++) {
//				bufferAsHexString += String.format("%02X", buffer[i]) + " ";
//			}
//			SmartDashboard.putString("Buffer: ", bufferAsHexString);
//		}
//	}
	
	@SuppressWarnings("deprecation")
//	public void operatorControl() {
//		
//		int numberOfFoundPackets = 0;
//		
//		while(isOperatorControl() && isEnabled()) {
//			int BUFFER_SIZE = 64;
//			byte buffer[] = new byte[BUFFER_SIZE];
//			buffer = pixyCam.getVariableSizeBuffer(BUFFER_SIZE);
//			for(int i = 0; i < BUFFER_SIZE; i++) {
//				// Make sure we won't get an ArrayOutOfBoundsException
//				if(i + 3 <= BUFFER_SIZE - 1) {
//					if(buffer[i] == (byte) 0x55) {
//						if(buffer[i+1] == (byte) 0xAA) {
//							if(buffer[i+2] == (byte) 0x55) {
//								if(buffer[i+3] == (byte) 0xAA) {
//									numberOfFoundPackets++;
//								}
//							}
//						}
//					}
//				}
//			}
//			SmartDashboard.putInt("Number of Found Packets: ", numberOfFoundPackets);
//		}
//	}
	
//	public void operatorControl() {
//		
//		int numberOfFoundPackets = 0;
//		
//		while(isOperatorControl() && isEnabled()) {
//			int BUFFER_SIZE = 64;
//			byte buffer[] = new byte[BUFFER_SIZE];
//			buffer = pixyCam.getVariableSizeBuffer(BUFFER_SIZE);
//			for(int i = 0; i < BUFFER_SIZE; i++) {
//				// Make sure we won't get an ArrayOutOfBoundsException
//				if(i + 5 <= BUFFER_SIZE - 1) {
//					if(buffer[i] == (byte) 0x00) {
//						if(buffer[i+1] == (byte) 0x00) {
//							if(buffer[i+2] == (byte) 0x55) {
//								if(buffer[i+3] == (byte) 0xAA) {
//									if(buffer[i+4] == (byte) 0x55) {
//										if(buffer[i+5] == (byte) 0xAA) {
//											numberOfFoundPackets++;
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//			SmartDashboard.putInt("Number of Found Packets Starting with 0x00 0x00: ", numberOfFoundPackets);
//		}
//	}
	
	public void operatorControl() {
		
		int count = 0;
		while(isOperatorControl() && isEnabled()) {
			
			if(getStartOfData() == 1) {
				
				short sig;
				
				byte[] syncedBufferWithoutSync = new byte[12];
				syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(12);
				SyncedBlock ourBlock1 = new SyncedBlock(syncedBufferWithoutSync);
				sig = ourBlock1.getSignature();
				SmartDashboard.putNumber(sig + " Synced Checksum:", ourBlock1.getChecksum());
//				SmartDashboard.putNumber("Synced Signature:", ourBlock.getSignature());
				SmartDashboard.putString(sig + " Synced X:", String.valueOf(ourBlock1.getX()));
				SmartDashboard.putString(sig + " Synced Y:", String.valueOf(ourBlock1.getY()));
//				SmartDashboard.putString("Synced X HEX:", SyncedBlock.getHexRepresentation(ourBlock.rawData[4], ourBlock.rawData[5]));
//				SmartDashboard.putString("Synced Y HEX:", SyncedBlock.getHexRepresentation(ourBlock.rawData[6], ourBlock.rawData[7]));
				SmartDashboard.putString(sig + " Synced Width:", String.valueOf(ourBlock1.getWidth()));
				SmartDashboard.putString(sig + " Synced height:", String.valueOf(ourBlock1.getHeight()));
				
				byte[] secondBlockSync = new byte[2];
				secondBlockSync = pixyCam.getVariableSizeBuffer(2);
				if((secondBlockSync[0] == (byte) 0x55) && (secondBlockSync[1] == (byte) 0xAA)) {
					syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(12);
					SyncedBlock ourBlock2 = new SyncedBlock(syncedBufferWithoutSync);
					sig = ourBlock2.getSignature();
					SmartDashboard.putNumber(sig + " Synced Checksum:", ourBlock2.getChecksum());
					SmartDashboard.putString(sig + " Synced X:", String.valueOf(ourBlock2.getX()));
					SmartDashboard.putString(sig + " Synced Y:", String.valueOf(ourBlock2.getY()));
					SmartDashboard.putString(sig + " Synced Width:", String.valueOf(ourBlock2.getWidth()));
					SmartDashboard.putString(sig + " Synced height:", String.valueOf(ourBlock2.getHeight()));
				}
			}
			
			if(count % 100 == 0) {
				SmartDashboard.putNumber("Count: ", (double) count);
			}
		}
	}
	
	// Align the buffer with the start of a new data packet.
	int getStartOfData() {
		byte[] word = new byte[2];
		byte[] lastWord = {(byte) 0xFF, (byte) 0xFF};
		while(true) {
			word = pixyCam.getVariableSizeBuffer(2);
			if((word[0] == (byte) 0x00) && (word[1] == (byte) 0x00) && (lastWord[0] == (byte) 0x00) && (lastWord[1] == (byte) 0x00)) {
				// In I2C, this means no data so return immediately.
				return 0;
			} else if((word[0] == (byte) 0x55) && (word[1] == (byte) 0xAA) && (lastWord[0] == (byte) 0x55) && (lastWord[1] == (byte) 0xAA)) {
				// Found normal block.
				return 1;
			} else if((word[0] == (byte) 0x56) && (word[1] == (byte) 0xAA) && (lastWord[0] == (byte) 0x55) && (lastWord[1] == (byte) 0xAA)) {
				// Found color code block.
				return 2;
			} else if((word[0] == (byte) 0xAA) && (word[1] == (byte) 0x55)) {
				// This is important, we might be byte reversed, or at a sync.
				pixyCam.getByte();
			}
			lastWord[0] = word[0];
			lastWord[1] = word[1];
		}
	}
	
	
}
