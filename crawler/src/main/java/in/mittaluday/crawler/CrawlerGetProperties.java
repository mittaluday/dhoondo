package in.mittaluday.crawler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class CrawlerGetProperties {
	InputStream inputStream;
 
	public Properties getPropValues() throws IOException {
		Properties prop = new Properties();
		try {
		
			String propFileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
			Date time = new Date(System.currentTimeMillis());
 
			// get the property value and print it out
//			String CRAWL_FOLDER = prop.getProperty("CRAWL_FOLDER");
//			String DUMP_FOLDER = prop.getProperty("DUMP_FOLDER");
//			String DUMP_FILE = prop.getProperty("DUMP_FILE");
//			String SUBDOMAINS = prop.getProperty("SUBDOMAINS");
//			String NEW_URL_BREAK = prop.getProperty("NEW_URL_BREAK");
//			String USER_STRING = prop.getProperty("USER_STRING");
//			String SEED_URL = prop.getProperty("SEED_URL");
//			String SEED_DOMAIN = prop.getProperty("SEED_DOMAIN");
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return prop;
	}
}
