package samThreshold;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FetchThresholds {

	private static ArrayList<String> processes = new ArrayList<>();
	private static HashMap<String, ArrayList<Threshold>> powders = new HashMap<String, ArrayList<Threshold>>();
	
	//Method for generating, populating and returning HashMap containing thresholds for use by Flag Generation class
	public HashMap<String, ArrayList<Threshold>> setThresholds(String input) throws IOException {

		String processName = "";

		ArrayList<Threshold> thresholds = new ArrayList<>();

		File excel = new File(input);

		// Converts first line of CSV file to string. String is then scanned and
		// each token is used to set phase names of newly generated Threshold
		// objects,
		// which are then added to a threshold arrayList
		BufferedReader brTest = new BufferedReader(
				new FileReader(System.getProperty("user.dir") + File.separator + "thresholds.csv"));

		String text = brTest.readLine();
		brTest.close();

		text = removeCommas(text);
		Scanner pScan = new Scanner(text);
		pScan.useDelimiter(",");
		while (pScan.hasNext()) {

			Threshold t = new Threshold();
			t.setPhase(pScan.next());
			thresholds.add(t);
		}
		pScan.close();

		// Scanner initialised, scans CSV file and populates processes arrayList
		// with the name of each process, which is
		// then used as reference for populating hashmap
		Scanner nScan = new Scanner(excel);
		nScan.useDelimiter(",");
		nScan.nextLine();
		int counter = 1;
		while (nScan.hasNextLine()) {

			if ((counter - 2) % 18 == 0) {
				processName = nScan.next();
				processes.add(processName);
			} else {
				nScan.next();
			}
			counter++;

		}
		nScan.close();

		// CSV file is scanned process name matches in the process name
		// arrayList, when a match is found,
		// the threshold array is populated with the associated threshold values
		// pertaining to that process
		for (String s : processes) {
			Scanner tScan = new Scanner(excel);
			tScan.useDelimiter(",");

			while (tScan.hasNextLine()) {

				if (tScan.next().equals(s)) {

					ArrayList<Threshold> tempThresholds = new ArrayList<Threshold>();
					for (int i = 0; i < thresholds.size(); i++) {
						Threshold nt = new Threshold();
						nt.setPhase(thresholds.get(i).getPhase());
						tempThresholds.add(nt);
					}
					for (Threshold t : tempThresholds) {

						t.setCondLower(tScan.nextDouble());
						t.setCondUpper(tScan.nextDouble());
						t.setTempLower(tScan.nextDouble());
						t.setTempUpper(tScan.nextDouble());
						// System.out.println(t.printThresholds());

					}
					System.out.println();
					// System.out.println(tempThresholds);
					powders.put(s, tempThresholds);

				}

			}
			tScan.close();
		}

		// print function for validating hashmap population
		// for (String s : powders.keySet()) {
		//
		// String key = s.toString();
		//
		// System.out.println(key + " " +
		// powders.get(s).get(0).printThresholds());
		//
		// }

		return powders;

	}
	
	//Jacob's addition - method for cleaning unncessary commas from uploaded threshold csv file, ensuring accurate population of HashMap
	public String removeCommas(String toClean) {
		Boolean keepComma = false;
		String toReturn = "";
		int length = toClean.length();
		char c;

		if (length > 0) {
			for (int i = 0; i < length; i++) {
				c = toClean.charAt(i);
				if (c != ',') {
					toReturn += c;
					keepComma = true;
				} else if (keepComma == true) {
					toReturn += c;
					keepComma = false;
				}
			}
		}

		return toReturn;
	}
}
