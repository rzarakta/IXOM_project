package timeconverter;

/*
 * 
 * author: Jacob Hoffland
 * email: jacob.hoffland@gmail.com
 * 
 * 
 */

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeConverterTest {

	//testing that midnight converts correctly
	@Test
	public void testMidnightDec2HSM() {
		TimeConverter tc = new TimeConverter();
		
		String hms = tc.decToHMS(0.0);
		assertTrue(hms.equals("00:00:00"));
	}

	@Test
	public void testMidnightHSM2Dec() {
		TimeConverter tc = new TimeConverter();
		
		double dec = 0.0;
		double value = tc.HMSToDec("00:00:00");
		assertTrue(dec == value);
	}
	
	//testing that 1sec to midnight converts properly
	@Test
	public void testEndDec2HSM() {
		TimeConverter tc = new TimeConverter();
		
		double fraction = (86399.0/86400);
		String hms = tc.decToHMS(fraction);
		assertTrue(hms.equals("23:59:59"));
	}

	@Test
	public void testEndHSM2Dec() {
		TimeConverter tc = new TimeConverter();
		
		double dec = (86399.0/86400);
		double value = tc.HMSToDec("23:59:59");
		assertTrue(dec == value);
	}
	
}
