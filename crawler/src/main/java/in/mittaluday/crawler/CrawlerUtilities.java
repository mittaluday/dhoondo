package in.mittaluday.crawler;

public class CrawlerUtilities {

	public static final String CRAWL_FOLDER = "C:/data/crawl/root";
	public static final String DUMP_FOLDER = "/dump";
	public static final String DUMP_FILE = "dumpfile.txt";
	public static final String SUBDOMAINS = "Subdomains.txt";
	
	public static final String NEW_URL_BREAK = "!@#$%^";	
	
	public static final String USER_STRING = "IR_W16_WebCrawler_14563364";
	public static final String SEED_URL = "http://www.ics.uci.edu/";
	public static final String SEED_DOMAIN = "ics.uci.edu";
	
	
	static public String getDumpFileName() {
		return CrawlerUtilities.CRAWL_FOLDER+CrawlerUtilities.DUMP_FOLDER+"/"+CrawlerUtilities.DUMP_FILE;
	}
	
	static public String getSubdomainFileName() {
		return CrawlerUtilities.CRAWL_FOLDER+CrawlerUtilities.CRAWL_FOLDER+"/"+CrawlerUtilities.SUBDOMAINS;
	}

}
