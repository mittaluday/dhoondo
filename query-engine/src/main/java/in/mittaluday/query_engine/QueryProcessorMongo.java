package in.mittaluday.query_engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.jsoup.Jsoup;

import in.mittaluday.file_indexer.MongoApp;
import in.mittaluday.file_indexer.Postings;

public class QueryProcessorMongo {
	Map<String, List<Postings>> index;
	HashMap<String, Double> cumulativePageScoreMap;
	HashMap<String, Tuple> cosineScoreMap;
	HashMap<String, Double> cosineMap;
	HashMap<String, String> urlToTitleMap;

	/**
	 * Class to store Numerator and Denominator tuple for cosine similarity
	 * 
	 * @author udaymittal
	 *
	 */
	class Tuple {

		double numerator;
		double denominator;
		double score;

		public Tuple(double numerator, double denominator) {
			this.numerator = numerator;
			this.denominator = denominator;
		}

		public double getNumerator() {
			return numerator;
		}

		public void setNumerator(double numerator) {
			this.numerator = numerator;
		}

		public double getDenominator() {
			return denominator;
		}

		public void setDenominator(double denominator) {
			this.denominator = denominator;
		}

		public double getScore() {
			return numerator / denominator;
		}
	}

	public QueryProcessorMongo() throws ClassNotFoundException, IOException {
		cumulativePageScoreMap = new HashMap<String, Double>();
		urlToTitleMap = new HashMap<String, String>();
		cosineScoreMap = new HashMap<String, Tuple>();
	}

	public ArrayList<Result> queryIndex(String query) throws IOException {
		if (query == null || query.isEmpty()) {
			return new ArrayList<Result>();
		}
		String[] queryTerms = query.split(" ");
		findMatchingPages(queryTerms);
		findMatchingPagesInAnchorText(queryTerms);
		System.out.println("map size =" + cumulativePageScoreMap.size());
		ArrayList<String> stringResults = rankResults();
		ArrayList<Result> results = createResultStructureList(stringResults,query);
		return results;

		// For cosine similarity use following

		// findMatchingPagesBasedOnCosine(queryTerms);
		// ArrayList<String> stringResults = rankCosineResults();

	}

	private ArrayList<Result> createResultStructureList(ArrayList<String> stringResults, String query) throws IOException {
		ArrayList<Result> results = new ArrayList<Result>();
		for (int i = 0; i < stringResults.size(); i++) {
			String[] queryterms = query.split(" ");
			Result r = new Result(stringResults.get(i), urlToTitleMap.get(stringResults.get(i)));
			if(r.getTitle() == null){
				System.out.println("found null");
				continue;
			}
			List<Document> file = MongoApp.db.getCollection("urltofilename")
					.find(new Document("url", stringResults.get(i))).into(new ArrayList<Document>());
			StringBuilder displaytext = new StringBuilder();
			if (!file.isEmpty()) {
//				System.out.println(file.get(0).getString("filename"));
				File f = new File(file.get(0).getString("filename"));
				org.jsoup.nodes.Document doc = Jsoup.parse(f, null);
				String text = doc.text();
//				System.out.println("doc.text :" + text);
				for (int j = 0; j < queryterms.length; j++) {
					String REGEX = "\\b" + queryterms[j] + "\\b";
					Pattern p = Pattern.compile(REGEX);
					Matcher m = p.matcher(text);
					int start, end;
					if (m.find()) {
						int pos = m.start();
						if (pos == 0) {
							start = pos;
							end = text.indexOf(" ", m.end() + 40);
							displaytext.append(text.substring(start, end));
							displaytext.append("(...)");
						}
						if (pos > 0 && m.end() <= text.length() - 20) {
							start = text.indexOf(" ", m.start() - 20);
							end = text.indexOf(" ", m.end() + 20);
							displaytext.append(text.substring(start, end));
							displaytext.append("(...)");
						}
						if (m.end() == text.length()) {
							end = m.end();
							start = text.indexOf(" ", m.start() - 40);
							displaytext.append("(...)");
							displaytext.append(text.substring(start, end));
						}
					}
				}
//				System.out.println("setting descr: " + displaytext.toString());
				r.setDescription(displaytext.toString());
			}
			results.add(r);
		}
		return results;
	}

