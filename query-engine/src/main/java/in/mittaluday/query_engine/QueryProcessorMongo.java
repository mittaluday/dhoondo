package in.mittaluday.query_engine;

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

import org.bson.Document;

import in.mittaluday.file_indexer.MongoApp;
import in.mittaluday.file_indexer.Postings;

public class QueryProcessorMongo {
	Map<String, List<Postings>> index;
	HashMap<String, Double> cumulativePageScoreMap;
	HashMap<String, Tuple> cosineScoreMap;
	HashMap<String, Double> cosineMap;
	HashMap<String, String> urlToTitleMap;

	
	/**
	 * Class to store Numerator
	 * and Denominator tuple for 
	 * cosine similarity
	 * @author udaymittal
	 *
	 */
	class Tuple{

		double numerator;
		double denominator;
		double score;
		public Tuple(double numerator, double denominator) {
			this.numerator = numerator;
			this.denominator=denominator;
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
		public double getScore(){
			return numerator/denominator;
		}
	}
	
	
	public QueryProcessorMongo() throws ClassNotFoundException, IOException {
		cumulativePageScoreMap = new HashMap<String, Double>();
		urlToTitleMap = new HashMap<String, String>();
	}

	public ArrayList<Result> queryIndex(String query) {
		if (query == null || query.isEmpty()) {
			return new ArrayList<Result>();
		}
		String[] queryTerms = query.split(" ");
		findMatchingPages(queryTerms);
		findMatchingPagesInAnchorText(queryTerms);
		ArrayList<String> stringResults = rankResults();
		ArrayList<Result> results = createResultStructureList(stringResults);
		return results;
		
		//For cosine similarity use following
		
		//findMatchingPagesBasedOnCosine(queryTerms);
		//ArrayList<String> stringResults = rankCosineResults();
		
	}

	private ArrayList<Result> createResultStructureList(ArrayList<String> stringResults) {
		ArrayList<Result> results = new ArrayList<Result>();
		for(int i=0;i<stringResults.size();i++){
			results.add(new Result(stringResults.get(i), urlToTitleMap.get(stringResults.get(i))));
		}
		return results;
	}

	/**
	 * Just plain cumulative TFIDF score for ranking
	 * This should be used to calculate original NDCG 
	 * @param queryTerms
	 */
	@SuppressWarnings("unused")
	private void findMatchingPagesOld(String[] queryTerms) {
		for (String term : queryTerms) {
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
	 * @param queryTerms
	 */
	private void findMatchingPages(String[] queryTerms) {
		for (String term : queryTerms) {
			List<Document> postings = MongoApp.db.getCollection("index").find(new Document("term", term))
					.into(new ArrayList<Document>());
			System.out.println("found " + postings.size() + " for term " + term);
			if (postings != null) {
				for (Document p : postings) {
					addPageScoreForTerm(p.getString("document_name"), p.getDouble("tfidf"));
					String title = p.getString("title");
					System.out.println("document name : " + p.getString("document_name"));
					System.out.println("title : " + title);
					
					urlToTitleMap.put(p.getString("document_name"), title);
					if(title.contains(term)){
						addPageScoreForTerm(p.getString("document_name"), 1.0);
					}
				}
			}
		}
	}

	/**
	 * Anchor text scoring
	 * @param queryTerms
	 */
	private void findMatchingPagesInAnchorText(String[] queryTerms) {
		for (String term : queryTerms) {
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
	 * @param queryTerms
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private void findMatchingPagesBasedOnCosine(String[] queryTerms) {
		//Assuming tf for every query term as 1 i.e. every term occurs only once in the query
		for(String term: queryTerms){
			List<Document> postings = MongoApp.db.getCollection("index").find(new Document("term", term))
					.into(new ArrayList<Document>());
			if (postings != null) {
				for (Document p : postings) {
					updateCosineScoreForTerm(p.getString("document_name"), 
							((ArrayList<Integer>) p.get("positions")).size(),
							p.getDouble("docVectorMagnitude"));
				}
			}
		}
		cosineMap = (HashMap<String, Double>) convertMapforSorting(cosineScoreMap);
	}
	
	/**
	 * Method used by findMatchingPagesBasedOnCosine
	 * for updatingt the numerator
	 * @param documentName
	 * @param termFrequency
	 * @param docVectorMagnitude
	 */
	private void updateCosineScoreForTerm(String documentName, int termFrequency, double docVectorMagnitude){
		if(cosineScoreMap.containsKey(documentName)){
			cosineScoreMap.get(documentName).setNumerator(
					cosineScoreMap.get(documentName).getNumerator() + termFrequency);
		}
		else{
			cosineScoreMap.put(documentName, new Tuple(termFrequency, docVectorMagnitude));
		}
	}

	/**
	 * Private method to convert a <String, Tuple> map 
	 * to a <String, Double> Map
	 * @param map
	 * @return
	 */
	private Map<String, Double> convertMapforSorting(HashMap<String, Tuple> map){
		Map<String, Double> convertedMap = new HashMap<String, Double>();
		for (String doc : map.keySet()) {
			convertedMap.put(doc, map.get(doc).getScore());
		}
		return convertedMap;
	}
	
	/**
	 * Rank cosine results
	 * @return
	 */
	private ArrayList<String> rankCosineResults() {
		cosineMap = sortMap(cosineMap);
		ArrayList<String> pages = new ArrayList<String>(cosineMap.keySet());
		if (pages.size() > 9) {
			return new ArrayList<String>(pages.subList(0, 9));
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
		if (pages.size() > 9) {
			return new ArrayList<String>(pages.subList(0, 9));
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
