package decayAnalysis;

import java.util.ArrayList;

import timeconverter.*;



public class RegressionAndParameters {
	double A, tau, RSquared; // A in units turbidity or conductivity. tau in units of seconds.
	
	public RegressionAndParameters(ArrayList<Coordinate> data){
		//y   = b   +     ax
		//lnT = lnA - 1/tau * t
		
		double[] y = new double[data.size()] , x = new double[data.size()];
		TimeConverter tc = new TimeConverter();
		double start = tc.HMSToDec(data.get(0).getTime())*86400;
		
		for (int i=0;i<data.size();i++){
			x[i]= tc.HMSToDec(data.get(i).getTime())*86400 - start;
			y[i]=Math.log((data.get(i).getValue()));
		}
		double n = (double) x.length;
		double b = ( mean(y)*sum(vmult(x, x))-mean(x)*sum(vmult(x,y)) ) / ( n*sum(vmult(x,x)) - sum(x)*sum(x) );
		double a = ( sum(vmult(x,y)) - n*mean(x)*mean(y) ) / ( sum(vmult(x,x)) -n*mean(x)*mean(x) );
		
		A = Math.exp(b);
		tau=-1/a;
		
		
		
		RSquared = calculateRSquared(x,y,a,b);
		
		
	}
	
	
	//Jacob sticking his nose where it doesn't belong. Alternate method to Henry's code above.
	//Returns A and B to fit a functional form y = Aexp(Bx) as well as rSquared value for the curve
	public Quintuple leastSquaresFitting(ArrayList<Coordinate> data){
		
		TimeConverter tc = new TimeConverter();
		int n = 0;
		double start = tc.HMSToDec(data.get(0).getTime())*86400, value;
		Coordinate cd;
		
		for(Coordinate xy: data){
			if(xy.getValue() != 0.0){
				n++;
			}
		}
		
		double[] x = new double[n], y = new double[n];
		for(int i = 0; i < n; i++){
			cd = data.get(i);
			
			value = cd.getValue();
			if(value != 0.0){
				x[i] = tc.HMSToDec(cd.getTime())*86400 - start;
				y[i] = Math.log(value);
			}
			
		}
		
		double a = ((sum(y)*sum(vmult(x, x))) - (sum(x)*sum(vmult(x, y)))) / ((n*sum(vmult(x, x))) - (sum(x)*sum(x)));
		double b = ((n*sum(vmult(x, y))) - (sum(x)*sum(y))) / ((n*sum(vmult(x, x))) - (sum(x)*sum(x)));
		double rSq = calculateRSquared(x, y, b, a);
		
		Quintuple toReturn = new Quintuple(Math.exp(a), b, rSq, data.get(0).getTime(), data.get(data.size()-1).getTime());
		
		return toReturn;
		
	}
	
	
	private double calculateRSquared(double[] x, double[] y, double a, double b) {
		double ssres=0, sstot=0, ymean=mean(y);
		
		for (int i=0; i<x.length;i++){
			ssres+=(y[i]-a*x[i]-b) * (y[i]-a*x[i]-b);
		}
		
		for(int i=0;i<y.length;i++){
			sstot+=(y[i]-ymean)*(y[i]-ymean);
		}
		
		return(1-(ssres/sstot));
	}

	private double sum(double[] vals){
		double total=0;
		for (double v:vals){
			total+=v;
			
		}
		return total;
	}
	
	private double mean(double[] vals){
		double sum = sum(vals);
		return sum/(vals.length);
	}
	
	private double[] vmult(double[] x, double[] y){
		double[] array = new double[x.length];
		for (int i=0;i<x.length;i++){
			array[i]=x[i]*y[i];
		}
		return array;
	}
	
	
}
