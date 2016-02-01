package in.mittaluday.data_processor;

public class Starter {

	public static void main(String[] args) {		
		CrawledDataProcessor cdp = new CrawledDataProcessor();
		cdp.startDataProcessing();	
		System.out.println("Files crawled :" + cdp.uniquePagesCrawled);
	}

}
