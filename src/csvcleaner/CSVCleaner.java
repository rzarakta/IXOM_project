package csvcleaner;

import timeconverter.TimeConverter;

/*
 * 
 * author: Jacob Hoffland
 * email: jacob.hoffland@gmail.com
 * 
 */


public class CSVCleaner {
	
	public String lineCleaner(String dirtyLine){
		
		String cleanLine = "", input = "", date = "", time = "", turb = "", cond = "", soil = "", temp = "", step = "";
		int lineLength = dirtyLine.length(), stage = 0;
		
		for(int i = 0; i < lineLength; i++){
			if(dirtyLine.charAt(i) == ','){
				
				if(stage == 0){
					date = dateTimeCleaner(removeSpaces(input));
					
				} else if (stage == 1){
			
					double timedec = Double.parseDouble(removeSpaces(input));
					TimeConverter t = new TimeConverter();
					time = dateTimeCleaner(t.decToHMS(timedec));
					
				} else if (stage == 2){
					turb = decimalCleaner(removeSpaces(input));
					
				} else if(stage == 3) {
					cond = decimalCleaner(removeSpaces(input));
					
				} else if(stage == 4) {
					soil = decimalCleaner(removeSpaces(input));
					
				} else if(stage == 5) {
					temp = decimalCleaner(removeSpaces(input));
					
				} else if (stage == 6){
					step = decimalCleaner(removeSpaces(input));
				}
				
				input = "";
				stage++;
				
			} else {
				input += dirtyLine.charAt(i);
			}
		}
		
		// !!!PAT ADDED LAST COMMA!!
		
		cleanLine = date+", "+time+", "+turb+", "+cond+", "+soil+", "+temp+", "+step+", ";
		
		return cleanLine;
	}

	
	//remove spaces from a String
	public String removeSpaces(String dirty){
		
		String clean = "";
		int length = dirty.length();
		char c;
		
		for(int i = 0; i < length; i++){
			c = dirty.charAt(i);
			if(c != ' '){
				clean += c;
			}
		}
		
		return clean;
	}
	
	
	//input string for date or time and returns in correct format or null if wrong input, currently doesn't deal with strings that are too long eg 19/01/2016
	public String dateTimeCleaner(String dirtyDateTime){
		
		int firstDigit = 0, secondDigit = 0, thirdDigit = 0, length = dirtyDateTime.length();
		String first = "", second = "", third = "", cleanDateTime = "";
		char c;
		boolean dateTime;
		
		if(dirtyDateTime.contains("/")){
			dateTime = true;
		} else if (dirtyDateTime.contains(":")){
			dateTime = false;
		} else {
			return " incorrect format";
		}
		
		for(int i = 0; i < length; i++){
			c = dirtyDateTime.charAt(i);
			
			if(Character.isDigit(c)){
				if(firstDigit < 2){
					first += c;
					firstDigit++;
					
				} else if (secondDigit < 2){
					second+= c;
					secondDigit++;
					
				} else if (thirdDigit < 2){
					third += c;
					thirdDigit++;
					
				}
			}
		}
		
		if((firstDigit + secondDigit + thirdDigit) == 6){
			if(dateTime){
				cleanDateTime = first+"/"+second+"/"+third;
			} else {
				cleanDateTime = first+":"+second+":"+third;
			}
			return cleanDateTime;
		}
		
		return "incorrect format";
	}
	
	
	//input string for a decimal and returns in correct format or null if wrong input
	public String decimalCleaner(String dirtyDec){
		
		String cleanDec = "";
		int length = dirtyDec.length(), dec = 0, dot = 0;
		char c;
		
		for(int i = 0; i < length; i++){
			c = dirtyDec.charAt(i);
			
			if(Character.isDigit(c)){
				cleanDec += c;
				dec++;
			} else if(c == '.' && dot == 0){
				
				if(dec == 0){
					cleanDec += "0.";
					dot++;
				} else {
					cleanDec += ".";
					dot++;
				}
			} else{
				return null;
			}
		}
		
		return cleanDec;
	}
	
}
