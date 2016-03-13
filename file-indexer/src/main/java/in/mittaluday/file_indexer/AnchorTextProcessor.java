package in.mittaluday.file_indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AnchorTextProcessor {

	public HashMap<String, String> anchorIndex = new HashMap<String, String>();

	public Map<String, String> startDataProcessing() throws IOException, ClassNotFoundException {
		Properties prop = getConfigurationProperties();

		File dumpFileDirectory = new File("C:/temp/dumpdata/htmlnewzip-full");
//		File dumpFileDirectory = new File(prop.getProperty("CRAWL_FOLDER") + prop.getProperty("DUMP_HTML_FOLDER"));
		System.out.println("html files : " + dumpFileDirectory.listFiles().length);
		for (File file : dumpFileDirectory.listFiles()) {
			Document doc = Jsoup.parse(file, null);
			Elements links = doc.select("a[href]");
			for (Element link : links) {
//				if (!link.attr("href").contains(".ics.uci.edu")) {
//					continue;
//				}
				if (anchorIndex.containsKey(link.attr("href"))) {
					anchorIndex.put(link.attr("href"), anchorIndex.get(link.attr("href")) + " " + link.text());
				} else {
					anchorIndex.put(link.attr("href"), link.text());
				}
			}

		}
		System.out.println("anchorindex size " + anchorIndex.size());
		showIndex(anchorIndex);
		return anchorIndex;

	}

	public static void showIndex(Map<String, String> index) {
		int counter = 0;
		for (String term : index.keySet()) {
			counter += 1;
			System.out.println(term + " " + index.get(term));
			if (counter == 50) {
				break;
			}
		}
	}

	private Properties getConfigurationProperties() throws IOException {
		InputStream inputStream = null;
		Properties prop = new Properties();
		try {
			String propFileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return prop;
	}

}
