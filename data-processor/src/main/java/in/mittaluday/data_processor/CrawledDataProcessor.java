package in.mittaluday.data_processor;

import java.io.File;
import java.io.FileNotFoundException;

public class CrawledDataProcessor {
	
	int uniquePagesCrawled;
	FileTokenizer fileTokenizer;
	
	public CrawledDataProcessor(){
		fileTokenizer = new FileTokenizer();
		uniquePagesCrawled = 0;		
	}
	
	public void startDataProcessing(){
		//TODO: add CrawlerUtilities to common place, add dependency
		//remove these hard codings
		
		File dumpFileDirectory = new File("C:/data/crawl/root/dump");
		for( File file:dumpFileDirectory.listFiles()){
			fileTokenizer.initialize(file);
			try {
				fileTokenizer.tokenizeFile();
				fileTokenizer.computeThreeGrams(fileTokenizer.getListOfTokens());
				fileTokenizer.printTokens();
				fileTokenizer.printThreegrams();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}			
			uniquePagesCrawled++;
		}
	}
	
	public int getUniquePagesCrawled(){
		return uniquePagesCrawled;
	}


}
