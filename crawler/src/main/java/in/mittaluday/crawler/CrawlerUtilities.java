package in.mittaluday.crawler;

public class CrawlerUtilities {

	public static final String CRAWL_FOLDER = "C:/data/crawl/root";
	public static final String DUMP_FOLDER = "/dump";
	public static final String DUMP_FILE = "dumpfile";
	public static final String SUBDOMAINS = "Subdomains.txt";
	
	public static final String NEW_URL_BREAK = "!@#$%^";	
	
	public static final String USER_STRING = "IR_W16_WebCrawler_14563364_70969866_95626832";
	public static final String SEED_URL = "http://www.ics.uci.edu/";
	public static final String SEED_DOMAIN = "ics.uci.edu";
	
	
	static public String getSubdomainFileName() {
		return CrawlerUtilities.CRAWL_FOLDER+CrawlerUtilities.CRAWL_FOLDER+"/"+CrawlerUtilities.SUBDOMAINS;
	}
	
	/*
	 * This method parses the domain name, retrieves the subdomain
	 * ie a URL like http://abc.ics.uci.edu/pathxyz should return abc.ics.uci.edu
	 */
	static public String parseURLForSubDomain(String url) {
		int startpos = url.indexOf("/", url.indexOf("/") + 1);
		int endpos = url.indexOf("/", startpos+1);
		if(endpos == -1){
			endpos = url.length();
		}
		return url.substring(startpos+1, endpos);
	}

}
