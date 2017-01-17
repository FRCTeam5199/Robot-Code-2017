package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.I2C;

public class Pixy {

	private I2C pixyBus;

	private short PIXY_ADDRESS = 0x51;
	private short W;
	private short lastW;

	public Pixy() {
		pixyBus = new I2C(I2C.Port.kOnboard, PIXY_ADDRESS);
	}

	public short getWord() {
		byte[] buffer = {0x00, 0x00};	

		//pixyBus.readOnly(buffer, 2);
		pixyBus.transaction(null, 0, buffer, 2);
		return (short) (buffer[1] << 8 | buffer[0]);
	}

	public boolean getIsFrameUpdate() {
		W = getWord();
		if (W == (short)0xAA55 && lastW == W) {
			lastW = W;
			return true;
		} else {
			lastW = W;
			return false;
		}

	}
	
	
	
}
