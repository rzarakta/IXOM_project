package samThreshold;

import java.text.DecimalFormat;
import java.util.ArrayList;

import timeconverter.TimeConverter;

public class Flag {
	
	private String date;
	private String startTime;
	private String endTime;
	private int zone;
	private String phase;
	private String message;
	private String target;
	private double actual;
	private double condLower;
	private double condUpper;
	private double tempLower;
	private double tempUpper;
	private String type;
	private double durationSeconds;
	private String durationLabel;
	
	private final static TimeConverter timeConverter = new TimeConverter();

	public Flag() {
		
		date = "";
		startTime = "";
		endTime = "";
		zone = 0;
		phase = "";
		message = "";
		target = "";
		type = "";
		actual = 0.0;
		tempUpper = 0.0;
		tempLower = 0.0;
		condUpper = 0.0;
		condLower = 0.0;
		
	}
	


	public Flag(String startTime, String endTime, int zone, String phase, String message,String target, double actual) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.zone = zone;
		this.phase = phase;
		this.message = message;
		this.target = target;
		this.actual = Math.round(actual*1000.0)/1000.0;
		setDurationSeconds();
		setDurationLabel();
	}
	
	public Flag(String startTime, String endTime, int zone, String phase, String message, double condLower, double condUpper, double tempLower, double tempUpper, String target, double actual) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.zone = zone;
		this.phase = phase;
		this.message = message;
		this.target = target;
		this.actual = Math.round(actual*1000.0)/1000.0;
		setDurationSeconds();
		setDurationLabel();
	}

	public String print() {
		return "| " + startTime + " | " + endTime + " | " + phase + " | " + message + " | " + target + " | " + actual + " | \n"; 
	}
	
	public double getDurationSeconds() {
		return durationSeconds;
	}

	public void setDurationSeconds() {
		
		this.durationSeconds = Math.floor(((timeConverter.HMSToDec(this.endTime) * 86400) - (timeConverter.HMSToDec(this.startTime) * 86400))* 100) / 100 ;
		
	}
	public String getDurationLabel() {
		return durationLabel;
	}

	public void setDurationLabel() {
		
		this.durationLabel = timeConverter.decToHMS(this.durationSeconds / 86400) ;
		
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public int getZone() {
		return zone;
	}

	public void setZone(int zone) {
		this.zone = zone;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public double getActual() {
		return actual;
	}

	public void setActual(double actual) {
		this.actual = actual;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public double getTempUpper() {
		return tempUpper;
	}

	public void setTempUpper(double tempUpper) {
		this.tempUpper = tempUpper;
	}

	public double getTempLower() {
		return tempLower;
	}

	public void setTempLower(double tempLower) {
		this.tempLower = tempLower;
	}

	public double getCondUpper() {
		return condUpper;
	}

	public void setCondUpper(double condUpper) {
		this.condUpper = condUpper;
	}

	public double getCondLower() {
		return condLower;
	}

	public void setCondLower(double condLower) {
		this.condLower = condLower;
	}
	
}
