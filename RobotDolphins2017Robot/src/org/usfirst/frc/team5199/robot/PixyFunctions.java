package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;

public class PixyFunctions {
	public static PixyProcess pixyProc, pixyProcShooter;
	public static UltrasonicFunctions ultraFunctions;
	public static EncoderFunctions encoder;
	public static double turnPower;
	public static RobotDrive robot;
	public PixyFunctions(Pixy pixy, UltrasonicFunctions ultra, EncoderFunctions encoderF, RobotDrive driver){
		pixyProc = new PixyProcess(pixy);
		ultraFunctions = ultra;
		encoder  =encoderF;
		robot = driver;
	}
	public PixyFunctions(Pixy pixy, Talon shooter, Talon angleAdjust, Talon turret){
		pixyProcShooter = new PixyProcess(pixy);
	}
	public static void placeGearOnPegPrimary(){
		turnAndGoStraightAlign();
		if (ultraFunctions.driveFowardGearLoading() > 14) {
			while (ultraFunctions.driveFowardGearLoading()  > (20)) {
				robot.drive(-.25, 0, 1);
				if(Math.abs(pixyProc.averageData(0, false)[0]-160)>RobotMap.pixyGearDataBuffer){
					turnAndGoStraightAlign();
				}
			}
			if (ultraFunctions.driveFowardGearLoading() < (20)) {
				while (ultraFunctions.driveFowardGearLoading()  > (14)) {
					robot.drive(-.15, 0, 1);
					if(Math.abs(pixyProc.averageData(0, false)[0]-160)>RobotMap.pixyGearDataBuffer){
						turnAndGoStraightAlign();
					}
				}
			}
		}
		robot.stop();
	}
	public static void turnAndGoStraightAlign(){
		int distance = pixyProc.averageData(0, false)[0];
		int distanceOff;
		do{
			 distance = pixyProc.averageData(0, false)[0];
			 distanceOff = distance -160;
			 turnPower = Math.abs(.01*distanceOff*7.5/(distance));
			 if(turnPower>.3){
				 turnPower = .3;
			 }
			 if(distanceOff<0){
				robot.drive(-.25, -1*turnPower, 1);
			 }else if(distanceOff>0){
				 robot.drive(-.25, turnPower, 1); 
			 }
			 ultraFunctions.goBackTooClosePixy();
		}while(Math.abs(distance)>RobotMap.pixyGearDataBuffer);
	}
	public static void alignGear(){
		double currentTime = System.currentTimeMillis();
		do{
		pixyProc.averageData(0, false);
		}while((System.currentTimeMillis()-currentTime)<300);
		int xAdjustment = pixyProc.averageData(0, false)[0];
		//Fancy Maths to determine how many turns of the wheel are needed to go forward
		int distanceBetween =pixyProc.distanceBetweenRightAndLeft();
		double distanceToDrive =0;
		if(distanceBetween!=0){
			distanceToDrive = 7.5*Math.abs((xAdjustment-160)/distanceBetween)*Math.sqrt(2); //this should, should give us distance to drive forward
		}
		double turnAngle = Math.atan(distanceToDrive/12);
		boolean turnRight;
		if(xAdjustment+RobotMap.pixyGearDistanceOff<160){
			turnRight =false;
		}else{
			turnRight = true;
		}
		//turn 45 degrees using encoders
				encoder.turnClicks(turnAngle, turnRight); //change to be number for a 45 degree turn
				//drive forward a distance using encoders to center on peg
				encoder.driveForward(distanceToDrive); //how far in inches to drive forward
				// turn back 45 degrees using encoders
				if(turnRight){
					turnRight=false;
				}else{
					turnRight=true;
				}
				encoder.turnClicks(turnAngle, turnRight);// change to be a number for a 45 degree turn
				//makes robot parallel to wall
				ultraFunctions.selfStraight();
				//checks to make sure gear will hit peg
				if(!checkIfAlignedGear()){
					do{
						
							pixyProc.averageData(0, false);
							}while((System.currentTimeMillis()-currentTime)<300);
							xAdjustment = pixyProc.averageData(0, false)[0];
							//Fancy Maths to determine how many turns of the wheel are needed to go forward
							distanceBetween =pixyProc.distanceBetweenRightAndLeft();
							
							if(distanceBetween!=0){
								distanceToDrive = Math.sqrt((Math.pow(7.5*Math.abs(xAdjustment-160)/distanceBetween,2) + 1296)); //this should, should give us distance to drive forward
							}
							turnAngle = Math.atan(distanceToDrive/12);
							
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
						if(turnRight){
							turnRight=false;
						}else{
							turnRight=true;
						}
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
		if(Math.abs(xAdjustment+RobotMap.pixyGearDistanceOff-160)<RobotMap.pixyGearDataBuffer){
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
		if(Math.abs(xAdjustment+RobotMap.pixyShooterDistanceOff-160)<RobotMap.pixyShooterDataBuffer){
			return true;
		}else{
			return false;
		}
	}
	public static void shoot(TalonSRX turret, Spark intake, Spark transport){
		//checks to see if the turret is lined up with the boiler
		//if it is not aligned, turret centers on target
		if(!checkIfAlignedShooter()){
			int xAdjustment = pixyProc.averageData(0, false)[0];
			do{
				xAdjustment = pixyProc.averageData(0, false)[0];
				if(xAdjustment+RobotMap.pixyShooterDistanceOff<160){
					if(xAdjustment+RobotMap.pixyShooterDistanceOff<140){
					turret.set(.5);
					}else if(xAdjustment+RobotMap.pixyShooterDistanceOff<155){
						turret.set(.25);
					}else{
						turret.set(.15);
					}
				}else{
					if(xAdjustment+RobotMap.pixyShooterDistanceOff<180){
						turret.set(.5);
						}else if(xAdjustment+RobotMap.pixyShooterDistanceOff<165){
							turret.set(.25);
						}else{
							turret.set(.15);

						}				}
			}while(Math.abs(xAdjustment+RobotMap.pixyShooterDistanceOff-160)<RobotMap.pixyShooterDataBuffer);
			turret.set(0);
		}
		//calculates the speed and angle necessary to shoot
		
		//makes the angle adjusment
		
		//adjusts motor speed
		
		//transports balls to be shot
	}
}
