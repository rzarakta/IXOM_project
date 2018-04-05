package dateconverter;

public class Number2Date {

	//given a number for days since Jan 1 1900, return a pair of ints for the year and day of that year
	public IntPair determineYear(int toConvert){
		int year = 1900;

		while(toConvert >= 366){

			if(year % 4 == 0){	// is a leap year
				toConvert--;
			}
			toConvert -= 365;
			year++;
		}

		IntPair toReturn = new IntPair(year, toConvert);
		return toReturn;

	}


	//given a pair of ints (IntPair) for the year and the day of that year, return a pair of ints for the month and the day of the month
	public IntPair determineMonth(IntPair input){
		int toConvert = input.getRemainder(), month = 1;

		if((input.getValue() % 4 == 0) && toConvert >= 60){ 	// is a leap year
			toConvert--;
			if(toConvert == 59){	// is feb 29th
				IntPair leapDay = new IntPair(2, 29);
				return leapDay;
			}
		}

		while(toConvert > 31){
			if(month == 2){
				toConvert -= 28;
			}else if(month == 4 || month == 6 || month == 9 || month == 11){
				toConvert -= 30;
			}else {
				toConvert -= 31;
			}
			month++;
		}

		IntPair toReturn = new IntPair(month, toConvert);
		return toReturn;
	}

}
