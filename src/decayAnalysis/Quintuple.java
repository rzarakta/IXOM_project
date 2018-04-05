package decayAnalysis;

public class Quintuple {

	double A;
	double B;
	double rSquared;
	String startTime;
	String endTime;
	
	public Quintuple(double x, double y, double z, String v, String w){
		A = x;
		B = y;
		rSquared = z;
		startTime = v;
		endTime = w;
	}

	public double getA() {
		return A;
	}

	public void setA(double a) {
		A = a;
	}

	public double getB() {
		return B;
	}

	public void setB(double b) {
		B = b;
	}

	public double getrSquared() {
		return rSquared;
	}

	public void setrSquared(double rSquared) {
		this.rSquared = rSquared;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
