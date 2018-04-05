package henrysThreshold;

public class effectivePeriod {
	
	/*
	 * defunct code 
	 * 
	 * Author: Henry Coulson 
	 * email: h.coulson@outlook.com
	 * 
	 * 
	 */

	private String startTimeString;
	private String endTimeString;
	
	private double startTimeDouble;
	private double endTimeDouble;
	private double average;
	
	public effectivePeriod(String startTime, String endTime){
		this.startTimeString=startTime;
		this.endTimeString=endTime;
	}
	public effectivePeriod(String startTime, String endTime, int zone){
		this.startTimeString=startTime;
		this.endTimeString=endTime;
	}
	
	public effectivePeriod(double startTime, double endTime){
		this.startTimeDouble=startTime;
		this.endTimeDouble=endTime;
	}
	public effectivePeriod(double startTime, double endTime, int zone){
		this.startTimeDouble=startTime;
		this.endTimeDouble=endTime;
	}
	
	
	public String getStartTime() {
		return startTimeString;
	}
	public void setStartTime(String startTime) {
		this.startTimeString = startTime;
	}
	public String getEndTime() {
		return endTimeString;
	}
	public void setEndTime(String endTime) {
		this.endTimeString = endTime;
	}
	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
	public String getStartTimeString() {
		return startTimeString;
	}
	public void setStartTimeString(String startTimeString) {
		this.startTimeString = startTimeString;
	}
	public String getEndTimeString() {
		return endTimeString;
	}
	public void setEndTimeString(String endTimeString) {
		this.endTimeString = endTimeString;
	}
	public double getStartTimeDouble() {
		return startTimeDouble;
	}
	public void setStartTimeDouble(double startTimeDouble) {
		this.startTimeDouble = startTimeDouble;
	}
	public double getEndTimeDouble() {
		return endTimeDouble;
	}
	public void setEndTimeDouble(double endTimeDouble) {
		this.endTimeDouble = endTimeDouble;
	}
	
	
	
	
}
