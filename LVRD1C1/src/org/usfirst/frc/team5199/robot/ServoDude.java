package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Servo;

public class ServoDude {
	//New class for controlling servos 
	public static Servo servo;
	public static double increment = 0.002;
	public static double position = 0.0;
	public static double tolerance = 0.001;
	
	public ServoDude(Servo giveNRG){
		//Constructor; sets the servoDude to inputed servo
		servo = giveNRG;
	}
	
	public static void set(double value){
		//sets to an inputed value
		servo.set(value);
	}
	
	public void increment(double input){
		//increments up or down depending on the input value
		//Used with the joystick for more manual control over the servo
		//when using the joystick, up on the Y-axis is negative, hence the first 
		//if statement increments the servo forward when the stick is held forward
		if (input < - tolerance) {
			position = position + increment;
			if (position > 1.0) {
				position = 1.0;
			}
		}
		if (input > tolerance) {
			position = position - increment;
			if (position < 0.0) {
				position = 0.0;
			}
		}
		servo.set(position);
	}
}