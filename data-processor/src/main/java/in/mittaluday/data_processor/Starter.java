package in.mittaluday.data_processor;

import java.io.IOException;

public class Starter {

	public static void main(String[] args) throws IOException, ClassNotFoundException {		
		CrawledDataProcessor cdp = new CrawledDataProcessor();
		cdp.startDataProcessing();	
		System.out.println("Files crawled :" + cdp.getUniquePagesCrawled());
	}

}
