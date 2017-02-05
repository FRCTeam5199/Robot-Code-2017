
package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends SampleRobot {
	NewPixy pixyCam = new NewPixy();
	pixyFunctions pixyFunction;
		
	public void robotInit() {
		pixyFunction = new pixyFunctions(pixyCam);
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
		int numberOfPixyBlocks = 0;
		
		double timeStart =  System.currentTimeMillis();
		double elapsedTime;
	    while (isOperatorControl() && isEnabled()) {
//	    	//Center on peg macro Option 1:
//	    	/*
//	    	 * 1. "Center on peg"
//	    	 * 2. autoalign with wall
//	    	 * 3. check if still centered
//	    	 * 4. if not repeat steps 1-3 until centered
//	    	 * 5. drive forward until gear is on peg
//	    	 */
//	    	int[] distanceX =  pixyFunction.averageData(0);
//	    	int distance = distanceX[0];
//	    	if(distance>0)
//	    	do{
//	    		 distanceX =  pixyFunction.averageData(0);
//	    		 if(distanceX[0]>0){
//	    			 distance = distanceX[0];
//	    		 }
//	 	    	
//		    	int distanceOff = distance -100; //100 instead of 160 because 60 is the constant off
//		    	do{
//		    		 distanceX =  pixyFunction.averageData(0);
//		    		 if(distanceX[0]>0){
//		    			 distance = distanceX[0];
//		    		 }
//		 	    	
//		    		double turnPower = .01*distanceOff*7.5/(distance);
//		    		if(turnPower>.3||turnPower<-.3){
//		    			turnPower =.3;
//		    		}
//		    		if(distanceOff>0){
//		    		robotDriver.drive(.25,turnPower, 1);
//		    		} else if(distanceOff<0){
//			    		robotDriver.drive(.25, -1*turnPower, 1);
//			    		}
//		    	
//		    	}while((Math.abs(distance+60)-160)>bufferOff); 
//		    	//the number 60 is the constant of the displacement of pixy cam
//		    	//this is determined experimentally and should be changed
//		    	
//		    	//this centers the robot after "centering" to ensure that the robot truly centered itself
//		    	ultraFunction.center();
//		    	
//	    	}while((Math.abs(distance+60)-160)>bufferOff);
//	    	//center on peg macro Option 2:
//	    	/*1. get the robot centered and three feet away
//	    	 *2. turn x degrees
//	    	 *3. drive x distance
//	    	 *3. turn back x degrees
//	    	 *4. center
//	    	 *5. drive forward until gear is in proper target
//	    	 */
//	    	//1. get the robot centered and three feet away:
//	    	//centers robot
//	    	//ONLY USE IF OPTION 1 FAILS
////	    	do{ 
////	    	robotDriver.drive(0, ultraFunction.center(), 1);
////	    	}while(ultraFunction.center()!=0);
////	    	//drives until three feet away
////	    	do{
////	    	robotDriver.drive(ultraFunction.driveFowardUntil(36), 0,1);
////	    	}while(ultraFunction.driveForwardUntil(36)!=0);
////	    	//gets the angle to turn and turns the robot x degrees
//	    
//	    	
//	    	
//	    	
//	    
//	    }
	    numberOfLoops++;
	    if (pixyCam.getStartOfDataChris2017_01_31() == 1) {
	    	numberOfPixyBlocks++;
	    	SmartDashboard.putString("Detected", "Yes");
	    }
	    else{
	    	SmartDashboard.putString("Detected", "No");
	    }
	    elapsedTime = (System.currentTimeMillis()-timeStart)/1000;
	    SmartDashboard.putNumber("Elapsed Time", elapsedTime);
	    SmartDashboard.putNumber("Loops", numberOfLoops);
	    SmartDashboard.putNumber("PixyBlocks", numberOfPixyBlocks);
	    SmartDashboard.putNumber("Loops per second", numberOfLoops/elapsedTime);
	    SmartDashboard.putNumber("PixyBlocks per second", numberOfPixyBlocks/elapsedTime);
	   // SmartDashboard.putNumber("Avg X", pixyFunction.averageData(1)[0]);
	    }
	    
	    
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
