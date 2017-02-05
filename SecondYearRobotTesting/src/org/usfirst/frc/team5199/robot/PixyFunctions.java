package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Talon;

public class PixyFunctions {
	public static pixyData pixyFunction, shooterData;
	public static robotDrive robotDriver;
	public static final int bufferOff =3; 
	public static UltrasonicFunctions ultraFunctions;
	public PixyFunctions(pixyData data,pixyData data2, Talon motorRight, Talon motorLeft, UltrasonicFunctions ultra){
		robotDriver = new robotDrive(motorRight, motorLeft);
		pixyFunction = data;
		shooterData = data2;
		ultraFunctions = ultra;
	}
	public static void alignGearPeg(){
		int[] distanceX =  pixyData.averageData(0);
    	int distance = distanceX[0];
    	if(distance>0){
    	do{
    		 distanceX =  pixyFunction.averageData(0);
    		 if(distanceX[0]>0){
    			 distance = distanceX[0];
    		 }
 	    	
	    	int distanceOff = distance -100; //100 instead of 160 because 60 is the constant off or whatever number
	    	do{
	    		 distanceX =  pixyFunction.averageData(0);
	    		 if(distanceX[0]>0){
	    			 distance = distanceX[0];
	    		 }
	 	    	
	    		double turnPower = Math.abs(.01*distanceOff*7.5/(distance));
	    		if(turnPower>.3){
	    			turnPower =.3;
	    		}
	    		
	    		if(distanceOff<0){
	    		robotDriver.drive(.25,turnPower, 1);
	    		} else if(distanceOff>0){
		    		robotDriver.drive(.25, -1*turnPower, 1);
		    		}
	    	ultraFunctions.goBackTooClosePixy();
	    	}while((Math.abs(distance+60)-160)>bufferOff); 
    	
	    	//the number 60 is the constant of the displacement of pixy cam
	    	//this is determined experimentally and should be changed
	    	
	    	//this centers the robot after "centering" to ensure that the robot truly centered itself
	    	ultraFunctions.selfStraight();
	    	ultraFunctions.goBackTooClosePixy();
    	}while((Math.abs(distance+60)-160)>bufferOff);
    	}
    	ultraFunctions.driveFowardUntil(12);
    	robotDriver.stop();
	}
	public static void shoot(){
		//if no data is found by the pixycam pan until it reads a block
		//this allows the camera to find and center on the boiler for shooting.
		//TODO Create a manual adjustment for the x position of the turret, just in case the pixyCam cannot see
		//the reflective tape
		int[] distanceX = shooterData.averageData(0);
		int distance = distanceX[0]; 
		int distanceOff;
	    	
    	
		if(distance>0){
			
			 if(distanceX[0]>0){
				 distance = distanceX[0];
			 }
			 distanceOff = distance -100;
			 //possibly add a constant for distance
			 while((Math.abs(distance)-160)>bufferOff){
				 if(distanceOff>0){
					 //turn turret right
					 
				 }else if(distanceOff<0){
					 //turn turret left
				 }
			 }
		}
	}

}
