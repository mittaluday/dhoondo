package in.mittaluday.data_processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class FileTokenizer {

	private File file;
	private HashMap<String, Integer> tokenCount;
	private HashMap<String, List<Integer>> tokenPosition;

	private ArrayList<String> listOfTokens;
	private ArrayList<String> listofThreeGrams;
	private HashMap<String, Integer> threeGramsFrequency;
	private String subdomain;
	private String subdomainURL;


	public String getSubdomainURL() {
		return subdomainURL;
	}

	public void setSubdomainURL(String subdomainURL) {
		this.subdomainURL = subdomainURL;
	}

	private static final String specialCharacters = ":;,~!@#$%^&*()_+=\\.\"<>?/`\\W";
	private static final String delimeters = "[ " + specialCharacters + "]+";

	public FileTokenizer() {
		tokenCount = new HashMap<String, Integer>();
		tokenPosition = new HashMap<String, List<Integer>>();
		listOfTokens = new ArrayList<String>();
		listofThreeGrams = new ArrayList<String>();
		threeGramsFrequency = new HashMap<String, Integer>();
	}

	public FileTokenizer(File file) {
		this();
		this.file = file;
	}

	public List<String> getListOfTokens() {
		return listOfTokens;
	}

	public void initialize(File file) {
		tokenCount = new HashMap<String, Integer>();
		listOfTokens = new ArrayList<String>();
		listofThreeGrams = new ArrayList<String>();
		threeGramsFrequency = new HashMap<String, Integer>();
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public HashMap<String, Integer> getTokenCount() {
		return tokenCount;
	}

	public HashMap<String, List<Integer>> getTokenPostion() {
		return tokenPosition;
	}

	public void setTokenCount(HashMap<String, Integer> tokenCount) {
		this.tokenCount = tokenCount;
	}

	public String getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

	/**
	 * First line of the dump file contains the URL This method extracts the
	 * subdomainURL
	 * 
	 * @param fileHandle
	 */
	public void parseSubdomain(Scanner fileHandle) {
		this.subdomainURL = fileHandle.nextLine().trim();
	}

	/**
	 * Method to parse the subdomain from the URL
	 * 
	 * @param url
	 * @return
	 */
	public String parseURLForSubDomain(String url) {
		int startpos = url.indexOf("/", url.indexOf("/") + 1);
		int endpos = url.indexOf("/", startpos + 1);
		if (endpos == -1) {
			endpos = url.length();
		}
		return url.substring(startpos + 1, endpos);
	}

	public void tokenizeFile() throws FileNotFoundException {
		Scanner fileReader = new Scanner(file);
		if (fileReader.hasNextLine()) {
			parseSubdomain(fileReader);
			this.subdomain = parseURLForSubDomain(this.subdomainURL);
		}
		while (fileReader.hasNextLine()) {
			String line = fileReader.nextLine();

			String tokens[] = line.split(delimeters);

			for (int i = 0; i < tokens.length; i++) {
				String trimmedToken = tokens[i].trim();
				if (trimmedToken.length() > 0) {
					listOfTokens.add(trimmedToken.toLowerCase());
					incrementTokenCount(trimmedToken.toLowerCase());

					addTokenPosition(trimmedToken.toLowerCase(), i);

				}
			}
		}
		fileReader.close();
	}

	public void computeTokenFrequencies(List<String> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			incrementTokenCount(tokens.get(i).toLowerCase());
		}
	}

	static HashSet<String> stopworddict;

	public static void loadDictionary() throws IOException, ClassNotFoundException {
		String stopworddictDictFilePath = "stopwords.ser";
		File stopwordDictFile = new File(stopworddictDictFilePath);
		FileInputStream f = new FileInputStream(stopworddictDictFilePath);
		ObjectInputStream s = new ObjectInputStream(f);
		stopworddict = (HashSet<String>) s.readObject();
		s.close();
		f.close();
	}

	public void computeThreeGrams(List<String> tokens) throws ClassNotFoundException, IOException {
		int threeGramCount = 1;
		loadDictionary();
		String firstGram = "", secondGram = "", thirdGram = "";
		for (int i = 0; i < tokens.size(); i++) {
			String token = tokens.get(i);
			if (!stopworddict.contains(token)) {
				if (threeGramCount == 1) {
					firstGram = token.toLowerCase();
					threeGramCount++;
				} else if (threeGramCount == 2) {
					firstGram = token.toLowerCase();
					threeGramCount++;
				} else {
					thirdGram = token.toLowerCase();
					String temp = firstGram + " " + secondGram + " " + thirdGram;
					listofThreeGrams.add(temp);
					incrementThreeGramCount(temp);
					firstGram = secondGram;
					secondGram = thirdGram;
				}
			}
		}
	}

	private void incrementTokenCount(String token) {
		if (tokenCount.containsKey(token)) {
			int count = tokenCount.get(token);
			tokenCount.put(token, count + 1);
		} else {
			tokenCount.put(token, 1);
		}
	}

	private void addTokenPosition(String token, int position) {
		if (tokenPosition.containsKey(token)) {
			tokenPosition.get(token).add(position);
		} else {
			tokenPosition.put(token, new ArrayList<Integer>());
			tokenPosition.get(token).add(position);
		}
	}

	private void incrementThreeGramCount(String threeGram) {
		if (threeGramsFrequency.containsKey(threeGram)) {
			int count = threeGramsFrequency.get(threeGram);
			threeGramsFrequency.put(threeGram, count + 1);
		} else {
			threeGramsFrequency.put(threeGram, 1);
		}
	}

	void printTokens() {
		System.out.println("***************Printing tokens:");
		for (int i = 0; i < listOfTokens.size(); i++) {
			System.out.println(listOfTokens.get(i));
		}
	}

	void printTokenFrequencies() {
		System.out.println("***************Printing tokens count:");
		HashMap<String, Integer> sortedMap = sortMap(tokenCount);
		Iterator<Entry<String, Integer>> it = sortedMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
	}

	void printThreegrams() {
		System.out.println("***************Printing 3-grams count:");
		HashMap<String, Integer> sortedMap = sortMap(threeGramsFrequency);
		Iterator<Entry<String, Integer>> it = sortedMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
	}

	public <K, V extends Comparable<? super V>> HashMap<K, V> sortMap(HashMap<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		HashMap<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public ArrayList<String> getListOfThreeGrams() {
		return listofThreeGrams;
	}

}
