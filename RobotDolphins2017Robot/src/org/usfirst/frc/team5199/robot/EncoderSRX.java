package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

public class EncoderSRX {
	private static CANTalon encoder;
	public EncoderSRX(){
		encoder = new CANTalon(RobotMap.canTalonPort);
		encoder.reset();
		encoder.reverseSensor(false);
		encoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
//		encoder.configEncoderCodesPerRev(360);
		encoder.configEncoderCodesPerRev(1024);
		encoder.setPosition(0);
	}

}
