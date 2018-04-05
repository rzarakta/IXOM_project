package Read_Data;

import java.util.ArrayList;
import java.util.HashMap;

public class DataPoint {

	static HashMap<DataPoint, String> map = new HashMap<DataPoint, String>();

	private String date;
	private String time;
	private double turbidity;
	private double conductivity;
	private double soil;
	private String phase;
	private double temp;
	private int zone;

	public DataPoint(String date, String time, double turbidity, double conductivity, double soil, double temp, int zone) {

		this.date = date;
		this.time = time;
		this.turbidity = turbidity;
		this.conductivity = conductivity;
		this.soil = soil;
		this.temp = temp;
		this.zone = zone;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public double getTurbidity() {
		return turbidity;
	}
	public void setTurbidity(double turbidity) {
		this.turbidity = turbidity;
	}
	public double getConductivity() {
		return conductivity;
	}
	public void setConductivity(double conductivity) {
		this.conductivity = conductivity;
	}
	public double getSoil() {
		return soil;
	}
	public void setSoil(double soil) {
		this.soil = soil;
	}
	public double getTemp() {
		return temp;
	}
	public void setTemp(double temp) {
		this.temp = temp;
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

	public String print(){

		return "| " + date + " | " + time + " | " + turbidity + " | " + conductivity + " | " + soil + " | " + temp + " | " +  zone + " | \n"; 

	}
	
	public static void setMap(ArrayList<DataPoint> data, ArrayList<String> phases){
		int zone = 1, phaseIndex = 0;

		for(DataPoint d: data){
			if(d.getZone() != zone){
				zone++;
				phaseIndex++;
			}
			map.put(d, phases.get(phaseIndex));
		}
	}
	
	public static HashMap<DataPoint, String> getMap(){
		return map;
	}

}
