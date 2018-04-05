package Read_Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PhaseNamesFromCSV {

	public static ArrayList<String> returnPhaseNames(File csv) throws FileNotFoundException {
		Scanner linescanner = new Scanner(csv);

		ArrayList<String> phaseNames = new ArrayList<String>();
		linescanner.nextLine();
		linescanner.nextLine();
		linescanner.nextLine();
		linescanner.useDelimiter(",");

		while (linescanner.hasNextLine()) {
			if (linescanner.hasNext()) {
				linescanner.next();
			} else {
				break;
			}
			if (linescanner.hasNext()) {
				linescanner.next();
			} else {
				break;
			}
			if (linescanner.hasNext()) {
				// this selects the names. now we need to remove the word: "using"
				String phaseName = linescanner.next();
				if(phaseName.contains("USING") ){
					phaseNames.add(phaseName.replace("USING", ""));
				}else{
					phaseNames.add(phaseName);
				}
				
				
				
			}
			linescanner.nextLine();
		}

		linescanner.close();
		return phaseNames;

	}
}
