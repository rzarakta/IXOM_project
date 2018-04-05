package henrysThreshold;

import Read_Data.*;
import timeconverter.*;
import java.util.ArrayList;
import java.util.HashMap;

public class metricTaker {

	/**
	 * 
	 * 
	 * Author: Henry Coulson email: h.coulson@outlook.com
	 * 
	 * 
	 */

	private ArrayList<DataPoint> data = new ArrayList<DataPoint>();
	private ArrayList<Integer> boundaryIndices = new ArrayList<Integer>();
	private HashMap<Integer, ArrayList<DataPoint>> effectivePeriods = new HashMap<Integer, ArrayList<DataPoint>>();
	private double sampleRate;
	private int minsIncrement;
	private int countsToEffectivePeriod;
	private HashMap<Integer, Double> tempAverages = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> condAverages = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> soilAverages = new HashMap<Integer, Double>();

	/**
	 * this object generates the relevant metrics for a set of data. this is
	 * where most of the data processing will be
	 * 
	 * @param data
	 */

	public metricTaker(ArrayList<DataPoint> data) {
		// assigns the data to the field
		this.data = data;
		/*
		 * these methods are fairly self explanatory. However, worth noting is
		 * that fact that the effective periods were taken to be 7 minutes in
		 * from the start of a new boundary, to enable the new conditions to
		 * take effect.
		 * 
		 */
		findAverageSampleRate();
		/*edited minutes to increments by passing fraction of CIP duration rather than fixed value
		*in order to resolve conflicts in shorter processes. Declared minsIncrememnt field and instantiated it
		*in findAverageSampleRate method
		*
		* - Sam
		*/
		
		minutesToIncrements(minsIncrement);
		assignBoundaryIndices();
		createEffectivePeriods();
		CalculateAverage();

	}

	private void minutesToIncrements(int i) {
		// the sample rate might change on the wizard, so this is just to find
		// out what it will be in any case
		countsToEffectivePeriod = (int) ((((double) i * 60) / 86400) / (sampleRate));
		
	}

	/**
	 * goes through the array of effective periods, and calculates some
	 * averages.
	 */
	private void CalculateAverage() {
		double temptot = 0;
		double condtot = 0;
		double soiltot = 0;
		int counts = 0;

		for (Integer key : effectivePeriods.keySet()) {
			ArrayList<DataPoint> pointsOfData = effectivePeriods.get(key);

			for (DataPoint dp : pointsOfData) {
				temptot += dp.getTemp();
				condtot += dp.getConductivity();
				soiltot += dp.getSoil();
				counts++;

			}

			tempAverages.put(key, temptot / counts);

			condAverages.put(key, condtot / counts);
			soilAverages.put(key, soiltot / counts);

		}

	}

	/**
	 * this is a complicated method. for each member of each phase, it adds the
	 * data points from 7 minutes in, onward, to the end.
	 */

	private void createEffectivePeriods() {
		int counter = 1;

		for (int i = 0; i < boundaryIndices.size() - 1; i++) {

			ArrayList<DataPoint> effectiveTime = new ArrayList<DataPoint>();

			// need to start at the beginning of the effective period
			for (int j = boundaryIndices.get(i) + countsToEffectivePeriod; j < boundaryIndices.get(i + 1); j++) {

				effectiveTime.add(data.get(j));
			}

			effectivePeriods.put(counter, effectiveTime);

			counter++;
		}

	}

	/**
	 * toggles through, and finds the indices where the boundaries of the phases
	 * are
	 * 
	 */

	private void assignBoundaryIndices() {

		boundaryIndices.add(0);

		for (int i = 1; i < data.size(); i++) {
			DataPoint p = data.get(i);
			DataPoint q = data.get(i - 1);
			if (p.getZone() != (q.getZone())) {
				boundaryIndices.add(i);
			}

		}

	}
	
	/**
	 * finds the average sample rate
	 * 
	 * also instantiates minsIncrement, converting fraction of total CIP duration from HMS to decimal and then rounding and converting to int 
	 * for use in minsToIncrement method - Sam
	 */

	private void findAverageSampleRate() {
		TimeConverter tc = new TimeConverter();
	
		sampleRate = (tc.HMSToDec(data.get(data.size() - 1).getTime()) - tc.HMSToDec(data.get(0).getTime()))/ data.size();
		
		//SAM'S ADDITION
		double CIPduration = (tc.HMSToDec(data.get(data.size() - 1).getTime()) - tc.HMSToDec(data.get(0).getTime()));
		CIPduration = (CIPduration*0.04) * 1440;
		minsIncrement = (int) Math.round(CIPduration);

	}

	public HashMap<Integer, ArrayList<DataPoint>> getEffectivePeriods() {
		return effectivePeriods;
	}

	public void setEffectivePeriods(HashMap<Integer, ArrayList<DataPoint>> effectivePeriods) {
		this.effectivePeriods = effectivePeriods;
	}

	public HashMap<Integer, Double> getTempAverages() {
		return tempAverages;
	}

	public void setTempAverages(HashMap<Integer, Double> tempAverages) {
		this.tempAverages = tempAverages;
	}

	public HashMap<Integer, Double> getCondAverages() {
		return condAverages;
	}

	public void setCondAverages(HashMap<Integer, Double> condAverages) {
		this.condAverages = condAverages;
	}

	public HashMap<Integer, Double> getSoilAverages() {
		return soilAverages;
	}

	public void setSoilAverages(HashMap<Integer, Double> soilAverages) {
		this.soilAverages = soilAverages;
	}

	public ArrayList<Integer> getBoundaryIndices() {
		return boundaryIndices;
	}

	public void setBoundaryIndices(ArrayList<Integer> boundaryIndices) {
		this.boundaryIndices = boundaryIndices;
	}

	public double getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(double sampleRate) {
		this.sampleRate = sampleRate;
	}

	public int getCountsToEffectivePeriod() {
		return countsToEffectivePeriod;
	}

	public void setCountsToEffectivePeriod(int countsToEffectivePeriod) {
		this.countsToEffectivePeriod = countsToEffectivePeriod;
	}

}
