package dateconverter;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateConverterTest {

	@Test
	public void testRandom() {
		Number2Date n2d = new Number2Date();
		IntPair withYear = n2d.determineYear(41875);
		IntPair withMonth = n2d.determineMonth(withYear);
		
		//System.out.println(withYear.getValue()+"/ "+withMonth.getValue()+"/ "+withMonth.getRemainder());
		
		assertTrue(((withYear.getValue() == 2014) && (withMonth.getValue() == 8)) && (withMonth.getRemainder() == 24));
		
	}
	
	@Test
	public void test1() {
		Number2Date n2d = new Number2Date();
		IntPair withYear = n2d.determineYear(1);
		IntPair withMonth = n2d.determineMonth(withYear);
		
		//System.out.println(withYear.getValue()+"/ "+withMonth.getValue()+"/ "+withMonth.getRemainder());
		
		assertTrue(((withYear.getValue() == 1900) && (withMonth.getValue() == 1)) && (withMonth.getRemainder() == 1));
		
	}
	
	@Test
	public void falseYear() {
		Number2Date n2d = new Number2Date();
		IntPair withYear = n2d.determineYear(1);
		IntPair withMonth = n2d.determineMonth(withYear);
		
		//System.out.println(withYear.getValue()+"/ "+withMonth.getValue()+"/ "+withMonth.getRemainder());
		
		assertTrue(((withYear.getValue() != 1901) && (withMonth.getValue() == 1)) && (withMonth.getRemainder() == 1));
		
	}
	
	@Test
	public void falseMonth() {
		Number2Date n2d = new Number2Date();
		IntPair withYear = n2d.determineYear(1);
		IntPair withMonth = n2d.determineMonth(withYear);
		
		//System.out.println(withYear.getValue()+"/ "+withMonth.getValue()+"/ "+withMonth.getRemainder());
		
		assertTrue(((withYear.getValue() == 1900) && (withMonth.getValue() != 0)) && (withMonth.getRemainder() == 1));
		
	}
	
	@Test
	public void falseDay() {
		Number2Date n2d = new Number2Date();
		IntPair withYear = n2d.determineYear(1);
		IntPair withMonth = n2d.determineMonth(withYear);
		
		//System.out.println(withYear.getValue()+"/ "+withMonth.getValue()+"/ "+withMonth.getRemainder());
		
		assertTrue(((withYear.getValue() == 1900) && (withMonth.getValue() == 1)) && (withMonth.getRemainder() != 0));
		
	}

}
