package in.mittaluday.crawler;

public class CrawlerUtilities {

//
//	
//	static public String getSubdomainFileName() {
//		return CrawlerUtilities.CRAWL_FOLDER+CrawlerUtilities.CRAWL_FOLDER+"/"+CrawlerUtilities.SUBDOMAINS;
//	}
	
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
