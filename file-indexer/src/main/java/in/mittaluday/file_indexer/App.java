package in.mittaluday.file_indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {
	static String indexFilePath = "C:/temp/index.ser";
	static String anchorIndexFilePath = "C:/temp/anchorindex.ser";

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		Map<String, List<Postings>> index;
		index = getIndex();
		showIndex(index);
	}
	
	public static Map<String, List<Postings>> getIndex() throws ClassNotFoundException, IOException{
	   return getIndex(indexFilePath);
	}
	public static Map<String, List<Postings>> getAnchorTextIndex() throws ClassNotFoundException, IOException{
		   return getIndex(anchorIndexFilePath);
		}
	public static Map<String, List<Postings>> getIndex(String file) throws ClassNotFoundException, IOException {
		Map<String, List<Postings>> index;
		File f = new File(file);
		if (f.exists() && !f.isDirectory()) {
			System.out.println("Index Exists!");
			index = loadIndex();
		} else {
			if(file.equals(indexFilePath)){
			index = createIndex();
			}
			else{
				index = createAnchorIndex();	
			}
			serializeIndex(index);
		}

		return index;
	}

	public static void calculateTfidf(TermIndex index) {
		int counter = 0;
		System.out.println("Total Terms: " + String.valueOf(index.getIndex().keySet().size()));
		for (String term : index.getIndex().keySet()) {
			counter += 1;
			if (counter % 10000 == 0) {
				System.out.println(counter + " tfidf calculated for these many terms");
			}
			for (Postings p : index.getIndex().get(term)) {
				p.setTfidf(p.getPositions().size(), index.getIndex().get(term).size(), index.getCorpus());
			}
		}
	}

	public static void serializeIndex(Map<String, List<Postings>> index) throws IOException {
		System.out.println("TermObject Size: " + index.size());
		File indexFile = new File(indexFilePath);
		if (!indexFile.exists()) {
			indexFile.createNewFile();
		}
		FileOutputStream f = new FileOutputStream(indexFile, false);
		ObjectOutputStream s = new ObjectOutputStream(f);
		s.writeObject(index);
		s.close();
	}

	public static void showIndex(Map<String, List<Postings>> index) {
		int counter = 0;
		for (String term : index.keySet()) {
			counter += 1;
			System.out.println(term + index.get(term));
			if (counter == 50) {
				break;
			}
		}
	}

	public static Map<String, List<Postings>> createIndex() throws FileNotFoundException {

		String CRAWL_FOLDER = "C:/temp";
		String DUMP_FOLDER = "/dumpdata";
		TermIndex index = new TermIndex();
		int counter = 0;
		File dumpFileDirectory = new File(CRAWL_FOLDER + DUMP_FOLDER);
		for (File file : dumpFileDirectory.listFiles()) {
			counter += 1;
			index.addTerms(file);
			if (counter % 5000 == 0) {
				System.out.println(counter + " Files indexed");
			}
		}
		calculateTfidf(index);
		return TermIndex.getIndex();
	}
	public static Map<String, List<Postings>> createAnchorIndex() throws ClassNotFoundException, IOException {
		TermIndex index = new TermIndex();
		AnchorTextProcessor anchor= new AnchorTextProcessor();
		Map<String,String> anchorMap=anchor.startDataProcessing();
		int counter = 0;
		for (String s:anchorMap.keySet()) {
			counter += 1;
			index.addTerms(s,anchorMap.get(s));
			if (counter % 5000 == 0) {
				System.out.println(counter + " Files indexed");
			}
		}
		calculateTfidf(index);
		return TermIndex.getIndex();
	}

	public static Map<String, List<Postings>> loadIndex() throws IOException, ClassNotFoundException {

		File indexFile = new File(indexFilePath);
		FileInputStream f = new FileInputStream(indexFile);
		ObjectInputStream s = new ObjectInputStream(f);
		HashMap<String, List<Postings>> index = (HashMap<String, List<Postings>>) s.readObject();
		s.close();
		f.close();
		return index;

	}

}
