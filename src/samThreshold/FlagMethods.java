package samThreshold;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import Read_Data.CSV2Array;
import Read_Data.DataPoint;
import henrysThreshold.metricTaker;

public class FlagMethods {

	
	private static ArrayList<Flag> flags;
	private static ArrayList<DataPoint> data;
	private static ArrayList<Integer> boundaries;
	private static HashMap<Integer,Double> condAverages;
	private static HashMap<Integer,Double> tempAverages;
	private static HashMap<Integer,Double> soilAverages;
	private metricTaker metric;
	private static File input;
	
	
	public FlagMethods(ArrayList<DataPoint> data) {
		
		FlagMethods.data = data;
		flags = new ArrayList<Flag>();
		metric = new metricTaker(data);		
		
	}
	
	public void createFlags(ArrayList<DataPoint> data) {
		
		condAverages = new HashMap<Integer,Double>();
		tempAverages = new HashMap<Integer,Double>();
		soilAverages = new HashMap<Integer,Double>();
		condAverages = metric.getCondAverages();
		tempAverages = metric.getTempAverages();
		soilAverages = metric.getCondAverages();
		
		for (int i = 0; i < 10; i++) {
			int phase = 1;
			Flag flag = new Flag();
			Flag preRinseFlag = new Flag(data.get(metric.getCountsToEffectivePeriod()).getTime(), data.get(boundaries.get(phase)).getTime(),
					phase, data.get(boundaries.get(phase)).getPhase(), "Average Conductivity out of specified range", 0.5, 0.8, 0.0, 0.0, "0.5 - 0.8", condAverages.get(phase));
			
			
			if (boundaries.get(i) == 0) {
				flag.setStartTime(data.get(metric.getCountsToEffectivePeriod()).getTime());
				flag.setEndTime(data.get(boundaries.get(phase)).getTime());
				flag.setCondLower(0.5);
				flag.setCondUpper(0.8);
				flag.setTarget("05 - 0.8");
				
				
				
			}
		}
		
	}
	
	public void applyPhase(ArrayList<DataPoint> data) {
		
		for (DataPoint d: data) {
			
			if (d.getZone() == 1) {
				d.setPhase("Pre-Flush");			
			}
			
			else if (d.getZone() == 2) {
				d.setPhase("Caustic Recirculation");
			}
			
			else if (d.getZone() == 3) {
				d.setPhase("Intermediate Wash");
			}
			
			else if (d.getZone() == 4) {
				d.setPhase("Acid Recirculation");
			}
			
			else if (d.getZone() == 5) {
				d.setPhase("Final Rinse");
			}
		}
	}
	
