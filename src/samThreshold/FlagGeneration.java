package samThreshold;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import Read_Data.DataPoint;
import henrysThreshold.MetricTaker2;
import henrysThreshold.Phase;

/**
 * @author magratsam
 *
 */

public class FlagGeneration {

	private static ArrayList<DataPoint> data;
	private MetricTaker2 metric;
	private String processName;
	private HashMap<String, ArrayList<Threshold>> processInfo;

	private ArrayList<String> phaseNames = new ArrayList<String>();

	public FlagGeneration(ArrayList<DataPoint> data, String processName, ArrayList<String> phaseNames) {

		FlagGeneration.data = data;
		this.processName = processName;

		FetchThresholds ft = new FetchThresholds();

		try {
			processInfo = ft.setThresholds(System.getProperty("user.dir") + File.separator + "thresholds.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// metric = new metricTaker(data);
		metric = new MetricTaker2(data, phaseNames, 0.5, 60);
	}

	/*
	 * Single method to iterate through data, testing data point metrics against thresholds and generating appropriate flag objects 
	 */
	
	public void thresholds(ArrayList<Flag> flagList) {

		// applyPhase(data);
		HashMap<Integer, Double> condAverages = new HashMap<Integer, Double>();
		HashMap<Integer, Double> tempAverages = new HashMap<Integer, Double>();

		// henry's taint
		// this populates the condAverages and tempAverages, so it can be used
		// as previously done
		for (int i = 0; i < metric.getNumberOfPhases(); i++) {
			tempAverages.put(i + 1, metric.getPhase(i).getTempAverages());
			condAverages.put(i + 1, metric.getPhase(i).getCondAverages());
		}

		// Caustic Prerinse chemical strength lower threshold
		for (Phase p : metric.getPhases()) {
			
			/* Confirms phase name and performs threshold test */
			if ((p.getName().contains("CAUSTIC PRERINSE")) && p.getCondAverages() < processInfo.get(processName).get(0).getCondLower()) {
				
				/*Flag object retrieves appropriate info from data*/
				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(), //Start-time
						data.get(p.getEndIndex()).getTime(), 						 //End-time
						0, 															 //Zone (no longer required)
						p.getName(), 												 //Phase name
						"Chemical strength too low",								 //Warning message
						processInfo.get(processName).get(0).getCondLower() + " - "	 //Target data range
						+ processInfo.get(processName).get(0).getCondUpper(),
						p.getCondAverages());										 //Actual data result

				flag.setType("Conductivity");										 //Sets flag type for graph generator 
				flagList.add(flag);												     //Adds flag to array for use by display class

			}

			// Caustic Prerinse conductivity upper threshold
			if ((p.getName().contains("CAUSTIC PRERINSE"))
					&& p.getCondAverages() > processInfo.get(processName).get(0).getCondUpper()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Chemical strength too high",
						processInfo.get(processName).get(0).getCondLower() + " - "
								+ processInfo.get(processName).get(0).getCondUpper(),
						p.getCondAverages());

				flag.setType("Conductivity");
				flagList.add(flag);

			}

			// Caustic Prerinse temperature lower threshold

			if ((p.getName().contains("CAUSTIC PRERINSE"))
					&& p.getTempAverages() < processInfo.get(processName).get(0).getTempLower()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Average temperature too low",
						processInfo.get(processName).get(0).getTempLower() + " - "
								+ processInfo.get(processName).get(0).getTempUpper(),
						p.getTempAverages());

				flag.setType("Temperature");
				flagList.add(flag);

			}

			// Caustic Prerinse temperature upper threshold

			if ((p.getName().contains("CAUSTIC PRERINSE"))
					&& p.getTempAverages() > processInfo.get(processName).get(0).getTempUpper()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Average temperature too high",
						processInfo.get(processName).get(0).getTempLower() + " - "
								+ processInfo.get(processName).get(0).getTempUpper(),
						p.getTempAverages());

				flag.setType("Temperature");
				flagList.add(flag);

			}

			// Acid Prerinse chemical strength lower threshold

			if ((p.getName().contains("ACID PRERINSE"))
					&& p.getCondAverages() < processInfo.get(processName).get(1).getCondLower()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Chemical strength too low",
						processInfo.get(processName).get(1).getCondLower() + " - "
								+ processInfo.get(processName).get(1).getCondUpper(),
						p.getCondAverages());

