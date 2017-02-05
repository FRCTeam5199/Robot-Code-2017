package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Talon;

public class PixyFunctions {
	public static PixyProcess pixyProc;
	public static UltrasonicFunctions ultraFunctions;
	public static EncoderFunctions encoder;
	public PixyFunctions(Pixy pixy, UltrasonicFunctions ultra, EncoderFunctions encoderF){
		pixyProc = new PixyProcess(pixy);
		ultraFunctions = ultra;
		encoder  =encoderF;
	}
	public PixyFunctions(Pixy pixy, Talon shooter, Talon angleAdjust, Talon turret){
		pixyProc = new PixyProcess(pixy);
	}
	public static void alignGear(){
		double currentTime = System.currentTimeMillis();
		do{
		pixyProc.averageData(0, false);
		}while((System.currentTimeMillis()-currentTime)<300);
		int xAdjustment = pixyProc.averageData(0, false)[0];
		//Fancy Maths to determine how many turns of the wheel are needed to go forward
		int distanceToDrive = fancyMaths;
		
		boolean turnRight;
		if(xAdjustment+RobotMap.pixyGearDistanceOff<160){
			turnRight =false;
		}else{
			turnRight = true;
		}
		//turn 45 degrees using encoders
				encoder.turnClicks(45, turnRight); //change to be number for a 45 degree turn
				//drive forward a distance using encoders to center on peg
				encoder.driveForward(distanceToDrive); //how far in inches to drive forward
				// turn back 45 degrees using encoders
				encoder.turnClicks(45, turnRight);// change to be a number for a 45 degree turn
				//makes robot parallel to wall
				ultraFunctions.selfStraight();
				//checks to make sure gear will hit peg
				if(!checkIfAlignedGear()){
					do{
						
							pixyProc.averageData(0, false);
							}while((System.currentTimeMillis()-currentTime)<300);
							xAdjustment = pixyProc.averageData(0, false)[0];
							//Fancy Maths to determine how many turns of the wheel are needed to go forward
							 distanceToDrive = fancyMaths;
							
							if(xAdjustment+RobotMap.pixyGearDistanceOff<160){
								turnRight =false;
							}else{
								turnRight = true;
							}
						//turn 45 degrees using encoders
						encoder.turnClicks(45, turnRight); //change to be number for a 45 degree turn
						//drive forward a distance using encoders to center on peg
						encoder.driveForward(distanceToDrive); //how far in inches to drive forward
						// turn back 45 degrees using encoders
						encoder.turnClicks(45, turnRight);// change to be a number for a 45 degree turn
						//makes robot parallel to wall
						ultraFunctions.selfStraight();
					}while(!checkIfAlignedGear());
				}
		
	public static boolean checkIfAlignedGear(){
		double currentTime = System.currentTimeMillis();
		do{
		pixyProc.averageData(0, false);
		}while((System.currentTimeMillis()-currentTime)<300);
		int xAdjustment = pixyProc.averageData(0, false)[0];
		if(xAdjustment<RobotMap.pixyGearDataBuffer){
			return true;
		}else{
			return false;
		}
	}
	public static boolean checkIfAlignedShooter(){
		double currentTime = System.currentTimeMillis();
		do{
		pixyProc.averageData(0, false);
		}while((System.currentTimeMillis()-currentTime)<100);
		int xAdjustment = pixyProc.averageData(0, false)[0];
		if(xAdjustment<RobotMap.pixyShooterDataBuffer){
			return true;
		}else{
			return false;
		}
	}
	public static void shoot(){
		//checks to see if the turret is lined up with the boiler
		//if it is not aligned, turret centers on target
		if(!checkIfAlignedShooter()){
			
		}
		//calculates the speed and angle necessary to shoot
		
		//makes the angle adjusment
		
		//adjusts motor speed
		
		//transports balls to be shot
	}
}
