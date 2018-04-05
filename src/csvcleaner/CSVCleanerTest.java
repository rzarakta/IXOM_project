package csvcleaner;

import org.junit.Test;

public class CSVCleanerTest {

	@Test
	public void testCleanLine() {
		CSVCleaner csvc = new CSVCleaner();
		String toClean = "15/06/12, 12:51:11, 4, 0, 0, 85.5, 0";
		String clean = csvc.lineCleaner(toClean);
		System.out.print(clean);
	}

}
