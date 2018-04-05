package henrysThreshold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import Read_Data.*;

public class MetricTaker2 {

	private ArrayList<Phase> phases= new ArrayList<Phase>();
	private ArrayList<DataPoint> data;
	private int NumberOfPhases;
	private double minCond; 
	private double minTemp;

	public MetricTaker2(ArrayList<DataPoint> data, ArrayList<String> phaseNames, double minCond, double minTemp) {

		
		for (String s : phaseNames) {
			phases.add(new Phase(s));
			
		}
		
		
		this.NumberOfPhases=phaseNames.size();
		this.minCond=minCond;
		this.minTemp=minTemp;
		this.data = data;

		assignBoundaryIndicesAndDataToPhases();
		ascribePhaseType();
		calculateAverages();
	}

	private void assignBoundaryIndicesAndDataToPhases() {
		int phasecount = 0;
		ArrayList<DataPoint> temp = new ArrayList<DataPoint>(100);
		phases.get(phasecount).setStartIndex(0);
		//System.out.println(System.getProperty("os.name"));
		//
		
		DataPoint prevDataPoint = data.get(0);
		for (int i=0;i<data.size();i++) {
			DataPoint d = data.get(i);
			
			if(i==data.size()-1){
				phases.get(phasecount).setEndIndex(i);
				phases.get(phasecount).setPhaseData(temp);
			}
			
			else if (d.getZone() != prevDataPoint.getZone()) {
				phases.get(phasecount).setPhaseData(temp);
				phases.get(phasecount).setEndIndex(i-1);
				phasecount++;
				phases.get(phasecount).setStartIndex(i);
				temp = new ArrayList<DataPoint>(100);
			}
			else {
				temp.add(d);
			}
			prevDataPoint = d;
		}
		
		
	}

	
	private void ascribePhaseType() {
		
		
		for(int i=0;i<phases.size();i++){
			Phase p = phases.get(i);
			if(p.getName().contains("PRERINSE")){
				p.setPreRinse(true);
				p.setEffectiveStartIndex(p.getEffectiveStartIndex());
				
			}
			else if (p.getName().contains("RINSE")){
				p.setIsRinse(true);
				p.setEffectiveStartIndex(p.getEffectiveStartIndex());
				
			}
			else{
				p.setIsRinse(false);
				
			}
			
			setEffectivePeriodDetails(p);
		}
	}

	

	

	private void setEffectivePeriodDetails(Phase p) {
		// this might need reevaluating
		int indexcount = p.getStartIndex();
		int startIndex=-1;
		ArrayList<DataPoint> temp = new ArrayList<DataPoint>(100);
		for(DataPoint d : p.getPhaseData()){
			if(d.getConductivity()>minCond && d.getTemp()>minTemp){
				if(startIndex==-1){
					startIndex=indexcount;
					
				}
				temp.add(d);
			}
			indexcount++;
		}
		
		if(startIndex==-1){
			p.setEffectiveStartIndex(p.getStartIndex());
			p.setEffPeriodData(p.getPhaseData());
			
		}else{
			p.setEffectiveStartIndex(startIndex);
			p.setEffPeriodData(temp);
			
		}
		
		
		
	}

	private void calculateAverages() {
		for (int i=0;i<phases.size();i++){
			Phase p = phases.get(i);
			double[] avs = findAverages(p.getEffPeriodData());
			p.setTempAverages(avs[0]);
			p.setCondAverages(avs[1]);
			p.setSoilAverages(avs[2]);
		}

	}

	private double[] findAverages(ArrayList<DataPoint> data) {
		double condtot =0;
		double soiltot =0;
		double temptot =0;
		double[] ret = new double[3];
		
		for(DataPoint d : data){
			temptot+=d.getTemp();
			condtot+=d.getConductivity();
			soiltot+=d.getSoil();
		}
		ret[0]=temptot/data.size();
		ret[1]=condtot/data.size();
		ret[2]=soiltot/data.size();
		return ret;
		
		
	}

	
	public Phase getPhase(int i) {
		return phases.get(i);
	}

	public int getNumberOfPhases() {
		return NumberOfPhases;
	}

	public void setNumberOfPhases(int numberOfPhases) {
		NumberOfPhases = numberOfPhases;
	}
	
	public int getIndexOfEffectivePeriod(String phasename){
		int garry = -1;
		for(Phase p : phases){
			if(p.getName().contains(phasename)){
				garry = p.getEffectiveStartIndex();
			}
		}
		return garry;
	}

	public ArrayList<Phase> getPhases() {
		return phases;
	}

	public void setPhases(ArrayList<Phase> phases) {
		this.phases = phases;
	}
	
	

}
