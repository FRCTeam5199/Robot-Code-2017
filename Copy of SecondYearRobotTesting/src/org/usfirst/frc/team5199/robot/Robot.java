package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends SampleRobot {

	public static Ultrasonic UltraTest;
	public static Ultrasonic UltraTest2;
	
	public Robot() {
		
		UltraTest = new Ultrasonic(3,2);
		UltraTest2 = new Ultrasonic(4,5);
		UltraTest.setAutomaticMode(true);
		
		UltraTest2.setAutomaticMode(true);
	}

	/**
	 * Runs the motors with Mecanum drive.
	 */

	@SuppressWarnings("deprecation")
	public void operatorControl() {

		while (isOperatorControl() && isEnabled()) {

			SmartDashboard.putBoolean("Enabled?", UltraTest.isEnabled());
			SmartDashboard.putNumber("distTest", UltraTest.getRangeInches());
			SmartDashboard.putBoolean("Enabled?", UltraTest2.isEnabled());
			SmartDashboard.putNumber("distTestLEft", UltraTest2.getRangeInches());

		}

	}

	

	public void autonomousPeriodic() {

	}

	public void autonomousInit() {

	}
}
