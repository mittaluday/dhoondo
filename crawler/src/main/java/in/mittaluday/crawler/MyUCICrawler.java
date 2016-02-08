package in.mittaluday.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyUCICrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|jsp|gif|jpg|php"
            + "|png|mp3|mp3|zip|gz))$");
    
    private static int numFilesinDumpFolder = 1;    
    
    private static Logger logger = LoggerFactory.getLogger(MyUCICrawler.class);

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
     @Override
     public boolean shouldVisit(Page referringPage, WebURL url) {
         String href = url.getURL().toLowerCase();
         CrawlerGetProperties properties = new CrawlerGetProperties();
 	     Properties crawlerProperties = null;
		try {
			crawlerProperties = properties.getPropValues();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("###Seed domain = " + href.contains(crawlerProperties.getProperty("SEED_DOMAIN"))+ ": href=" + href);
         return !FILTERS.matcher(href).matches() &&
        		 href.contains(crawlerProperties.getProperty("SEED_DOMAIN")) &&
        		 !href.contains("?");
     }

     /**
      * This function is called when a page is fetched and ready
      * to be processed by your program.
      */
     @Override
     public void visit(Page page) {
    	 CrawlerGetProperties properties = new CrawlerGetProperties();
 	     Properties crawlerProperties = null;
		try {
			crawlerProperties = properties.getPropValues();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
         String url = page.getWebURL().getURL();
         logger.info(crawlerProperties.getProperty("USER_STRING")+"URL: " + url);

         if (page.getParseData() instanceof HtmlParseData) {
             HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
             String text = htmlParseData.getText();
             String html = htmlParseData.getHtml();
             Set<WebURL> links = htmlParseData.getOutgoingUrls();

             logger.info(crawlerProperties.getProperty("USER_STRING")+"Text length: " + text.length());
             logger.info(crawlerProperties.getProperty("USER_STRING")+"Html length: " + html.length());
             logger.info(crawlerProperties.getProperty("USER_STRING")+"Number of outgoing links: " + links.size());
             
             try {
				addToDataDumpFile(text.trim(), url, crawlerProperties);
			} catch (IOException e) {
				e.printStackTrace();
			}                       
         }
    }
     
	private synchronized void addToDataDumpFile(String text, String url, Properties crawlerProperties) throws IOException {
		int dumpFileNumber = getDumpFileNumber(crawlerProperties.getProperty("CRAWL_FOLDER"),
												crawlerProperties.getProperty("DUMP_FOLDER"));
		
		File dumpFile = new File(getDumpFileName(dumpFileNumber, crawlerProperties.getProperty("CRAWL_FOLDER"),
																crawlerProperties.getProperty("DUMP_FOLDER"),
																crawlerProperties.getProperty("DUMP_FILE")));	
		dumpFile.createNewFile();
//		System.out.println("Crawled " + url + " created file " + dumpFile.getAbsolutePath());
		PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter(dumpFile, true)));		
		addNewURLHeader(out,url);
		out.println(text);	
		out.flush();
	}

	private synchronized int getDumpFileNumber(String crawlFolder, String dumpFolder) {
//		return new File(crawlFolder+dumpFolder).list().length+1;
		return numFilesinDumpFolder++;
	}

	private void addNewURLHeader(PrintWriter out, String url) {
//		out.println(CrawlerUtilities.NEW_URL_BREAK + CrawlerUtilities.USER_STRING + CrawlerUtilities.NEW_URL_BREAK);
		out.println(url);
	}
	
	public String getDumpFileName(int dumpFileNumber, String crawlFolder, String dumpFolder, String dumpFile) {
		return crawlFolder+dumpFolder+"/"+dumpFile+"_"+dumpFileNumber+".txt";
	}
}
