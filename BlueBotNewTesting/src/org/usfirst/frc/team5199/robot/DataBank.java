package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

public class DataBank {
	public Pixy pixyGear, pixyShooter;
	public PixyProcess pixyGearProc, pixyShooterProc;
	public EncoderDriveFunctions driveEncoders;
	public UltrasonicData ultraData;
	public 	Encoder flywheel;
	public CANTalon turret;
	public CircularAverageBuffer flywheelAVG;
	long time = System.currentTimeMillis();
	public Victor right = new Victor(0), left = new Victor(1);

	public DataBank(){
		pixyGear = new Pixy(0x51);
		pixyGearProc = new PixyProcess(pixyGear);
		pixyShooter = new Pixy(0x53);
		pixyShooterProc = new PixyProcess(pixyShooter);
		time = System.currentTimeMillis();
		ultraData = new UltrasonicData(1,0,3,2);
		flywheel =  new Encoder(5, 4, false, Encoder.EncodingType.k4X);
		flywheel.reset();
		flywheel.setDistancePerPulse(RobotMap.inchesPerRotation / 2);
		flywheelAVG = new CircularAverageBuffer(75);
		driveEncoders = new EncoderDriveFunctions(right,left);
//		turret.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
//		driveEncoders = new EncoderDriveFunctions(, null, null);
	}
	public double ultraDistanceRight(){
		return ultraData.distanceRight();
	}
	public double ultraDistanceLeft(){
		return ultraData.distanceLeft();
	}
	public double pixyGearXPos(){
		return pixyGearProc.compensatedGearPixyData();
	}
	public double pixyShooterXPos(){
		return pixyShooterProc.shooterData()[0];
	}
	public double shooterRPM(){
		return flywheelAVG.DataAverage(flywheel.getRate());
	}
	public double turretPosition(){
		return turret.getPosition();
	}
	
}
