package in.mittaluday.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class MyCrawlerController {

	

	public static void main(String[] args) throws IOException {
	    Logger logger = LoggerFactory.getLogger(MyCrawlerController.class);

		CrawlerGetProperties properties = new CrawlerGetProperties();
		Properties crawlerProperties = properties.getPropValues();
		
		String logSeq = crawlerProperties.getProperty("USER_STRING");
		
        int numberOfCrawlers = 10;

        CrawlConfig config = new CrawlConfig();
        setCrawlConfigurations(config, crawlerProperties);
        try {
			initializeDumpDirectories(config, crawlerProperties);
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
	    	//controller.addSeed("http://www.ics.uci.edu/~lopes/teaching/cs221W16/");
	    	
	        /*
	         * Start the crawl. This is a blocking operation, meaning that your code
	         * will reach the line after this only when crawling is finished.
	         */
	    	long startTime = System.currentTimeMillis();
	        controller.start(MyUCICrawler.class, numberOfCrawlers);
	        long endTime = System.currentTimeMillis();
	        logger.info(logSeq+ "Total time for crawling: " + (endTime-startTime));
	        
	        //serializeMaps();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void setCrawlConfigurations(CrawlConfig config, Properties crawlerProperties) {		
        config.setCrawlStorageFolder(crawlerProperties.getProperty("CRAWL_FOLDER"));
        config.setMaxDepthOfCrawling(Integer.parseInt(crawlerProperties.getProperty("CRAWLING_DEPTH")));
        config.setUserAgentString(crawlerProperties.getProperty("USER_STRING"));
        config.setPolitenessDelay(Integer.parseInt(crawlerProperties.getProperty("POLITENESS_DELAY")));
        config.setResumableCrawling(true);
        config.setShutdownOnEmptyQueue(true);
        config.setIncludeBinaryContentInCrawling(false);
        config.setProcessBinaryContentInCrawling(false);    
	}
	
	private static void initializeDumpDirectories(CrawlConfig config, Properties crawlerProperties) throws Exception {
	    File dumpFolder = new File(config.getCrawlStorageFolder() + crawlerProperties.getProperty("DUMP_FOLDER"));
	    if (!dumpFolder.exists()) {
	      if (!dumpFolder.mkdirs()) {
	        throw new Exception("Failed creating the frontier folder: " + dumpFolder.getAbsolutePath());
	      }
	    } else {
	    	for(File file:dumpFolder.listFiles()){
	    		file.delete();
	    	}
	    }
	    
	    
	    File dumpHtmlFolder = new File(config.getCrawlStorageFolder() + crawlerProperties.getProperty("DUMP_HTML_FOLDER"));
	    if (!dumpHtmlFolder.exists()) {
	      if (!dumpHtmlFolder.mkdirs()) {
	        throw new Exception("Failed creating the frontier folder: " + dumpHtmlFolder.getAbsolutePath());
	      }
	    } else {
	    	for(File file:dumpHtmlFolder.listFiles()){
	    		file.delete();
	    	}
	    }  	    
	}	
	
	private static void serializeMaps() throws IOException {
		CrawlerGetProperties crawlerProperties = new CrawlerGetProperties();
		Properties properties;
		properties = crawlerProperties.getPropValues();

		File anchorText = new File(properties.getProperty("CRAWL_FOLDER") + "/" + properties.getProperty("ANCHORTEXTFILE"));
		File pageRankMap = new File(properties.getProperty("CRAWL_FOLDER") + "/" + properties.getProperty("PAGERANKMAP"));
		
		if(!anchorText.exists()){
			anchorText.createNewFile();
		}
		FileOutputStream f = new FileOutputStream(anchorText, false);
		ObjectOutputStream s = new ObjectOutputStream(f);
		s.writeObject(MyUCICrawler.anchortextMap);
		s.close();
		
		if(!pageRankMap.exists()){
			pageRankMap.createNewFile();
		}
		f = new FileOutputStream(pageRankMap, false);
		s = new ObjectOutputStream(f);
		s.writeObject(MyUCICrawler.pageRankerMap);
		s.close();		
	}

}
