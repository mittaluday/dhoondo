package in.mittaluday.crawler;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class MyCrawlerController {
	
	

	public static void main(String[] args) throws IOException {
		
		
		CrawlerGetProperties properties = new CrawlerGetProperties();
		Properties crawlerProperties = properties.getPropValues();
		
        int numberOfCrawlers = 10;

        CrawlConfig config = new CrawlConfig();
        setCrawlConfigurations(config, crawlerProperties);
        try {
			initializeDumpDirectory(config, crawlerProperties);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(0);
		}

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller;
		try {
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
			
	        /*
	         * For each crawl, you need to add some seed urls. These are the first
	         * URLs that are fetched and then the crawler starts following links
	         * which are found in these pages
	         */
	    	controller.addSeed(crawlerProperties.getProperty("SEED_URL"));	    	
	    	
	        /*
	         * Start the crawl. This is a blocking operation, meaning that your code
	         * will reach the line after this only when crawling is finished.
	         */
	    	long startTime = System.currentTimeMillis();
	        controller.start(MyUCICrawler.class, numberOfCrawlers);
	        long endTime = System.currentTimeMillis();
	        System.out.println("Total time for crawling: " + (endTime-startTime));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void setCrawlConfigurations(CrawlConfig config, Properties crawlerProperties) {		
        config.setCrawlStorageFolder(crawlerProperties.getProperty("CRAWL_FOLDER"));
        config.setMaxDepthOfCrawling(-1);
        config.setUserAgentString(crawlerProperties.getProperty("USER_STRING"));
        config.setPolitenessDelay(Integer.parseInt(crawlerProperties.getProperty("POLITENESS_DELAY")));
        config.setResumableCrawling(false);
        config.setShutdownOnEmptyQueue(true);
        config.setIncludeBinaryContentInCrawling(false);
        config.setProcessBinaryContentInCrawling(false);       
	}
	
	private static void initializeDumpDirectory(CrawlConfig config, Properties crawlerProperties) throws Exception {
	    File dumpFolder = new File(config.getCrawlStorageFolder() + crawlerProperties.getProperty("DUMP_FOLDER"));
	    if (!dumpFolder.exists()) {
	      if (!dumpFolder.mkdir()) {
	        throw new Exception("Failed creating the frontier folder: " + dumpFolder.getAbsolutePath());
	      }
	    } else {
	    	for(File file:dumpFolder.listFiles()){
	    		file.delete();
	    	}
	    }
	}	

}
