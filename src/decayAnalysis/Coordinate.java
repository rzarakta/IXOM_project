package decayAnalysis;

public class Coordinate {
	
	String time;
	double value;
	
	public Coordinate(String t, double v){
		time = t;
		value = v;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
}
