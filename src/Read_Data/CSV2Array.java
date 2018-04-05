package Read_Data;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.Scanner;

import csvcleaner.CSVCleaner;

/*
 * 
 * author: Patrick Smith
 * email: patrickmsmith0@gmail.com
 * 
 */

public class CSV2Array {

	
	
	public ArrayList<DataPoint> populateData(File dirtyCSVFile, ArrayList<DataPoint> data) throws FileNotFoundException {

		Scanner linescanner = new Scanner(dirtyCSVFile);

		data = new ArrayList<DataPoint>();

		CSVCleaner c = new CSVCleaner(); 
		
		linescanner.nextLine();
		linescanner.nextLine();
		
		int step = 1, previousStep = 0;
		
		while (linescanner.hasNextLine()) {
	
			
			String line = c.lineCleaner(linescanner.nextLine());
	
		
			
			if(!line.contains("null")){

				
				
				Scanner scanner = new Scanner(line);
				scanner.useDelimiter(", ");

				DataPoint d = new DataPoint(scanner.next(), scanner.next(), scanner.nextDouble(), scanner.nextDouble(),
						scanner.nextDouble(), scanner.nextDouble(), (int) scanner.nextDouble());
				
				if(d.getZone() != previousStep){
					
					step++;
					previousStep = 1-previousStep;
					
					
				}
				d.setZone(step);
				
				
				data.add(d);
				scanner.close();
				
			}
			
		}

		linescanner.close();
		return data;

	}
	
	public static void printAll(ArrayList<DataPoint> data) {

		for (DataPoint output : data) {

			output.print();

		}

	}
	
	//testing array input


}