	public void fixBoundaries() {
		
		boundaries = new ArrayList<Integer>();
		boundaries = metric.getBoundaryIndices();
		System.out.println(boundaries.size());
		System.out.println("Start boundary edit---");
		for (Integer i : boundaries) {
			System.out.println("Index: " + i);
		}
		for (int i = 1; i < boundaries.size(); i++) {
			if (boundaries.get(i) != 0) boundaries.set(i, boundaries.get(i)-1);
			
		}
		for (Integer i : boundaries) {
			System.out.println("New Index: " + i);
		}
		System.out.println("End boundary edit---");
	}
	
	
	public void condThresholdsOld(ArrayList<Flag> flagList) {
		
		HashMap<Integer,Double> condAverages = new HashMap<Integer,Double>();
		condAverages = metric.getCondAverages();
		
		if (condAverages.get(1) < 0.5) {
			
			Flag flag = new Flag(data.get(metric.getCountsToEffectivePeriod()).getTime(), data.get(metric.getBoundaryIndices().get(1)).getTime(), 1, "Pre-Rinse", "Caustic strength too low", "0.5 - 0.8" , condAverages.get(1));
			flagList.add(flag);
		}
		
		if (condAverages.get(1) > 0.8) {
		
			Flag flag = new Flag(data.get(metric.getCountsToEffectivePeriod()).getTime(), data.get(metric.getBoundaryIndices().get(1)).getTime(), 1, "Pre-Rinse", "Caustic strength too high", "0.5 - 0.8", condAverages.get(1));
			flagList.add(flag);
		}
		
		if (condAverages.get(2) < 1.0) {
			
			Flag flag = new Flag(data.get(metric.getBoundaryIndices().get(1) + metric.getCountsToEffectivePeriod()).getTime(), data.get(metric.getBoundaryIndices().get(2)).getTime(), 2, "Caustic Recirculation", "Caustic strength too low", "1.0 - 1.2", condAverages.get(2));
			flagList.add(flag);
		}
		
		if (condAverages.get(2) > 1.2) {
			
			Flag flag = new Flag(data.get(metric.getBoundaryIndices().get(1) + metric.getCountsToEffectivePeriod()).getTime(), data.get(metric.getBoundaryIndices().get(2)).getTime(), 2, "Caustic Recirculation", "Caustic strength too high", "1.0 - 1.2", condAverages.get(2));
			flagList.add(flag);
		}
		
		if (condAverages.get(4) < 0.8) {
			
			Flag flag = new Flag(data.get(metric.getBoundaryIndices().get(3) + metric.getCountsToEffectivePeriod()).getTime(), data.get(metric.getBoundaryIndices().get(4)).getTime(), 4, "Acid Recirculation", "Acidic strength too low", "0.8 - 1.0", condAverages.get(4));
			flagList.add(flag);
		}
		
		if (condAverages.get(4) > 1.0) {
			
			Flag flag = new Flag(data.get(metric.getBoundaryIndices().get(3) + metric.getCountsToEffectivePeriod()).getTime(), data.get(metric.getBoundaryIndices().get(4)).getTime(), 4, "Acid Recirculation", "Acidic strength too high", "0.8 - 1.0", condAverages.get(4));
			flagList.add(flag);
		}
	}
	
public void condThresholds(ArrayList<Flag> flagList) {
		
		HashMap<Integer,Double> condAverages = new HashMap<Integer,Double>();
		condAverages = metric.getCondAverages();
		
		if (condAverages.get(1) < 0.5) {
			
			Flag flag = new Flag(data.get(metric.getCountsToEffectivePeriod()).getTime(), data.get(boundaries.get(1)).getTime(), 1, data.get(boundaries.get(1)).getPhase(), "Caustic strength too low", "0.5 - 0.8" , condAverages.get(1));
			flagList.add(flag);
		}
		
		if (condAverages.get(1) > 0.8) {
		
			Flag flag = new Flag(data.get(metric.getCountsToEffectivePeriod()).getTime(), data.get(boundaries.get(1)).getTime(), 1, data.get(boundaries.get(1)).getPhase(), "Caustic strength too high", "0.5 - 0.8", condAverages.get(1));
			flagList.add(flag);
		}
		
		if (condAverages.get(2) < 1.0) {
			
			Flag flag = new Flag(data.get(boundaries.get(1) + metric.getCountsToEffectivePeriod()).getTime(), data.get(boundaries.get(2)).getTime(), 2, data.get(boundaries.get(2)).getPhase(), "Caustic strength too low", "1.0 - 1.2", condAverages.get(2));
			flagList.add(flag);
		}
		
		if (condAverages.get(2) > 1.2) {
			
			Flag flag = new Flag(data.get(boundaries.get(1) + metric.getCountsToEffectivePeriod()).getTime(), data.get(boundaries.get(2)).getTime(), 2, data.get(boundaries.get(2)).getPhase(), "Caustic strength too high", "1.0 - 1.2", condAverages.get(2));
			flagList.add(flag);
		}
		
		if (condAverages.get(4) < 0.8) {
			
			Flag flag = new Flag(data.get(boundaries.get(3) + metric.getCountsToEffectivePeriod()).getTime(), data.get(boundaries.get(4)).getTime(), 4, data.get(boundaries.get(4)).getPhase(), "Acidic strength too low", "0.8 - 1.0", condAverages.get(4));
			flagList.add(flag);
		}
		
		if (condAverages.get(4) > 1.0) {
			
			Flag flag = new Flag(data.get(boundaries.get(3) + metric.getCountsToEffectivePeriod()).getTime(), data.get(boundaries.get(4)).getTime(), 4, data.get(boundaries.get(4)).getPhase(), "Acidic strength too high", "0.8 - 1.0", condAverages.get(4));
			flagList.add(flag);
		}
		
		
	}
	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		//User must change directory to local in order to test
		input = new File("/home/magratsam/git/cleanfile5.csv");
		CSV2Array c = new CSV2Array();
				
		data = c.populateData(input, data);
				
		FlagMethods fm = new FlagMethods(data);
		fm.applyPhase(data);
		fm.fixBoundaries();
		fm.condThresholdsOld(flags);
		fm.condThresholds(flags);
		
		//Console print of flags
		for(Flag flag: flags) {
			System.out.println();
			System.out.println(flag.print());
		}
		
	}
	
}
