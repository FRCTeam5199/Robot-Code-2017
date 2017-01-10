package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.I2C;

public class Pixy {
	
	I2C pixyBus;
	
	short PIXY_ADDRESS = 0x51;
	
	private Pixy(){
		pixyBus = new I2C(I2C.Port.kOnboard, PIXY_ADDRESS);
	}
	
	public short getWord(){
		byte[] buffer = {0, 0};
	
		pixyBus.readOnly(buffer, 2);
		return (short)(buffer[1]<<8 | buffer[0]);
	}
}
