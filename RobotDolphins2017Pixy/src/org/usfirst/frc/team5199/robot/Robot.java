package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {

	String test = "START: ";
	int counter = 0, display = 0, zeroCount = 0, zeroDisp = 0;
	int oneCount = 0, oneDisp = 0;
	boolean oneCounter = false;

	NewPixy pixyCam = new NewPixy(); // Pixy.java is Kevin's, NewPixy.java is
										// Kyle's.

	public Robot() {

	}

	public void operatorControl() {

		while (isOperatorControl() && isEnabled()) {

			if (pixyCam.syncTest() == 1) {
				oneCounter = true;
			} else if (pixyCam.syncTest() == 0) {
				oneCounter = false;
				oneCount = 0;
			}
			if (oneCounter) {
				oneCount++;
				if (oneDisp < oneCount) {
					oneDisp = oneCount;
				}
			}

			SmartDashboard.putNumber("One Counter", oneDisp);

			// if(Block.isValidPacket(currentData)) {
			// Only update the Smart Dashboard if a valid packet is supplied.
			// SmartDashboard.putString("Pixy Cam Data",
			// pixyCam.printBlockData(currentData));
			// }

		}
	}
}