	/**
	 * Just plain cumulative TFIDF score for ranking This should be used to
	 * calculate original NDCG
	 * 
	 * @param queryTerms
	 */
	@SuppressWarnings("unused")
	private void findMatchingPagesOld(String[] queryTerms) {
		for (String term : queryTerms) {
			term = term.toLowerCase();
			List<Document> postings = MongoApp.db.getCollection("index").find(new Document("term", term))
					.into(new ArrayList<Document>());
			if (postings != null) {
				for (Document p : postings) {
					addPageScoreForTerm(p.getString("document_name"), p.getDouble("tfidf"));
				}
			}
		}
	}

	/**
	 * TFIDF + Title for scoring
	 * 
	 * @param queryTerms
	 */
	private void findMatchingPages(String[] queryTerms) {
		for (String term : queryTerms) {
			term = term.toLowerCase();
			List<Document> postings = MongoApp.db.getCollection("index").find(new Document("term", term))
					.into(new ArrayList<Document>());
			System.out.println("found " + postings.size() + " results for " + term);
			if (postings != null) {
				for (Document p : postings) {
					addPageScoreForTerm(p.getString("document_name"), p.getDouble("tfidf"));
					String title = p.getString("title");
					urlToTitleMap.put(p.getString("document_name"), title);
					if (title.contains(term)) {
						addPageScoreForTerm(p.getString("document_name"), 1.0);
					}
				}
			}
		}
	}

	/**
	 * Anchor text scoring
	 * 
	 * @param queryTerms
	 */
	private void findMatchingPagesInAnchorText(String[] queryTerms) {
		for (String term : queryTerms) {
			term = term.toLowerCase();
			List<Document> postings = MongoApp.db.getCollection("anchorindex").find(new Document("term", term))
					.into(new ArrayList<Document>());
			if (postings != null) {
				for (Document p : postings) {
					addPageScoreForTerm(p.getString("document_name"), p.getDouble("tfidf"));
				}
			}
		}
	}

	/**
	 * Cosine Similarity
	 * 
	 * @param queryTerms
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private void findMatchingPagesBasedOnCosine(String[] queryTerms) {
		// Assuming tf for every query term as 1 i.e. every term occurs only
		// once in the query
		for (String term : queryTerms) {
			term = term.toLowerCase();
			List<Document> postings = MongoApp.db.getCollection("index").find(new Document("term", term))
					.into(new ArrayList<Document>());
			if (postings != null) {
				for (Document p : postings) {
					updateCosineScoreForTerm(p.getString("document_name"),
							((ArrayList<Integer>) p.get("positions")).size(), p.getDouble("docVectorMagnitude"));
				}
			}
		}
		cosineMap = (HashMap<String, Double>) convertMapforSorting(cosineScoreMap);
	}

	/**
	 * Method used by findMatchingPagesBasedOnCosine for updatingt the numerator
	 * 
	 * @param documentName
	 * @param termFrequency
	 * @param docVectorMagnitude
	 */
	private void updateCosineScoreForTerm(String documentName, int termFrequency, double docVectorMagnitude) {
		if (cosineScoreMap.containsKey(documentName)) {
			cosineScoreMap.get(documentName)
					.setNumerator(cosineScoreMap.get(documentName).getNumerator() + termFrequency);
		} else {
			cosineScoreMap.put(documentName, new Tuple(termFrequency, docVectorMagnitude));
		}
	}

	/**
	 * Private method to convert a <String, Tuple> map to a <String, Double> Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Double> convertMapforSorting(HashMap<String, Tuple> map) {
		Map<String, Double> convertedMap = new HashMap<String, Double>();
		for (String doc : map.keySet()) {
			convertedMap.put(doc, map.get(doc).getScore());
		}
		return convertedMap;
	}

	/**
	 * Rank cosine results
	 * 
	 * @return
	 */
	private ArrayList<String> rankCosineResults() {
		cosineMap = sortMap(cosineMap);
		ArrayList<String> pages = new ArrayList<String>(cosineMap.keySet());
		if (pages.size() > 19) {
			return new ArrayList<String>(pages.subList(0, 19));
		}
		return pages;
	}

	private void addPageScoreForTerm(String documentName, double tfidf) {
		if (cumulativePageScoreMap.containsKey(documentName)) {
			cumulativePageScoreMap.put(documentName, cumulativePageScoreMap.get(documentName) + tfidf);
		} else {
			cumulativePageScoreMap.put(documentName, tfidf);
		}
	}

	private ArrayList<String> rankResults() {
		
		cumulativePageScoreMap = sortMap(cumulativePageScoreMap);
		ArrayList<String> pages = new ArrayList<String>(cumulativePageScoreMap.keySet());
		if (pages.size() > 19) {
			return new ArrayList<String>(pages.subList(0, 19));
		}
		return pages;
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
}
