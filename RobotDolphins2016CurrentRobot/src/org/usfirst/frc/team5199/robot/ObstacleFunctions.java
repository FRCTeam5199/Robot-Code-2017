package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class ObstacleFunctions {
		static boolean Active = false;
		public static void ChivalDeFrise(Talon leftBack, Talon leftFront, 
				Talon rightBack, Talon rightFront, Solenoid flow1, Solenoid direc1){
				 //double time1, double time2, double time3, double time4){
			Active = true;
			while(Active){
			leftBack.set(.2);
			rightBack.set(-.2);
			leftFront.set(.2);
			rightFront.set(-.2);
			Timer.delay(.1);
			flow1.set(true);
			direc1.set(true);
			Timer.delay(.9);
			leftBack.set(.4);
			rightBack.set(-.4);
			leftFront.set(.4);
			rightFront.set(-.4);
			Timer.delay(.2);
			direc1.set(false);
			Timer.delay(.4);
			leftBack.set(1);
			rightBack.set(-1);
			leftFront.set(1);
			rightFront.set(-1);
			Timer.delay(1.2);
			leftBack.stopMotor();
			leftFront.stopMotor();
			rightBack.stopMotor();
			rightFront.stopMotor();
			Active = false;
			}

            
		}
		public static void Drawbridge(Talon leftBack, Talon leftFront, 
				Talon rightBack, Talon rightFront, Solenoid flow1, Solenoid direc1, Solenoid flow2, Solenoid direc2){
			Active = true;
			while(Active){
			flow2.set(true);
			direc2.set(true);
			Timer.delay(3);
			flow1.set(true);
			direc1.set(true);
			Timer.delay(1);
			flow2.set(true);
			direc2.set(false);
			flow1.set(true);
			direc1.set(false);
			Active = false;
			}
		
            
		}

		public static void RoughTerrainAuto(Talon leftBack, Talon leftFront, 
				Talon rightBack, Talon rightFront, DoubleSolenoid ballCyl){
			boolean autoSthap = false;
			if (autoSthap==false){
				ballCyl.set(DoubleSolenoid.Value.kReverse);
				Timer.delay(.5);
				leftBack.set(1);
				rightBack.set(-1);
				leftFront.set(1);
				rightFront.set(-1);
				Timer.delay(1.75);
				leftBack.set(-.1);
				rightBack.set(.1);
				leftFront.set(-.1);
				rightFront.set(.1);
				Timer.delay(.5);
				leftBack.set(-1);
				rightBack.set(1);
				leftFront.set(-1);
				rightFront.set(1);
				Timer.delay(1.75);
				leftBack.set(.1);
				rightBack.set(-.1);
				leftFront.set(.1);
				rightFront.set(-.1);
				Timer.delay(.5);
				leftBack.set(1);
				rightBack.set(-1);
				leftFront.set(1);
				rightFront.set(-1);
				Timer.delay(1.75);
				leftBack.set(-.1);
				rightBack.set(.1);
				leftFront.set(-.1);
				rightFront.set(.1);
				autoSthap=true;
		}
		}
}
