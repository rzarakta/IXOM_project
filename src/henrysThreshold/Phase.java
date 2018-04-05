package henrysThreshold;

import java.util.ArrayList;
import java.util.HashMap;

import Read_Data.DataPoint;

public class Phase {

	private int startIndex;
	private int effectiveStartIndex;
	private int endIndex;
	private String name;
	private double tempAverages;
	private double condAverages;
	private double soilAverages;
	private ArrayList<DataPoint> phaseData;
	private ArrayList<DataPoint> effPeriodData;
	private boolean isRinse=false;
	private boolean isPreRinse=false;
	
	public Phase(String name){
		this.name=name;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEffectiveStartIndex() {
		return effectiveStartIndex;
	}

	public void setEffectiveStartIndex(int effectiveStartIndex) {
		this.effectiveStartIndex = effectiveStartIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTempAverages() {
		return tempAverages;
	}

	public void setTempAverages(double tempAverages) {
		this.tempAverages = tempAverages;
	}

	public double getCondAverages() {
		return condAverages;
	}

	public void setCondAverages(double condAverages) {
		this.condAverages = condAverages;
	}

	public double getSoilAverages() {
		return soilAverages;
	}

	public void setSoilAverages(double soilAverages) {
		this.soilAverages = soilAverages;
	}

	public ArrayList<DataPoint> getPhaseData() {
		return phaseData;
	}

	public void setPhaseData(ArrayList<DataPoint> phaseData) {
		this.phaseData = phaseData;
	}

	
	public ArrayList<DataPoint> getEffPeriodData() {
		return effPeriodData;
	}

	public void setEffPeriodData(ArrayList<DataPoint> effPeriodData) {
		this.effPeriodData = effPeriodData;
	}


	public boolean getIsRinse() {
		return isRinse;
	}

	public void setIsRinse(boolean isRinse) {
		this.isRinse = isRinse;
	}

	public boolean isPreRinse() {
		return isPreRinse;
	}

	public void setPreRinse(boolean isPreRinse) {
		this.isPreRinse = isPreRinse;
	}
	
	
}
