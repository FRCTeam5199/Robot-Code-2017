package org.usfirst.frc.team5199.robot;

public class CircularAverageBuffer {
	private  double sumBuffer;
	private  int bufferSize;
	private  double[] dataArray;
	private  int counter;
	private  boolean firstRun;
	private  double result;
	public CircularAverageBuffer(int bufferArraySize){
		bufferSize = bufferArraySize;
		sumBuffer = 0;
		counter = 0;
		result =0; 
		firstRun = true;
		dataArray = new double[bufferSize];
		for(int i =0; i< bufferSize; i++){
			dataArray[i] = 0.0;
		}
	}
	public double DataAverage(double data) {
		//takes the data as a parameter and uses the circular buffer
		//it then calculates and returns the average
		if (counter== bufferSize) {
			firstRun = false;
			counter = 0;
		}
		
		sumBuffer += data - dataArray[counter];

		dataArray[counter] = data;
		
		

		if (firstRun) {
			result = sumBuffer / counter;

		} else {
			result = sumBuffer / bufferSize;
		}
		counter++;
		return (result);
	}
	public double getAverage(){
		//This function should only be called after the 
		//data average function has been called at least one time
		return result;
	}
}
