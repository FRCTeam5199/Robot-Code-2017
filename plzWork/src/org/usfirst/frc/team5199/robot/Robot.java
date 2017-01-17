package org.usfirst.frc.team5199.robot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.vision.AxisCamera;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive class.
 */
@SuppressWarnings("unused") // Suppress warning for unused imports
public class Robot extends SampleRobot {
	NewPixy pixyCam = new NewPixy(); // Pixy.java is Kevin's, NewPixy.java is Kyle's.
	
	int frameCnt;
	
	public Robot() {
		
		
		
	}




	@SuppressWarnings("deprecation")
	public void operatorControl() {
		
	

		while (isOperatorControl() && isEnabled()) {
			
		
			Block currentData = pixyCam.getDataBlock();
			if (!pixyCam.isFrameUpdate()){
				frameCnt++;
			}
			SmartDashboard.putNumber("Pixy?", frameCnt);
			if(Block.isValidPacket(currentData)) {
				// Only update the Smart Dashboard if a valid packet is supplied.
			SmartDashboard.putString("Pixy Cam Data", pixyCam.printBlockData(currentData));
				    
			

				SmartDashboard.putBoolean("isFrameUpdate", pixyCam.isFrameUpdate());
			}
		
			
			       }

			}
}