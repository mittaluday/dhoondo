package in.mittaluday.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|jsp|gif|jpg|mat|php"
            + "|png|mp3|mp4|zip|gz|pdf|cc|cpp|java|py|javac))$");
    
    private static int numFilesinDumpFolder = 1;    
    
    public static HashMap<String , LinksCounter> pageRankerMap = new HashMap<String, LinksCounter>();
    public static HashMap<String , ArrayList<String>> anchortextMap = new HashMap<String, ArrayList<String>>();
    
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
			e.printStackTrace();
		}
		logger.info("###Seed domain = " + String.valueOf(!FILTERS.matcher(href).matches() &&
       		 href.contains(crawlerProperties.getProperty("SEED_DOMAIN")) &&
       		 !href.contains("?"))+ ": href=" + href + ": " + href.getClass()) ;
		logger.info("********************************************************************************************");
		
		if(href.contains("student-affairs/contact/") 
				|| href.contains("archive.ics.uci.edu") 
				|| href.contains("wics.ics.uci.edu")){
			return false;
		}

		return !FILTERS.matcher(href).matches() &&
        		 href.contains(crawlerProperties.getProperty("SEED_DOMAIN")) &&
        		 !href.contains("?") && (href.length()<=512);
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
         //logger.info(crawlerProperties.getProperty("USER_STRING")+"URL: " + url);

         if (page.getParseData() instanceof HtmlParseData) {
             HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
             String text = htmlParseData.getText();
             String html = htmlParseData.getHtml();
             Set<WebURL> links = htmlParseData.getOutgoingUrls();
             String title = htmlParseData.getTitle();
             
//             addLinksToPageRankerMap(url, links);
             
             try {
				addToDumpFiles(text.trim(), html.trim(), url, title, crawlerProperties);
			} catch (IOException e) {
				e.printStackTrace();
			}                       
         }
    }
    

	private void addLinksToPageRankerMap(String url, Set<WebURL> links) {
		// Increment outgoing links from given url
		if(pageRankerMap.containsKey(url)){
			LinksCounter lc = pageRankerMap.get(url);
			lc.incrementOutgoingLinks(links.size());
			pageRankerMap.put(url, lc);
			
		} else {
			LinksCounter lc = new LinksCounter();
			lc.setOutgoingLinks(links.size());	
			pageRankerMap.put(url, lc);
		}
		
		// Increment incoming links for all the urls in links set
		for(WebURL w : links){
			String linkurl = w.getURL();			
			if(pageRankerMap.containsKey(linkurl)){
				LinksCounter lc = pageRankerMap.get(linkurl);
				lc.incrementIncomingLinks(1);
				pageRankerMap.put(linkurl, lc);
			} else {
				LinksCounter lc = new LinksCounter();
				lc.setIncomingLinks(1);	
				pageRankerMap.put(linkurl, lc);
			}		
			
			String anchorText = w.getAnchor();
			if(anchortextMap.containsKey(linkurl)){
				ArrayList<String> anchorList = anchortextMap.get(linkurl);
				anchorList.add(anchorText);
				anchortextMap.put(linkurl, anchorList);
			} else {
				ArrayList<String> anchorList = new ArrayList<String>();
				anchorList.add(anchorText);
				anchortextMap.put(linkurl, anchorList);
			}			
		}		
	}

	private synchronized void addToDumpFiles(String text, String html, String url, String title, Properties crawlerProperties) throws IOException {
		int dumpFileNumber = getDumpFileNumber(crawlerProperties.getProperty("CRAWL_FOLDER"),crawlerProperties.getProperty("DUMP_FOLDER"));
		addToDataDumpFile(text.trim(), url, title, crawlerProperties, dumpFileNumber);
		addToDataDumpHtmlFile(html.trim(), url, title, crawlerProperties, dumpFileNumber);		
	}

	private synchronized void addToDataDumpFile(String text, String url, String title, 
												Properties crawlerProperties, int dumpFileNumber) throws IOException {		
		File dumpFile = new File(getDumpFileName(dumpFileNumber, crawlerProperties.getProperty("CRAWL_FOLDER"),
																crawlerProperties.getProperty("DUMP_FOLDER"),
																crawlerProperties.getProperty("DUMP_FILE")));
		System.out.println("Trying to create " + dumpFile.getAbsolutePath());
		dumpFile.createNewFile();
		PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter(dumpFile, true)));		
		addNewURLHeader(out,url);
		out.println(title);
		out.println(text);	
		out.flush();
	}
	
	 
	private synchronized void addToDataDumpHtmlFile(String html, String url, String title, Properties crawlerProperties, int dumpFileNumber) throws IOException {
		File dumpHtmlFile = new File(getDumpFileName(dumpFileNumber, crawlerProperties.getProperty("CRAWL_FOLDER"),
				crawlerProperties.getProperty("DUMP_HTML_FOLDER"),
				crawlerProperties.getProperty("DUMP_FILE")));
		System.out.println("Trying to create " + dumpHtmlFile.getAbsolutePath());
		dumpHtmlFile.createNewFile();
		PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter(dumpHtmlFile, true)));		
		addNewURLHeader(out,url);
		out.println(title);
		out.println(html);	
		out.flush();
	}

	private synchronized int getDumpFileNumber(String crawlFolder, String dumpFolder) {
		return numFilesinDumpFolder++;
	}

	private void addNewURLHeader(PrintWriter out, String url) {
		out.println(url);
	}
	
	public String getDumpFileName(int dumpFileNumber, String crawlFolder, String dumpFolder, String dumpFile) {
		return crawlFolder+dumpFolder+"/"+dumpFile+"_"+dumpFileNumber+".txt";
	}
	
}
