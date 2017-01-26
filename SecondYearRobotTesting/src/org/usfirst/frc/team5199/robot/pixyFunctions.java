package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class pixyFunctions {
	NewPixy pixyCam;
	UltrasonicFunctions ultraFunctions;
	Talon rightMotor, leftMotor;
	robotDrive robotDriver;
	Joystick stick;
	boolean tomare = false;
	public static final double distanceOffBuffer =2;
	
	public pixyFunctions(NewPixy pixy,Talon right,Talon left,UltrasonicFunctions ultra){
		rightMotor = right;
		leftMotor = left;
		robotDriver = new robotDrive(rightMotor, leftMotor);
		ultraFunctions = ultra;
		stick = new Joystick(1);
		pixyCam = pixy; 
		
	}
	public void center(){
		double distanceRight = 0,distanceLeft=0, distanceOff =0;
		tomare = false;
		do{
			if (pixyCam.getStartOfData() == 1) {

				short sig;

				byte[] syncedBufferWithoutSync = new byte[12];
				syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(12);
				SyncedBlock ourBlock1 = new SyncedBlock(syncedBufferWithoutSync);
				sig = ourBlock1.getSignature();
				SmartDashboard.putNumber(sig + " Synced Checksum:", ourBlock1.getChecksum());
				// SmartDashboard.putNumber("Synced Signature:",
				// ourBlock.getSignature());
				distanceRight = ourBlock1.getX();
				SmartDashboard.putString(sig + " Synced X:", String.valueOf(ourBlock1.getX()));
				SmartDashboard.putString(sig + " Synced Y:", String.valueOf(ourBlock1.getY()));
				// SmartDashboard.putString("Synced X HEX:",
				// SyncedBlock.getHexRepresentation(ourBlock.rawData[4],
				// ourBlock.rawData[5]));
				// SmartDashboard.putString("Synced Y HEX:",
				// SyncedBlock.getHexRepresentation(ourBlock.rawData[6],
				// ourBlock.rawData[7]));
				SmartDashboard.putString(sig + " Synced Width:", String.valueOf(ourBlock1.getWidth()));
				SmartDashboard.putString(sig + " Synced height:", String.valueOf(ourBlock1.getHeight()));

				byte[] secondBlockSync = new byte[2];
				secondBlockSync = pixyCam.getVariableSizeBuffer(2);
				if ((secondBlockSync[0] == (byte) 0x55) && (secondBlockSync[1] == (byte) 0xAA)) {
					syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(12);
					SyncedBlock ourBlock2 = new SyncedBlock(syncedBufferWithoutSync);
					sig = ourBlock2.getSignature();
					distanceLeft = ourBlock2.getX();
					SmartDashboard.putNumber(sig + " Synced Checksum:", ourBlock2.getChecksum());
					SmartDashboard.putString(sig + " Synced X:", String.valueOf(ourBlock2.getX()));
					SmartDashboard.putString(sig + " Synced Y:", String.valueOf(ourBlock2.getY()));
					SmartDashboard.putString(sig + " Synced Width:", String.valueOf(ourBlock2.getWidth()));
					SmartDashboard.putString(sig + " Synced height:", String.valueOf(ourBlock2.getHeight()));
				}
			}
			distanceOff = distanceRight + distanceLeft-320;
			if(distanceOff<(-1*distanceOffBuffer)){
			do{
				robotDriver.drive(0,-.15, 1);
				ultraFunctions.goBackTooClosePixy();
				if (pixyCam.getStartOfData() == 1) {

					short sig;

					byte[] syncedBufferWithoutSync = new byte[12];
					syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(12);
					SyncedBlock ourBlock1 = new SyncedBlock(syncedBufferWithoutSync);
					sig = ourBlock1.getSignature();
					SmartDashboard.putNumber(sig + " Synced Checksum:", ourBlock1.getChecksum());
					// SmartDashboard.putNumber("Synced Signature:",
					// ourBlock.getSignature());
					distanceRight = ourBlock1.getX();
					SmartDashboard.putString(sig + " Synced X:", String.valueOf(ourBlock1.getX()));
					SmartDashboard.putString(sig + " Synced Y:", String.valueOf(ourBlock1.getY()));
					// SmartDashboard.putString("Synced X HEX:",
					// SyncedBlock.getHexRepresentation(ourBlock.rawData[4],
					// ourBlock.rawData[5]));
					// SmartDashboard.putString("Synced Y HEX:",
					// SyncedBlock.getHexRepresentation(ourBlock.rawData[6],
					// ourBlock.rawData[7]));
					SmartDashboard.putString(sig + " Synced Width:", String.valueOf(ourBlock1.getWidth()));
					SmartDashboard.putString(sig + " Synced height:", String.valueOf(ourBlock1.getHeight()));

					byte[] secondBlockSync = new byte[2];
					secondBlockSync = pixyCam.getVariableSizeBuffer(2);
					if ((secondBlockSync[0] == (byte) 0x55) && (secondBlockSync[1] == (byte) 0xAA)) {
						syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(12);
						SyncedBlock ourBlock2 = new SyncedBlock(syncedBufferWithoutSync);
						sig = ourBlock2.getSignature();
						distanceLeft = ourBlock2.getX();
						SmartDashboard.putNumber(sig + " Synced Checksum:", ourBlock2.getChecksum());
						SmartDashboard.putString(sig + " Synced X:", String.valueOf(ourBlock2.getX()));
						SmartDashboard.putString(sig + " Synced Y:", String.valueOf(ourBlock2.getY()));
						SmartDashboard.putString(sig + " Synced Width:", String.valueOf(ourBlock2.getWidth()));
						SmartDashboard.putString(sig + " Synced height:", String.valueOf(ourBlock2.getHeight()));
					}
				}
				distanceOff = distanceRight + distanceLeft-320;
				if(stick.getRawButton(2) && stick.getRawButton(1)){
					tomare = true;
				}
			}while(distanceOff<(-1*distanceOffBuffer) && tomare == true);
			
			}
			if(distanceOff>distanceOffBuffer){
				do{
					robotDriver.drive(0,.15, 1);
					ultraFunctions.goBackTooClosePixy();
					if (pixyCam.getStartOfData() == 1) {

						short sig;

						byte[] syncedBufferWithoutSync = new byte[12];
						syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(12);
						SyncedBlock ourBlock1 = new SyncedBlock(syncedBufferWithoutSync);
						sig = ourBlock1.getSignature();
						SmartDashboard.putNumber(sig + " Synced Checksum:", ourBlock1.getChecksum());
						// SmartDashboard.putNumber("Synced Signature:",
						// ourBlock.getSignature());
						distanceRight = ourBlock1.getX();
						SmartDashboard.putString(sig + " Synced X:", String.valueOf(ourBlock1.getX()));
						SmartDashboard.putString(sig + " Synced Y:", String.valueOf(ourBlock1.getY()));
						// SmartDashboard.putString("Synced X HEX:",
						// SyncedBlock.getHexRepresentation(ourBlock.rawData[4],
						// ourBlock.rawData[5]));
						// SmartDashboard.putString("Synced Y HEX:",
						// SyncedBlock.getHexRepresentation(ourBlock.rawData[6],
						// ourBlock.rawData[7]));
						SmartDashboard.putString(sig + " Synced Width:", String.valueOf(ourBlock1.getWidth()));
						SmartDashboard.putString(sig + " Synced height:", String.valueOf(ourBlock1.getHeight()));

						byte[] secondBlockSync = new byte[2];
						secondBlockSync = pixyCam.getVariableSizeBuffer(2);
						if ((secondBlockSync[0] == (byte) 0x55) && (secondBlockSync[1] == (byte) 0xAA)) {
							syncedBufferWithoutSync = pixyCam.getVariableSizeBuffer(12);
							SyncedBlock ourBlock2 = new SyncedBlock(syncedBufferWithoutSync);
							sig = ourBlock2.getSignature();
							distanceLeft = ourBlock2.getX();
							SmartDashboard.putNumber(sig + " Synced Checksum:", ourBlock2.getChecksum());
							SmartDashboard.putString(sig + " Synced X:", String.valueOf(ourBlock2.getX()));
							SmartDashboard.putString(sig + " Synced Y:", String.valueOf(ourBlock2.getY()));
							SmartDashboard.putString(sig + " Synced Width:", String.valueOf(ourBlock2.getWidth()));
							SmartDashboard.putString(sig + " Synced height:", String.valueOf(ourBlock2.getHeight()));
						}
					}
					distanceOff = distanceRight + distanceLeft-320;
					if(stick.getRawButton(2) && stick.getRawButton(1)){
						tomare = true;
					}
				}while(distanceOff>distanceOffBuffer && tomare == true);
			}
			//ultraFunctions.pixySelfStraight();
		}while((distanceOff>distanceOffBuffer||distanceOff<(-1*distanceOffBuffer)));
		robotDriver.stop();
		
	}
}
