
package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends SampleRobot {

	
	//Ultrasonic Sanic = new Ultrasonic(1,1);
		UltrasonicData ultra;
public void robotInit() {
	ultra = new UltrasonicData(4,5,2,3);

}

public void ultrasonicSample() {

	
	// reads the range on the ultrasonic sensor
}
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
    	
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
       
    }

    /**
     * This function is called periodically during operator control
     */
    
    
	public void operatorControl() {
		int numberOfLoops =0;
		double timeStart =  System.currentTimeMillis();
	    while (isOperatorControl() && isEnabled()) {
	    	ultra.distanceRight();
	    	
	    	numberOfLoops++;
	    }
	    SmartDashboard.putNumber("Time Elapsed", (System.currentTimeMillis()-timeStart)/1000);
	    SmartDashboard.putNumber("Number of Loops", numberOfLoops);
	    
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
