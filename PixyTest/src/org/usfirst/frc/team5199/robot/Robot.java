package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {

	String test = "START: ";
	int x, y, width, height;
	boolean oneCounter = false;

	Pixy pixyCam = new Pixy(); // Pixy.java is Kevin's, NewPixy.java is
										// Kyle's.
	PixyPacket pack = new PixyPacket();

	public Robot() {

	}

	public void operatorControl() {
		SmartDashboard.putString("test1", ".");
		SmartDashboard.putString("test2", ".");
		SmartDashboard.putString("test3", ".");
		SmartDashboard.putString("test4", ".");
		while (isOperatorControl() && isEnabled()) {

			// if (pixyCam.syncTest() == 1) {
			// oneCounter = true;
			// } else if (pixyCam.syncTest() == 0) {
			// oneCounter = false;
			// oneCount = 0;
			// }
			// if (oneCounter) {
			// oneCount++;
			// if (oneDisp < oneCount) {
			// oneDisp = oneCount;
			// }
			// }
			//
			SmartDashboard.putString("test1", "Started");
			try {
				pack = pixyCam.readPacket(1);
				SmartDashboard.putString("test2", "Success!");
			} catch (PixyException e) {
				
				pack= null;// TODO Auto-generated catch block
				SmartDashboard.putString("test2", "Failed!");
				e.printStackTrace();

			}
			SmartDashboard.putString("test3", "Results:");

			try{
				SmartDashboard.putNumber("X", pack.X );
				SmartDashboard.putNumber("Y", pack.Y );
				SmartDashboard.putNumber("Height", pack.Height );
				SmartDashboard.putNumber("Width", pack.Width );
				SmartDashboard.putString("test4", "Success!");
			} catch (NullPointerException e){
				e.printStackTrace();
				SmartDashboard.putString("test4", "NullPointer");
			}
			

			// if(Block.isValidPacket(currentData)) {
			// Only update the Smart Dashboard if a valid packet is supplied.
			// SmartDashboard.putString("Pixy Cam Data",
			// pixyCam.printBlockData(currentData));
			// }

		}
	}
}
