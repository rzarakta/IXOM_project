package samThreshold;

import java.util.ArrayList;
import java.util.HashMap;

public class Threshold {
	
	private String phase;
	private double condLower;
	private double condUpper;
	private double tempLower;
	private double tempUpper;
	
	public String getPhase() {
		return phase;
	}


	public void setPhase(String phase) {
		this.phase = phase;
	}


	public double getCondLower() {
		return condLower;
	}


	public void setCondLower(double condLower) {
		this.condLower = condLower;
	}


	public double getCondUpper() {
		return condUpper;
	}


	public void setCondUpper(double condUpper) {
		this.condUpper = condUpper;
	}


	public double getTempLower() {
		return tempLower;
	}


	public void setTempLower(double tempLower) {
		this.tempLower = tempLower;
	}


	public double getTempUpper() {
		return tempUpper;
	}


	public void setTempUpper(double tempUpper) {
		this.tempUpper = tempUpper;
	}
	
	public Threshold(String phase, double condLower, double condUpper, double tempLower, double tempUpper) {
		super();
		this.phase = phase;
		this.condLower = condLower;
		this.condUpper = condUpper;
		this.tempLower = tempLower;
		this.tempUpper = tempUpper;
	}
	
	public Threshold() {
		this.phase = "";
		this.condLower = 0;
		this.condUpper = 0;
		this.tempLower = 0;
		this.tempUpper = 0;
	}
	
	
	public String printThresholds() {
		
		return "|" + phase + "|" + condLower + "|" + condUpper + "|" + tempLower +"|" + tempUpper + "|";
	}
	
}