				flag.setType("Conductivity");
				flagList.add(flag);

			}

			// Acid Prerinse conductivity upper threshold

			if ((p.getName().contains("ACID PRERINSE"))
					&& p.getCondAverages() > processInfo.get(processName).get(1).getCondUpper()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Chemical strength too high",
						processInfo.get(processName).get(1).getCondLower() + " - "
								+ processInfo.get(processName).get(1).getCondUpper(),
						p.getCondAverages());

				flag.setType("Conductivity");
				flagList.add(flag);

			}

			// //Acid Prerinse temperature lower threshold

			if ((p.getName().contains("CAUSTIC PRERINSE"))
					&& p.getTempAverages() < processInfo.get(processName).get(1).getTempLower()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Average temperature too low",
						processInfo.get(processName).get(1).getTempLower() + " - "
								+ processInfo.get(processName).get(1).getTempUpper(),
						p.getTempAverages());

				flag.setType("Temperature");
				flagList.add(flag);

			}

			// //Acid Prerinse temperature upper threshold

			if ((p.getName().contains("ACID PRERINSE"))
					&& p.getTempAverages() > processInfo.get(processName).get(1).getTempUpper()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Average temperature too high",
						processInfo.get(processName).get(1).getTempLower() + " - "
								+ processInfo.get(processName).get(1).getTempUpper(),
						p.getTempAverages());

				flag.setType("Temperature");
				flagList.add(flag);

			}

			// Caustic Cycle chemical strength lower threshold

			if ((p.getName().contains("CAUSTIC CYCLE"))
					&& p.getCondAverages() < processInfo.get(processName).get(2).getCondLower()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Chemical strength too low",
						processInfo.get(processName).get(2).getCondLower() + " - "
								+ processInfo.get(processName).get(2).getCondUpper(),
						p.getCondAverages());

				flag.setType("Conductivity");
				flagList.add(flag);

			}

			// Caustic Cycle chemical strength upper threshold

			if ((p.getName().contains("CAUSTIC CYCLE"))
					&& p.getCondAverages() > processInfo.get(processName).get(2).getCondUpper()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Chemical strength too high",
						processInfo.get(processName).get(2).getCondLower() + " - "
								+ processInfo.get(processName).get(2).getCondUpper(),
						p.getCondAverages());

				flag.setType("Conductivity");
				flagList.add(flag);

			}

			// Caustic Cycle temperature lower threshold

			if ((p.getName().contains("CAUSTIC CYCLE"))
					&& p.getTempAverages() < processInfo.get(processName).get(2).getTempLower()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Average temperature too low",
						processInfo.get(processName).get(2).getTempLower() + " - "
								+ processInfo.get(processName).get(2).getTempUpper(),
						p.getTempAverages());

				flag.setType("Temperature");
				flagList.add(flag);

			}

			// Caustic Cycle temperature upper threshold

			if ((p.getName().contains("CAUSTIC CYCLE"))
					&& p.getTempAverages() > processInfo.get(processName).get(2).getTempUpper()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Average temperature too high",
						processInfo.get(processName).get(2).getTempLower() + " - "
								+ processInfo.get(processName).get(2).getTempUpper(),
						p.getTempAverages());

				flag.setType("Temperature");
				flagList.add(flag);

			}

			// Acid Cycle chemical strength lower threshold

			if ((p.getName().contains("ACID CYCLE"))
					&& p.getCondAverages() < processInfo.get(processName).get(3).getCondLower()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Chemical strength too low",
						processInfo.get(processName).get(3).getCondLower() + " - "
								+ processInfo.get(processName).get(3).getCondUpper(),
						p.getCondAverages());

				flag.setType("Conductivity");
				flagList.add(flag);

			}

			// Acid Cycle chemical strength upper threshold

			if ((p.getName().contains("ACID CYCLE"))
					&& p.getCondAverages() > processInfo.get(processName).get(3).getCondUpper()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Chemical strength too high",
						processInfo.get(processName).get(3).getCondLower() + " - "
								+ processInfo.get(processName).get(3).getCondUpper(),
						p.getCondAverages());

				flag.setType("Conductivity");
				flagList.add(flag);

			}

			// Acid Cycle temperature lower threshold

			if ((p.getName().contains("ACID CYCLE"))
					&& p.getTempAverages() < processInfo.get(processName).get(3).getTempLower()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Average temperature too low",
						processInfo.get(processName).get(3).getTempLower() + " - "
								+ processInfo.get(processName).get(3).getTempUpper(),
						p.getTempAverages());

				flag.setType("Temperature");
				flagList.add(flag);

			}

			// Acid Cycle temperature upper threshold

			if ((p.getName().contains("ACID CYCLE"))
					&& p.getTempAverages() > processInfo.get(processName).get(3).getTempUpper()) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Average temperature too high",
						processInfo.get(processName).get(3).getTempLower() + " - "
								+ processInfo.get(processName).get(3).getTempUpper(),
						p.getTempAverages());

				flag.setType("Temperature");
				flagList.add(flag);

			}

			// Zero conductivity test at end of rinse phase

			if ((p.getName().contains("INTERMEDIATE RINSE") || p.getName().contains("FINAL RINSE"))
					&& data.get(p.getEndIndex()).getConductivity() != 0) {

				Flag flag = new Flag(data.get(p.getEffectiveStartIndex()).getTime(),
						data.get(p.getEndIndex()).getTime(), 0, p.getName(), "Conductivity Non-Zero at Rinse End",
						"0.0", data.get(p.getEndIndex()).getConductivity());

				flag.setType("Conductivity");

				flagList.add(flag);
			}
		}
	}

	// Applies phase labels to each data point
	public void applyPhase(ArrayList<DataPoint> data) {

		int dataSize = data.size();
		if (dataSize > 0) {
			for (int i = 0; i < data.size(); i++) {
				DataPoint d = data.get(i);
				d.setPhase(phaseNames.get(d.getZone() - 1));

			}
		}

	}

	public void setPhaseNames(ArrayList<String> phaseNames) {
		this.phaseNames = phaseNames;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

}
