package decayAnalysis;

import java.util.ArrayList;

import Read_Data.DataPoint;
//import timeconverter.TimeConverter;

public class Analyser {
	
	public ArrayList<Coordinate> findSteepestCond(ArrayList<DataPoint> dataPoints){
		
		ArrayList<Coordinate> steepest = new ArrayList<>();
		ArrayList<DataPoint> condList = new ArrayList<>();
		ArrayList<ArrayList<DataPoint>> LOL = new ArrayList<>();
		double gradient = 0;
		
		LOL = findAllDescents(dataPoints);
		
		int oldSize, newSize;
		
		//repeat merging as long as there are two adjacent decreasing sets of DataPoints
		while(true){
			oldSize = LOL.size();
			
			LOL = mergeAdjacentDescents(LOL);
			
			newSize = LOL.size();
			
			if(oldSize == newSize){
				break;
			}
		}
		
		//find the arraylist of DataPoints with the largest decrease in conductivity
		for(ArrayList<DataPoint> a: LOL){
			if(a.size() > 1){
				if(pointToPointGradient(a) >= gradient){
					condList = a;
					gradient = pointToPointGradient(a);
				}
			}
		}
		
		condList = completeZone(condList, dataPoints);
		
		//for the arraylist of DataPoints with the largest decrease in conductivity, construct coordinate objects for each DataPoint and return an arraylist of those coordinates
		for(DataPoint d: condList){
			Coordinate c = new Coordinate(d.getTime(), d.getConductivity());
			
			steepest.add(c);
		}
		
		return steepest;
		
	}
	
	//calculate the rate of decrease of conductivity for an ArrayList of DataPoints
	public double pointToPointGradient(ArrayList<DataPoint> dataPoints){
		//TimeConverter tc = new TimeConverter();
		
		DataPoint x1y1 = dataPoints.get(dataPoints.size() - 1), x2y2 = dataPoints.get(0);
		
		//double x1 = tc.HMSToDec(x1y1.getTime());
		//double x2 = tc.HMSToDec(x2y2.getTime());
		double y1 = x1y1.getConductivity(), y2 = x2y2.getConductivity(), gradient = (y2 - y1);
		
		//double gradient = (y2 - y1)/((x1 - x2)*86400);
		
		return gradient;
	}
	
	//Given the initial array of DataPoints, splits into separate lists by zone
	public ArrayList<ArrayList<DataPoint>> splitByZones(ArrayList<DataPoint> DataPoints){
		ArrayList<ArrayList<DataPoint>> LOC = new ArrayList<ArrayList<DataPoint>>();
		ArrayList<DataPoint> zonePoints = new ArrayList<DataPoint>();
		
		int zone = 1;
		
		for(DataPoint d: DataPoints){
			if(d.getZone() != zone){
				zone++;
				LOC.add(zonePoints);
				zonePoints = new ArrayList<DataPoint>();
			}
			zonePoints.add(d);
		}
		LOC.add(zonePoints);
		
		return LOC;
	}
	
	public ArrayList<ArrayList<DataPoint>> mergeAdjacentDescents(ArrayList<ArrayList<DataPoint>> LOL){
		
		ArrayList<ArrayList<DataPoint>> newLOL = new ArrayList<>();
		ArrayList<DataPoint> adjacent, current;
		int j = LOL.size() - 1;
		
		for(int i = 0; i < j; i++){
			
			current = LOL.get(i);
			adjacent = LOL.get(i+1);
			
			if((current.size() > 1 && adjacent.size() > 1) && (current.get(current.size() - 1).getZone() == adjacent.get(0).getZone())){
				current = mergeArrays(current, adjacent);
				newLOL.add(current);
				i++;
			} else {
				newLOL.add(current);
				if(i == LOL.size() - 2){
					newLOL.add(adjacent);
				}
			}
		}
		
		return newLOL;
	}
	
	//merge two ArrayLists of DataPoints together
	public ArrayList<DataPoint> mergeArrays(ArrayList<DataPoint> a, ArrayList<DataPoint> b){
		
		for(DataPoint d: b){
			a.add(d);
		}
		
		return a;
	}
	
	//Go through a list of DataPoints and make ArrayList of ArrayLists of points with decreasing conductivity values
	public ArrayList<ArrayList<DataPoint>> findAllDescents(ArrayList<DataPoint> DataPoints){
		ArrayList<DataPoint> tempList = new ArrayList<>();
		ArrayList<ArrayList<DataPoint>> LOL = new ArrayList<>();
		
		DataPoint previous = new DataPoint(null, null, 0, Double.MAX_VALUE, 0, 0, 0);
		
		for(DataPoint d: DataPoints){
			if(!((d.getConductivity() <= previous.getConductivity()) && (previous.getZone() == d.getZone()))){
				LOL.add(tempList);
				tempList = new ArrayList<>();
			}
			tempList.add(d);
			previous = d;
		}
		LOL.add(tempList);
		
		return LOL;
	}
	
	//tag on the rest of the points in the same zone as the steepest decrease arraylist
	public ArrayList<DataPoint> completeZone(ArrayList<DataPoint> steepest, ArrayList<DataPoint> dataPoints){
		
		DataPoint oldEnd = steepest.get(steepest.size() - 1);
		boolean complete = false;
		int zone = oldEnd.getZone();
		
		for(DataPoint d: dataPoints){
			if(complete == true){
				if(d.getZone() == zone){
					steepest.add(d);
				} else {
					complete = false;
				}
			}
			
			if(d == oldEnd){
				complete = true;
			}
		}
		
		return steepest;
	}
	
}
